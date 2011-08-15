package net.ion.framework.convert.image.generator;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.io.IOUtils;

/**
 * @author jonck
 */
public class SimpleExample {
	private JFrame app;
	private JEditorPane editor;

	public static void main(String args[]) {
		new SimpleExample();
	}

	public SimpleExample() {
		initComponents();
		buildPanel();
	}

	private void initComponents() {
		setApp(new JFrame("HTML test"));
		setEditor(new JEditorPane());
	}

	private void buildPanel() {
		getApp().getContentPane().add(getEditor(), BorderLayout.CENTER);
		setHTML();
		getApp().pack();
		getApp().setVisible(true);
	}

	private void setHTML() {
		getEditor().setContentType("text/html");
		getEditor().setEditable(false);
		HTMLEditorKit kit = (HTMLEditorKit) getEditor().getEditorKit();
		try {
			String rule1 = "td.odd {font-size: 10pt; font-family:Verdana;" + "background-color: #FFFF99;}";
			String rule2 = "td.even {font-size: 10pt; font-family: Verdana;" + "background-color: #DDDDDD;}";

			HTMLDocument doc = (HTMLDocument) getEditor().getDocument();
			doc.getStyleSheet().addRule(rule1);
			doc.getStyleSheet().addRule(rule2);

			// String HTMLText = "<table border=\"1\" width=\"300\" cellspacing=\"5\" cellpadding=\"10\">" + "<tr><td class=\"odd\">Odd</td><td class=\"even\">Even</td></tr>" + "</table>";
			String HTMLText = IOUtils.toString(new FileInputStream("data/firstdoc.htm")) ;
			
			StringReader stringReader = new StringReader(HTMLText);
			kit.read(stringReader, doc, 0);
			stringReader.close();
			getEditor().setCaretPosition(0);
		} catch (IOException e) {
			System.out.println(e);
		} catch (BadLocationException e) {
			System.out.println(e);
		}
	}

	public JFrame getApp() {
		return app;
	}

	public void setApp(JFrame app) {
		this.app = app;
	}

	public JEditorPane getEditor() {
		return editor;
	}

	public void setEditor(JEditorPane editor) {
		this.editor = editor;
	}
}
