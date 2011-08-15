package net.ion.framework.res;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 * 시스템 문자셋으로 된 문자열과 Unicode로된 문자열을 서로 교환할 수 있게 하는 유틸
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class StringConverter extends JFrame// JDialog
{
	JSplitPane jSplitPane1 = new JSplitPane();
	JTextArea jTextArea1 = new JTextArea();
	JTextArea jTextArea2 = new JTextArea();

	public StringConverter() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setTitle("Properties String Converter");

		jTextArea1.setText("");
		jTextArea2.setText("");

		// GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// Object[] o =ge.getAllFonts();
		// for(int s=0;s<o.length;++s)
		// System.out.println(o[s]);
		// Font f = new Font(
		// jTextArea1.setFont(Font.decode("굴림"));
		// jTextArea2.setFont(Font.decode("Tahoma"));

		this.getContentPane().add(jSplitPane1, BorderLayout.CENTER);

		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);

		jSplitPane1.add(new JScrollPane(jTextArea1), JSplitPane.TOP);
		jSplitPane1.add(new JScrollPane(jTextArea2), JSplitPane.BOTTOM);

		// 윈도우 위치 및 크기
		Dimension dim = getToolkit().getScreenSize();

		Preferences prefs = Preferences.userNodeForPackage(this.getClass());

		int width = prefs.getInt("width", (int) (dim.width * 0.7));
		int height = prefs.getInt("height", (int) (dim.height * 0.4));
		int x = prefs.getInt("locationX", dim.width / 2 - width / 2);
		int y = prefs.getInt("locationY", dim.height / 2 - height / 2);
		int div = prefs.getInt("dividerLocation", height / 2);

		setSize(width, height);
		setLocation(x, y);
		jSplitPane1.setDividerLocation(div);

		// jTextArea2에 focus가 가도록
		jTextArea2.requestFocusInWindow();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Preferences prefs = Preferences.userNodeForPackage(this.getClass());

				int width = (int) ((StringConverter) e.getSource()).getSize().getWidth();
				int height = (int) ((StringConverter) e.getSource()).getSize().getHeight();
				int x = (int) ((StringConverter) e.getSource()).getLocation().getX();
				int y = (int) ((StringConverter) e.getSource()).getLocation().getY();
				int div = ((StringConverter) e.getSource()).jSplitPane1.getDividerLocation();

				prefs.putInt("width", width);
				prefs.putInt("height", height);
				prefs.putInt("locationX", x);
				prefs.putInt("locationY", y);
				prefs.putInt("dividerLocation", div);

				System.exit(0);
			}
		});

		jTextArea1.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {
				String s = jTextArea1.getText();
				// s=loadConvert(s);
				s = saveConvert(s, true);
				jTextArea2.setText(s);
			}
		});

		jTextArea2.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				String s = jTextArea2.getText();
				s = loadConvert(s);
				// s=saveConvert(s,true);
				jTextArea1.setText(s);
			}
		});

	}

	/*
	 * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their original forms
	 */
	private String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					// if (aChar == 't') aChar = '\t';
					// else if (aChar == 'r') aChar = '\r';
					// else if (aChar == 'n') aChar = '\n';
					// else if (aChar == 'f') aChar = '\f';
					// outBuffer.append(aChar);

					outBuffer.append('\\');
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/*
	 * Converts unicodes to encoded &#92;uxxxx and writes out any of the characters in specialSaveChars with a preceding slash
	 */
	private String saveConvert(String theString, boolean escapeSpace) {
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len * 2);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			switch (aChar) {
			// case ' ':
			// if (x == 0 || escapeSpace)
			// outBuffer.append('\\');
			//
			// outBuffer.append(' ');
			// break;
			// case '\\':outBuffer.append('\\'); outBuffer.append('\\');
			// break;
			// case '\t':outBuffer.append('\\'); outBuffer.append('t');
			// break;
			// case '\r':outBuffer.append('\\'); outBuffer.append('r');
			// break;
			// case '\f':outBuffer.append('\\'); outBuffer.append('f');
			// break;
			// case '\n':outBuffer.append('\\'); outBuffer.append('n');
			// break;
			case '\n':
				outBuffer.append('\n');
				break;

			default:
				if ((aChar < 0x0020) || (aChar > 0x007e)) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					// if (specialSaveChars.indexOf(aChar) != -1)
					// outBuffer.append('\\');
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	@SuppressWarnings("unused")
	private static final String specialSaveChars = "=: \t\r\n\f#!";
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		StringConverter sc = new StringConverter();
		sc.show();
	}
}
