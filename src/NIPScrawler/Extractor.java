package NIPScrawler;

import entity.Paper;
import entity.Paper.PaperBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Extractor {
	
	private String PdfDir;
	private String htmlDir;
	private String host;
	
	public Extractor (String PdfDir, String htmlDir, String host) {
		this.PdfDir = PdfDir;
		this.host = host;
		this.htmlDir = htmlDir;
	}
	
	public List<Paper> getPaperList(int year) {
		List<Paper> papers = new ArrayList<>();
		
		String filePath = htmlDir + "/NipsListPage-" + String.valueOf(year) + ".txt";
		if (!new File(filePath).isFile()) {
			System.out.println("File does not exist.");
			return new ArrayList<Paper>();
		}
		try {
			//open local txt file include the cataloge, read into stringbuilder content
			BufferedReader inLine = new BufferedReader(new FileReader(filePath));
			StringBuilder input = new StringBuilder();
			String line = new String();
			while ((line = inLine.readLine()) != null) {
				input.append(line);
			}
			inLine.close();
			String content = input.toString();
			//split the raw file by 4 or more white spaces, each entry is <li>...</li>
			String[] entries = content.split("\\s{4,}");
			
			//collect paper information
			int counter = 0;
			//System.out.print("Total number of papers is " + entries.length +".\n");
			for (String entry : entries) {
				//if (counter > 5) break;
				if (entry.contains("<a href=\"/paper/")) {
					//System.out.print("processing paper " + counter + "\n"); 
					papers.add(getPaperBuilder(entry, year).build());
					counter++;
				}
			}
			return papers;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Paper>();
	}
	
	public PaperBuilder getPaperBuilder(String entry, int year) {
		PaperBuilder builder = new PaperBuilder();
		try {
			String[] segments = entry.split("\"");
			if (segments.length < 1) { //not getting real data
				return null;
			}
			String id = segments[1].split("/|-")[2];
//			System.out.println("Id is " +id);
			String linkToPaper = host + segments[1].trim();
//			System.out.println("pagelink is " +linkToPaper);
			String title = segments[2].split("<|>")[1].toLowerCase().trim();
//			System.out.println("title is " +title);
			List<String> authors = new ArrayList<>();
			for (int i = 0; i < segments.length - 1; i++) {
				if (segments[i].equals("author")) {
					authors.add(segments[i + 1].split("<|>")[1]);
//					System.out.println("author: " + authors.get(authors.size() -1));
				}
			}
			
			String pageHtml = getPageHtml(linkToPaper);
			String pdfLink = getPdfLink(pageHtml);
//			System.out.println(pdfLink);
			String paperAbstract;
			paperAbstract = getAbstract(pageHtml);
//			System.out.println(paperAbstract);
			PdfCrawler crawler = new PdfCrawler(pdfLink, Integer.parseInt(id));
			paperAbstract = getAbstract(pageHtml);
			crawler.downloadPdf(PdfDir);
			if (paperAbstract == null || paperAbstract.isEmpty()) {
				paperAbstract = crawler.getAbstractFromPdf(PdfDir + "/Nips-"+ id +".pdf", PdfDir);
			}
			paperAbstract.replace('\n', ' ');
//			System.out.println(paperAbstract);
			builder.setTitle(title).setYear(year).setPaperID(id)
			       .setAuthors(authors).setPaperAbstract(paperAbstract).setPdfLink(pdfLink);
			return builder;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getPageHtml(String pageUrl) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(pageUrl).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000); //set timeout to 5 seconds
			int code = connection.getResponseCode();
			if (code != 200) {
				System.out.println("Link to single paper page failed.");
				return new String();
			}
			StringBuilder strBuilder = new StringBuilder();
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inLine = new String();
			while ((inLine = bf.readLine()) != null) {
				strBuilder.append(inLine);
			}
			bf.close();
			String html = strBuilder.toString();
			return html;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String();
	}
	
	//get pdfLink from paper webpage
	public String getPdfLink(String pageHtml) {
		String[] strings = pageHtml.split("\"");
		String link = new String();
		for (int i = 0 ;i < strings.length; i++) {
			if (strings[i].contains(".pdf")) {
				link = strings[i];
				break;
			}
		}
		
		if (link.contains("http")) {
			return link;
		} else {
			return new String(host + "/" + link);
		}
	}
	
	//get abstract from paper webpage
	public String getAbstract(String link) {
		if (link.contains("Abstract Missing")) {
			return new String();
		}
		String[] segments = link.split("<p class=\"abstract\">");
		String target = segments[1].split("</p>")[0];
		return target;
	}
	
	public void analyzeCatalog(int year) {
		//TODO
	}
}
