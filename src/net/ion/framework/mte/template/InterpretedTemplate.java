package net.ion.framework.mte.template;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.ion.framework.mte.DefaultModelAdaptor;
import net.ion.framework.mte.Engine;
import net.ion.framework.mte.ModelAdaptor;
import net.ion.framework.mte.ProcessListener;
import net.ion.framework.mte.ScopedMap;
import net.ion.framework.mte.TemplateContext;
import net.ion.framework.mte.ProcessListener.Action;
import net.ion.framework.mte.token.ElseToken;
import net.ion.framework.mte.token.EndToken;
import net.ion.framework.mte.token.ExpressionToken;
import net.ion.framework.mte.token.ForEachToken;
import net.ion.framework.mte.token.IfToken;
import net.ion.framework.mte.token.InvalidToken;
import net.ion.framework.mte.token.PlainTextToken;
import net.ion.framework.mte.token.StringToken;
import net.ion.framework.mte.token.Token;
import net.ion.framework.mte.token.TokenStream;

public class InterpretedTemplate extends AbstractTemplate {

	protected final TokenStream tokenStream;
	protected transient StringBuilder output;
	protected transient TemplateContext context;

	public InterpretedTemplate(String template, String sourceName, Engine engine) {
		this.template = template;
		this.engine = engine;
		this.sourceName = sourceName;
		tokenStream = new TokenStream(sourceName, template, engine.config().exprStartToken(), engine.config().exprEndToken());
		tokenStream.prefill();
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized Set<String> getUsedVariables() {
		if (this.usedVariables != null) {
			return this.usedVariables;
		}

		this.usedVariables = new TreeSet<String>();
		final Engine engine = Engine.createDefaultEngine();
		final ScopedMap scopedMap = new ScopedMap(Collections.EMPTY_MAP);

		ProcessListener processListener = new ProcessListener() {

			public void log(TemplateContext context, Token token, Action action) {
				if (token instanceof ExpressionToken) {
					String variable = ((ExpressionToken) token).getExpression();
					if (!isLocal(variable)) {
						usedVariables.add(variable);
					}
				}
			}

			// a variable is local if any enclosing foreach has it as a step
			// variable
			private boolean isLocal(String variable) {
				for (Token token : context.scopes) {
					if (token instanceof ForEachToken) {
						String foreachVarName = ((ForEachToken) token).getVarName();
						if (foreachVarName.equals(variable)) {
							return true;
						}
					}
				}
				return false;

			}

		};
		final Locale locale = Locale.getDefault();
		context = new TemplateContext(template, locale, sourceName, scopedMap, new DefaultModelAdaptor(), engine, engine.errorHandler(), processListener);

		transformPure(context);
		return usedVariables;
	}

	@Override
	public synchronized String transform(Map<String, Object> model, Locale locale, ModelAdaptor modelAdaptor, ProcessListener processListener) {
		try {
			context = new TemplateContext(template, locale, sourceName, new ScopedMap(model), modelAdaptor, engine, engine.errorHandler(), processListener);
			String transformed = transformPure(context);
			return transformed;
		} finally {
			context = null;
			output = null;
		}
	}

	protected String transformPure(TemplateContext context) {
		tokenStream.reset();
		output = new StringBuilder((int) (context.template.length() * context.engine.config().expansionSizeFactor()));
		tokenStream.nextToken();
		while (tokenStream.currentToken() != null) {
			content(false);
		}
		return output.toString();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	private void foreach(boolean inheritedSkip) {
		ForEachToken feToken = (ForEachToken) tokenStream.currentToken();
		Iterable iterable = (Iterable) feToken.evaluate(context);
		// begin a fresh iteration with a reset index
		feToken.setIterator(iterable.iterator());
		feToken.resetIndex();
		tokenStream.consume();

		context.model.enterScope();
		context.push(feToken);
		try {

			// in case we do not want to evaluate the body, we just do a quick
			// scan until the matching end
			if (inheritedSkip || !feToken.iterator().hasNext()) {
				Token contentToken;
				while ((contentToken = tokenStream.currentToken()) != null && !(contentToken instanceof EndToken)) {
					content(true);
				}
				if (contentToken == null) {
					engine.errorHandler().error("missing-end", feToken);
				} else {
					tokenStream.consume();
					context.notifyProcessListener(contentToken, Action.END);
				}
			} else {

				while (feToken.iterator().hasNext()) {

					context.model.put(feToken.getVarName(), feToken.advance());
					addSpecialVariables(feToken, context.model);

					// for each iteration we need to rewind to the beginning
					// of the for loop
					tokenStream.rewind(feToken);
					Token contentToken;
					while ((contentToken = tokenStream.currentToken()) != null && !(contentToken instanceof EndToken)) {
						content(false);
					}
					if (contentToken == null) {
						engine.errorHandler().error("missing-end", feToken);
					} else {
						tokenStream.consume();
						context.notifyProcessListener(contentToken, Action.END);
					}
					if (!feToken.isLast()) {
						output.append(feToken.getSeparator());
					}
				}
			}

		} finally {
			context.model.exitScope();
			context.pop();
		}
	}

	private void condition(boolean inheritedSkip) {
		IfToken ifToken = (IfToken) tokenStream.currentToken();
		tokenStream.consume();

		context.push(ifToken);
		try {
			boolean localSkip;
			if (inheritedSkip) {
				localSkip = true;
			} else {
				localSkip = !(Boolean) ifToken.evaluate(context);
			}

			Token contentToken;
			while ((contentToken = tokenStream.currentToken()) != null && !(contentToken instanceof EndToken) && !(contentToken instanceof ElseToken)) {
				content(localSkip);
			}

			if (contentToken instanceof ElseToken) {
				tokenStream.consume();
				// toggle for else branch
				if (!inheritedSkip) {
					localSkip = !localSkip;
				}
				context.notifyProcessListener(contentToken, inheritedSkip ? Action.SKIP : Action.EVAL);

				while ((contentToken = tokenStream.currentToken()) != null && !(contentToken instanceof EndToken)) {
					content(localSkip);
				}

			}

			if (contentToken == null) {
				engine.errorHandler().error("missing-end", ifToken);
			} else {
				tokenStream.consume();
				context.notifyProcessListener(contentToken, Action.END);
			}
		} finally {
			context.pop();
		}
	}

	private void content(boolean skip) {
		Token token = tokenStream.currentToken();
		context.notifyProcessListener(token, skip ? Action.SKIP : Action.EVAL);
		if (token instanceof PlainTextToken) {
			tokenStream.consume();
			if (!skip) {
				output.append(token.getText());
			}
		} else if (token instanceof StringToken) {
			tokenStream.consume();
			if (!skip) {
				String expanded = (String) token.evaluate(context);
				output.append(expanded);
			}
		} else if (token instanceof ForEachToken) {
			foreach(skip);
		} else if (token instanceof IfToken) {
			condition(skip);
		} else if (token instanceof ElseToken) {
			tokenStream.consume();
			engine.errorHandler().error("else-out-of-scope", token);
		} else if (token instanceof EndToken) {
			tokenStream.consume();
			engine.errorHandler().error("unmatched-end", token);
		} else if (token instanceof InvalidToken) {
			tokenStream.consume();
			engine.errorHandler().error("invalid-expression", token);
		} else {
			tokenStream.consume();
			// what ever else there may be, we just evaluate it
			String evaluated = (String) token.evaluate(context);
			output.append(evaluated);
		}

	}

	@Override
	public String toString() {
		return template;
	}
}
