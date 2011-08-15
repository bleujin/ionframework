package net.ion.framework.template.ref;

import java.util.ArrayList;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.configuration.ConfigurationException;
import net.ion.framework.configuration.ConfigurationFactory;
import net.ion.framework.template.CompiledTemplate;
import net.ion.framework.template.CompilerException;
import net.ion.framework.template.InvalidSetupException;
import net.ion.framework.template.Page;
import net.ion.framework.template.PageReceiver;
import net.ion.framework.template.RenderingSupport;
import net.ion.framework.template.Template;
import net.ion.framework.template.TemplateCompiler;
import net.ion.framework.template.TemplateRuntime;
import net.ion.framework.template.TemplateRuntimeException;
import net.ion.framework.template.tagext.TemplateContext;

/**
 * template framework를 사용하는 참조 구현
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class Renderer {
	private RenderingSupport rs;
	private TemplateCompiler compiler;

	public Renderer(String name, String taglibFile, int handlerHostSize, int parserHostSize) throws InvalidSetupException {
		try {
			ConfigurationFactory factory = ConfigurationFactory.getInstance(name);
			factory.build(taglibFile);
			Configuration taglib = factory.getConfiguration("taglib");
			this.rs = new RenderingSupport(taglib, handlerHostSize, parserHostSize);
		} catch (ConfigurationException ex) {
			throw new InvalidSetupException(ex);
		}

		this.compiler = new TemplateCompiler(rs);
	}

	public void destory() {
		rs.destroy();
	}

	public RenderingSupport getRenderingSupport() {
		return this.rs;
	}

	public CompiledTemplate compile(Template template) throws CompilerException {
		return compiler.compile(template);
	}

	public Page[] exec(CompiledTemplate template, long onePageTimeOutMSec) throws TemplateRuntimeException {
		return exec(template, new GenericTemplateContext(), onePageTimeOutMSec);
	}

	public Page[] exec(CompiledTemplate template, TemplateContext context, long onePageTimeOutMSec) throws TemplateRuntimeException {
		InnerPageReceiver receiver = new InnerPageReceiver();
		TemplateRuntime runtime = new TemplateRuntime(this.rs, receiver, onePageTimeOutMSec);

		runtime.exec(context, template);
		return receiver.list.toArray(new Page[0]);
	}

	class InnerPageReceiver implements PageReceiver {
		public final ArrayList<Page> list;

		public InnerPageReceiver() {
			list = new ArrayList<Page>();
		}

		public void receivePage(Page page) {
			list.add(page);
		}
	}
}
