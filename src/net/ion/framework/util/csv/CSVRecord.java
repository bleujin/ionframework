package net.ion.framework.util.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.ion.framework.util.StringUtil;

public class CSVRecord {
	private List<String> fields = new ArrayList<String>();

	public CSVRecord(String recordText, String fieldSeparator) {

		/**
		 * true if within a quote
		 **/
		boolean inQuote = false;
		/**
		 * temp saved field value
		 **/
		String savedField = "";
		/**
		 * current field value
		 **/
		String curField = "";
		/**
		 * field being built
		 **/
		String field = "";
		/**
		 * array of records. split it according to the field delimiter. The
		 * default String.split() is not accurate according to the M$ view.
		 **/
		char fieldSeparatorChar = fieldSeparator.charAt(0);

		// String records[] = recordText.split(fieldSeparator);
		String records[] = splitIncludeEmpty(recordText, fieldSeparator);
		for (int rec = 0; rec < records.length; rec++) {
			field = records[rec];
			// Add this field to currently saved field.
			curField = savedField + field;
			// Iterate over current field.
			int curFieldLength = curField.length();
			for (int i = 0; i < curFieldLength; i++) {
				char ch = curField.charAt(i); // current char

				if (!inQuote && ch == '"') {
					if (i == 0 || (i - 1 >= 0 && curField.charAt(i - 1) == fieldSeparatorChar)) {
						inQuote = true;
					}
				} else if (inQuote && ch == '"') {
					if (i + 1 == curFieldLength) {
						inQuote = false;
					} else if (i + 1 < curFieldLength && curField.charAt(i + 1) != '"' && curField.charAt(i + 1) == fieldSeparatorChar) {
						inQuote = false;
					} else if (i + 1 < curFieldLength && curField.charAt(i + 1) == '"') {
						i++;
					}
				}
			}// end of current field

			if (inQuote) {
				savedField = curField + fieldSeparator;
				inQuote = false;
			} else {
				char ch = (curFieldLength > 0 ? curField.charAt(0) : ' '); // current
																			// char
				char lst = (curFieldLength - 1 > 0 ? curField.charAt(curFieldLength - 1) : ' ');
				if (ch == '"' && lst == '"') {
					// Strip leading and trailing quotes
					curField = curField.substring(1, curFieldLength - 1);

					curField = StringUtil.replace(curField, "\"\"", "\"");
				}

				fields.add(curField);
				savedField = "";
			}
		}// end of for each record
	}

	public String getField(int index) {
		if (index < fields.size())
			return fields.get(index);
		else
			return "";
	}

	public int count() {
		return fields.size();
	}

	public String[] toArray() {
		return fields.toArray(new String[0]);
	}

	/**
	 * <pre>
	 * ThothUtils.splitIncludeEmpty(null, *)         = null
	 * ThothUtils.splitIncludeEmpty("", *)           = []
	 * ThothUtils.splitIncludeEmpty("a.b.c", ".")    = ["a", "b", "c"]
	 * ThothUtils.splitIncludeEmpty("a..b.c", ".")   = ["a", "", "b", "c"]
	 * ThothUtils.splitIncludeEmpty("a...", ".")     = ["a", "", "", ""]
	 * ThothUtils.splitIncludeEmpty("a:b:c", ".")    = ["a:b:c"]
	 * ThothUtils.splitIncludeEmpty("a\tb\nc", null) = ["a\tb\nc"]
	 * ThothUtils.splitIncludeEmpty("a b c", " ")    = ["a", "b", "c"]
	 * </pre>
	 */
	private String[] splitIncludeEmpty(String str, String delim) {
		if (str == null) {
			return new String[] {};
		}
		if (delim == null || delim.length() == 0) {
			return new String[] { str };
		}

		/*
		 * return StringUtils.split(str, delim);
		 */
		StringTokenizer tokenizer = new StringTokenizer(str, delim, true);
		String lastValue = "";
		ArrayList<String> tokenList = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.equals(delim)) {
				tokenList.add(lastValue);
				lastValue = "";
			} else {
				lastValue = token;
			}
		}
		tokenList.add(lastValue);

		return tokenList.toArray(new String[0]);

	}


	public String toString() {
		return "CSVRecord{" + "fields=" + fields + "}";
	}
}
