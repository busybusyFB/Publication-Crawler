package ICLRcrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class Preprocessor {
	public void convertHtmlToText(String htmlDir) {
		if (htmlDir == null || htmlDir.isEmpty() || !new File(htmlDir).isDirectory()) {
			System.out.println("Not a directory, try a new name");
			return;
		}
		File[] files = new File(htmlDir).listFiles(new FilenameFilter() {
														public boolean accept(File directory, String fileName) {
														return fileName.endsWith(".html");
												   }});
//		File[] files = new File(htmlDir).listFiles();
		try {
			for(int i = 0; i < files.length; i++) {
				BufferedReader reader = new BufferedReader(new FileReader(files[i]));
				String txtName = files[i].getName().replaceAll("html", "txt");
				BufferedWriter writer = new BufferedWriter(new FileWriter(htmlDir +  "/" + txtName, false));
				String inLine = new String();
				while ((inLine = reader.readLine()) != null) {
					writer.append(inLine);
				}
				reader.close();
				writer.close();
			}
			System.out.println("Converted " + files.length + " html to txt files.");
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
