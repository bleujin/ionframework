package net.ion.framework.convert.test;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// import com.lowagie.text.pdf.draw.LineSeparator;
// import com.lowagie.text.pdf.draw.VerticalPositionMark;

/**
 * 
 * @author Sushma P
 */
public class PDFHeader {

	private void createPDF() throws DocumentException {
		try {
			File inputFile = new File("data/nmms_1852243.jpg");
			Document document = new Document(PageSize.A3);
			// VerticalPositionMark separator = new LineSeparator(1, 80, Color.RED, Element.ALIGN_RIGHT, -2);
			PdfWriter.getInstance(document, new FileOutputStream("data/SamplePicture.pdf"));
			String address = "Himagiri Meadows, \n BG Road, \n Blr-83";
			HeaderFooter header = new HeaderFooter(new Phrase(address), false);
			header.setBorder(Rectangle.NO_BORDER);
			header.setAlignment(Element.ALIGN_RIGHT);
			document.setHeader(header);

			// VerticalPositionMark separator = new LineSeparator(1, 80, Color.RED, Element.ALIGN_RIGHT, -2);

			HeaderFooter footer = new HeaderFooter(new Phrase("Page no: "), true);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);

			Image img = Image.getInstance(inputFile.getCanonicalPath());

			img.setAlignment(Element.ALIGN_LEFT);
			img.setAbsolutePosition(0, 825 - img.getScaledHeight());

			document.open();
			document.add(img);
			document.add(new Paragraph("Helloo:"));

			document.close();
		} catch (IOException ex) {
			Logger.getLogger(PDFHeader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		try {
			PDFHeader pdfHeader = new PDFHeader();
			pdfHeader.createPDF();
		} catch (DocumentException ex) {
			Logger.getLogger(PDFHeader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}