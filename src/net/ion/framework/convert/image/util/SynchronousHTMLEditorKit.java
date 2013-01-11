package net.ion.framework.convert.image.util;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;

/**
 * @author Yoav Aharoni
 */
public class SynchronousHTMLEditorKit extends HTMLEditorKit {

	public Document createDefaultDocument() {
		HTMLDocument doc = (HTMLDocument) super.createDefaultDocument();
		doc.setAsynchronousLoadPriority(-1);

		return doc;
//		try {
//			HTMLDocument doc = (HTMLDocument) super.createDefaultDocument();
//			doc.setAsynchronousLoadPriority(-1);
//			
//			doc.getStyleSheet().addRule(IOUtils.toString(new FileReader("./data/_today.css")));
//			doc.getStyleSheet().addRule(IOUtils.toString(new FileReader("./data/_common.css")));
//			doc.getStyleSheet().addRule(IOUtils.toString(new FileReader("./data/_comic.css")));
//			doc.getStyleSheet().addRule(IOUtils.toString(new FileReader("./data/_anicontest.css")));
//			return doc;
//		} catch (IOException ex) {
//			throw new IllegalStateException(ex) ;
//		}
	}

	public ViewFactory getViewFactory() {
		return new HTMLFactory() {
			public View create(Element elem) {
				View view = super.create(elem);
				if (view instanceof ImageView) {
					((ImageView) view).setLoadsSynchronously(true);
				}
				return view;
			}
		};
	}
}
