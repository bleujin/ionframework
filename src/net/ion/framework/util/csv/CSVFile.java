package net.ion.framework.util.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
	private CSVRecord titleRecord;
	private List<CSVRecord> records = new ArrayList<CSVRecord>();

	// ----- Constructor -----

	public CSVFile(Reader reader, boolean hasTitle, String fieldSeparator) throws IOException {
		this(reader, hasTitle, fieldSeparator, 0, -1);
	}

	public CSVFile(Reader reader, boolean hasTitle, String fieldSeparator, int startRow, int rowLength) throws IOException {
		if (rowLength == 0) {
			return;
		}

		BufferedReader bufferedReader = new BufferedReader(reader);
		boolean inQuote = false;
		String savedRecord = "";
		String curRecord = "";

		if (hasTitle) {
			startRow++;
		}

		// Iterate over each split, looking for incomplete quoted strings.
		char fieldSeparatorChar = fieldSeparator.charAt(0);
		int rowIndex = 0;
		String line;
		// long startTime = System.currentTimeMillis();
		while ((line = bufferedReader.readLine()) != null) {
			curRecord = savedRecord + line;

			int curRecordLength = curRecord.length();
			for (int i = 0; i < curRecordLength; i++) {
				char ch = curRecord.charAt(i);

				if (!inQuote && ch == '"') {
					if (i == 0 || (i - 1 >= 0 && curRecord.charAt(i - 1) == fieldSeparatorChar)) {
						inQuote = true;
					}
				} else if (inQuote && ch == '"') {
					if (i + 1 == curRecordLength) {
						inQuote = false;
					} else if (i + 1 < curRecordLength && curRecord.charAt(i + 1) != '"' && curRecord.charAt(i + 1) == fieldSeparatorChar) {
						inQuote = false;
					} else if (i + 1 < curRecordLength && curRecord.charAt(i + 1) == '"') {
						i++;
					}
				}
			}

			if (inQuote) {
				// A space is currently used to replace the row delimiter when
				// found within a text field
				savedRecord = curRecord + "\n";
				inQuote = false;
			} else {
				if (rowIndex == 0) {
					titleRecord = new CSVRecord(curRecord, fieldSeparator);
				}
				if (rowIndex >= startRow) {
					records.add(new CSVRecord(curRecord, fieldSeparator));

					if (rowLength != -1 && records.size() >= rowLength) {
						return;
					}
				}

				rowIndex++;
				savedRecord = "";
			}
		}
	}

	public CSVRecord getTitleRecord() {
		return titleRecord;
	}

	public CSVRecord getRecord(int index) {
		return records.get(index);
	}

	public int count() {
		return records.size();
	}

	public CSVRecord[] toArray() {
		return records.toArray(new CSVRecord[0]);
	}
}
