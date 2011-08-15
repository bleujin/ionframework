package net.ion.framework.convert.image;

import junit.framework.TestCase;

import net.ion.framework.convert.image.generator.HtmlImageGenerator;

import org.w3c.tidy.Tidy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import org.w3c.dom.Document;

public class TestImage extends TestCase {

	public void testToImage() throws Exception {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(new URL("http://comic.naver.com/webtoon/weekdayList.nhn?week=thu")) ;
		imageGenerator.saveAsImage("data/include_css.png");
	}

	public void testFileToImage() throws Exception {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(new File("data/firstdoc.htm").toURL()) ;
		imageGenerator.saveAsImage("data/firstdoc.png");
	}

	public void testIONToImage() throws Exception {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(new URL("http://comic.naver.com/webtoon/detail.nhn?titleId=25735&no=142&weekday=thu")) ;
		imageGenerator.saveAsImage("data/ion.png");
	}
}
