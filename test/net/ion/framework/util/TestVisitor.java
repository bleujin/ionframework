package net.ion.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

public class TestVisitor extends TestCase {

	public void testSize() throws Exception{
		FileTraverse d = new FileTraverse(new File("C:/Intel/Logs"));
		LineSize s = new LineSize();
		d.traverse(s);
		Debug.line(s.getSum());
	}
}

class LineSize implements Visitor {
	private long sum = 0;

	public void accept(File f) throws IOException {
		BufferedReader reader =  new BufferedReader(new FileReader(f)) ;
		while(reader.readLine() != null) {
			sum ++ ;
		}
	}
	
	public long getSum(){
		return sum;
	}
}


interface Visitor {
	void accept(File f) throws IOException;
}

class FileTraverse {
	
	private File file ;
	FileTraverse(File file){
		this.file = file ;
	}
	
	public void traverse(Visitor v) throws IOException{
		if (file.isDirectory()){
			File[] child =  file.listFiles() ;
			for (File cfile : child) {
				v.accept(cfile) ;
			}
		} else {
			v.accept(file) ;
		}
	}
	
	
}