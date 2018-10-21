package NIPScrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;


public class PdfCrawler {
	
	private String pdfLink;
	private int paperID;
	
	public PdfCrawler(String pageLink, int paperID) {
		this.pdfLink = pageLink;
		this.paperID = paperID;
	}
	
	public boolean downloadPdf(String outputDir) {
		try {
			//establish connection
			HttpURLConnection conn = (HttpURLConnection) new URL(pdfLink).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			int responseCode = conn.getResponseCode();
//			System.out.println("Sending request to URL: " + pdfLink);
//			System.out.println("Response code is " + responseCode);
			if (responseCode != 200) {
				return false;
			}
//			System.out.println(conn.getContentType());
			if (!conn.getContentType().equalsIgnoreCase("application/pdf")) {
//				System.out.println("Not a pdf file.");
				return false;
			}
			
			//read pdf content
//			System.out.println("Extracting pdf file.");
			InputStream inStream = conn.getInputStream();
			byte[] barr = new byte[1024];
			int length;
			String fileName = "Nips-" + String.valueOf(paperID) + ".pdf";
//			System.out.println("Writing to local file:" + outputDir + "/" + fileName);
			FileOutputStream outStr = new FileOutputStream(outputDir + "/" + fileName);
	        while ((length = inStream.read(barr)) != -1) {
	        	outStr.write(barr, 0, length);
	        }
	        outStr.flush();
	        outStr.close();
	        inStream.close();
//			System.out.println("Writing success\nFinished");
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getAbstractFromPdf(String filePath, String outputDir) {
		if (!new File(filePath).isFile()) {
			System.out.println(filePath + " does not exist.");
			return new String();
		}
		try {
			//read context from pdf file
			PDDocument document = PDDocument.load(new File(filePath));
			document.getClass();
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            PDFTextStripper tStripper = new PDFTextStripper();
            String pdfFileInText = tStripper.getText(document);
            String lines[] = pdfFileInText.split("\\n?\\r");
            StringBuilder strBuilder = new StringBuilder();
            boolean record = false;
            for (String line : lines) {
            	if (line.contains("Abstract")) {
            		record = true;
            		continue;
            	}
            	if (line.contains("Introduction")) {
            		break;
            	}
            	if (record) {
            		strBuilder.append(line);
            	}
            }
//            System.out.println(strBuilder);
            String paperAbstract = strBuilder.toString();
            paperAbstract.replace("  "," ");
            return paperAbstract;
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String();
	}
	
}
