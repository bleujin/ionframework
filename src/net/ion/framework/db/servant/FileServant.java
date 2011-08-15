package net.ion.framework.db.servant;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileServant extends ExtraServant {
	private boolean isTrace = false;
	private int limitMilisecond;
	private String fileName;

	public FileServant(boolean isTrace, int limitMilisecond, String fileName) {
		this.isTrace = isTrace;
		this.limitMilisecond = limitMilisecond;
		this.fileName = fileName;
	}

	protected void handle(AfterTask atask) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.write(atask.getQueryable().toString());
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected boolean isDealWith(AfterTask atask) {
		return this.isTrace && atask.getEnd() - atask.getStart() > this.limitMilisecond;
	}

	public ExtraServant newCloneInstance() {
		return new FileServant(this.isTrace, this.limitMilisecond, this.fileName);
	}
}