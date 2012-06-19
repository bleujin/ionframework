package net.ion.framework.db.bean.handlers;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class CSVReader {

	public CSVReader(InputStream input, String encoding) throws UnsupportedEncodingException {
		this(new InputStreamReader(input, encoding), ',');
	}

	public CSVReader(InputStream input, String encoding, char separator) throws UnsupportedEncodingException {
		this(new InputStreamReader(input, encoding), separator);
	}

	public CSVReader(Reader r, char separator) {
		/* convert Reader to BufferedReader if necessary */
		if (r instanceof BufferedReader) {
			this.r = (BufferedReader) r;
		} else {
			this.r = new BufferedReader(r);
		}
		this.separator = separator;
	}

	public CSVReader(Reader r) {
		/* convert Reader to BufferedReader if necessary */
		if (r instanceof BufferedReader) {
			this.r = (BufferedReader) r;
		} else {
			this.r = new BufferedReader(r);
		}
		this.separator = ',';
	}

	// private static final boolean debugging = true;

	private BufferedReader r;

	private char separator;

	private static final int EOL = 0;
	private static final int ORDINARY = 1;
	private static final int QUOTE = 2;
	private static final int SEPARATOR = 3;
	private static final int WHITESPACE = 4;

	private int categorise(char c) {
		switch (c) {
		case ' ':
		case '\r':
		case 0xff:
			return WHITESPACE;
			// case ';':
			// case '!':
		case '#':
			// return EOL;
		case '\n':
			return EOL; /* artificially applied to end of line */
		case '\"':
			return QUOTE;
		default:
			if (c == separator) { /* dynamically determined so can't use as case label */
				return SEPARATOR;
			} else if ('!' <= c && c <= '~') { /* do our tests in crafted order, hoping for an early return */
				return ORDINARY;
			} else if (0x00 <= c && c <= 0x20) {
				return WHITESPACE;
			} else if (Character.isWhitespace(c)) {
				return WHITESPACE;
			} else {
				return ORDINARY;
			}
		}
	}

	private static final int SEEKINGSTART = 0;
	private static final int INPLAIN = 1;
	private static final int INQUOTED = 2;
	private static final int AFTERENDQUOTE = 3;
	private static final int SKIPPINGTAIL = 4;

	private String line = null;

	private int lineCount = 0;

	public String[] getLine() throws EOFException, IOException {
		Vector<String> lineArray = new Vector<String>();
		String token = null;
		String returnArray[] = null;

		while (lineArray.size() == 0) {
			while ((token = get()) != null) {
				lineArray.add(token);
			}
		}

		returnArray = new String[lineArray.size()];

		for (int ii = 0; ii < lineArray.size(); ii++) {
			returnArray[ii] = lineArray.elementAt(ii).toString();
		}

		return returnArray;
	}

	private String get() throws EOFException, IOException {
		StringBuffer field = new StringBuffer(50);
		readLine();

		int state = SEEKINGSTART; /* start seeking, even if partway through a line */

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			int category = categorise(c);
			switch (state) {
			case SEEKINGSTART: {
				switch (category) {
				case WHITESPACE:
					break; /* ignore */
				case QUOTE:
					state = INQUOTED;
					break;
				case SEPARATOR:
					line = line.substring(i + 1); /* end of empty field */
					return "";
				case EOL:
					line = null; /* end of line */
					return null;
				case ORDINARY:
					field.append(c);
					state = INPLAIN;
					break;
				}
				break;
			}
			case INPLAIN: {
				switch (category) {
				case QUOTE:
					throw new IOException("Malformed CSV stream. Missing quote at start of field on line " + lineCount);
				case SEPARATOR:

					/* done */
					line = line.substring(i + 1);
					return field.toString().trim();
				case EOL:
					line = line.substring(i); /* push EOL back */
					return field.toString().trim();
				case WHITESPACE:
					field.append(' ');
					break;
				case ORDINARY:
					field.append(c);
					break;
				}
				break;
			}
			case INQUOTED: {
				switch (category) {
				case QUOTE:
					state = AFTERENDQUOTE;
					break;
				case EOL:
					throw new IOException("Malformed CSV stream. Missing quote after field on line " + lineCount);
				case WHITESPACE:
					field.append(' ');
					break;
				case SEPARATOR:
				case ORDINARY:
					field.append(c);
					break;
				}
				break;
			}

			case AFTERENDQUOTE: {
				switch (category) {
				case QUOTE:
					field.append(c);
					state = INQUOTED;
					break;
				case SEPARATOR:
					line = line.substring(i + 1);
					return field.toString().trim();
				case EOL:
					line = line.substring(i); /* push back eol */
					return field.toString().trim();
				case WHITESPACE:
					state = SKIPPINGTAIL; /* ignore trailing spaces up to separator */
					break;
				case ORDINARY:
					throw new IOException("Malformed CSV stream, missing separator after field on line " + lineCount);
				}
				break;
			} // end of AFTERENDQUOTE
			case SKIPPINGTAIL: {
				switch (category) {
				case SEPARATOR:
					line = line.substring(i + 1);
					return field.toString().trim();
				case EOL:
					line = line.substring(i); /* push back eol */
					return field.toString().trim();
				case WHITESPACE:
					break;
				case QUOTE:
				case ORDINARY:
					throw new IOException("Malformed CSV stream, missing separator after field on line " + lineCount);
				}
				break;
			}
			}
		}
		throw new IOException("Program logic bug. Should not reach here. Processing line " + lineCount);
	}

	private void readLine() throws EOFException, IOException {
		if (line == null) {
			line = r.readLine(); /* this strips platform specific line ending */
			if (line == null) {
				/* null means EOF, yet another inconsistent Java convention. */
				throw new EOFException();
			} else {
				line += '\n'; /* apply standard line end for parser to find */
				lineCount++;
			}
		}
	}

	public void skip(int fields) throws EOFException, IOException {
		if (fields <= 0) {
			return;
		}
		for (int i = 0; i < fields; i++) {
			get();
		}
	}

	public void skipToNextLine() throws EOFException, IOException {
		if (line == null) {
			readLine();
		}
		line = null;
	}

	public void close() throws IOException {
		if (r != null) {
			r.close();
			r = null;
		}
	}

}
