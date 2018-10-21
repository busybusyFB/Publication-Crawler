package ICLRcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import entity.Paper;
import entity.Paper.PaperBuilder;

public class Extractor {
	
	public String readTxt(String filePath) {
		if (filePath == null || filePath.isEmpty() || !new File(filePath).isFile()) {
			System.out.println("Error: no such file");
			return new String();
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line;
			StringBuilder builder = new StringBuilder();
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
			String content = builder.toString();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String();
	}
	
	public List<Paper> extractPapers2018Workshop(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		String[] lines = content.split("\\s{4,}");
		//locate the target ranges
		int acceptedStart = 0;
		int acceptedEnd = 0;
		int rejectedStart = 0;
		int rejectedEnd = 0;
		for (int i = 0; i< lines.length; i++) {
			if (lines[i].contains("id=\"accepted-papers\"")) {
//				System.out.println("i " + i);
				acceptedStart = i;
			} else if (lines[i].contains("id=\"rejected-papers\"")) {
				acceptedEnd = i - 1;
				rejectedStart = i;
			} else if (lines[i].contains("id=\"withdrawn-papers")) {
				rejectedEnd = i - 1;
				break;
			}
		}
		int accptPaper = 0;
		for (int i = acceptedStart; i < acceptedEnd + 1;i++) {
//			if (accptPaper > 3) break;
			int entryStart = i;
			int entryEnd = findNextEntryStart(lines, entryStart, acceptedEnd) - 1;
			i = entryEnd;
//			System.out.println("start " + entryStart + lines[entryStart] + "\n");
//			System.out.println("end " + entryEnd + lines[entryEnd] + "\n");
			Paper paper = getPaperBuilder(lines, entryStart, entryEnd).build();
			if (paper.getPaperID() == null || paper.getPaperID().isEmpty()) {
				continue;
			}
			accptPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Papers: " + accptPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
//		papers.get(papers.size() - 1).printAllFields();
		papers.add(new PaperBuilder().build());
		
		//extract rejected papers
		int rejectedPaper = 0;
		for (int i = rejectedStart; i < rejectedEnd + 1;i++) {
//			if (accptOralPaper > 2) break;
			int entryStart = i;
			int entryEnd = findNextEntryStart(lines, entryStart, rejectedEnd) - 1;
			i = entryEnd;
			Paper paper = getPaperBuilder(lines, entryStart, entryEnd).build();
			if (paper.getPaperID() == null || paper.getPaperID().isEmpty()) {
				continue;
			}
			rejectedPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}
//		papers.get(papers.size() - 1).printAllFields();
		System.out.println("Rejected Papers: " + rejectedPaper + ", total size is " + papers.size());
		
		return papers;
	}
	
	public List<Paper> extractPapers2018Conference(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		String[] lines = content.split("\\s{4,}");
		//locate the target ranges
		int oralStart = 0;
		int oralEnd = 0;
		int posterStart = 0;
		int posterEnd = 0;
		int rejectedStart = 0;
		int rejectedEnd = 0;
		for (int i = 0; i< lines.length; i++) {
			if (lines[i].contains("id=\"accepted-oral-papers")) {
//				System.out.println("i " + i);
				oralStart = i;
			} else if (lines[i].contains("id=\"accepted-poster-papers")) {
				oralEnd = i - 1;
				posterStart = i;
			} else if (lines[i].contains("id=\"workshop-papers")) {
				posterEnd = i - 1;
			} else if (lines[i].contains("id=\"rejected-papers")) {
				rejectedStart = i;
			} else if (lines[i].contains("id=\"withdrawn-papers")) {
				rejectedEnd = i - 1;
				break;
			}
		}
		
		//extract accepted oral papers
		int accptOralPaper = 0;
		for (int i = oralStart; i < oralEnd + 1;i++) {
//			if (accptOralPaper > 3) break;
			int entryStart = i;
			int entryEnd = findNextEntryStart(lines, entryStart, oralEnd) - 1;
			i = entryEnd;
//			System.out.println("start " + entryStart + lines[entryStart] + "\n");
//			System.out.println("end " + entryEnd + lines[entryEnd] + "\n");
			Paper paper = getPaperBuilder(lines, entryStart, entryEnd).build();
			if (paper.getPaperID() == null || paper.getPaperID().isEmpty()) {
				continue;
			}
			accptOralPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Oral Papers: " + accptOralPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());
		
		int accptPosterPaper = 0;
		for (int i = posterStart; i < posterEnd + 1;i++) {
//			if (accptPosterPaper > 2) break;
			int entryStart = i;
			int entryEnd = findNextEntryStart(lines, entryStart, posterEnd) - 1;
//			System.out.println(lines[entryStart]);
//			System.out.println(lines[entryEnd]);
			i = entryEnd;
			Paper paper = getPaperBuilder(lines, entryStart, entryEnd).build();
			if (paper.getPaperID() == null || paper.getPaperID().isEmpty()) {
				continue;
			}
			accptPosterPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}
		System.out.println("Accepted Poster Papers: " + accptPosterPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());
		
		
		//extract rejected papers
		int rejectedPaper = 0;
		for (int i = rejectedStart; i < rejectedEnd + 1;i++) {
//			if (accptOralPaper > 2) break;
			int entryStart = i;
			int entryEnd = findNextEntryStart(lines, entryStart, rejectedEnd) - 1;
			i = entryEnd;
			Paper paper = getPaperBuilder(lines, entryStart, entryEnd).build();
			if (paper.getPaperID() == null || paper.getPaperID().isEmpty()) {
				continue;
			}
			rejectedPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}
		System.out.println("Rejected Papers: " + rejectedPaper + ", total size is " + papers.size());
		return papers;
	}
	
	public List<Paper> extractPapers2017Conference(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		String paperInfo = content.split("\\s{4,}")[44];
		String[] lines = paperInfo.split("><");
		//locate the target ranges
		int oralStart = 0;
		int oralEnd = 0;
		int posterStart = 0;
		int posterEnd = 0;
		int rejectedStart = 0;
		int rejectedEnd = lines.length - 1;
		for (int i = 0; i< lines.length; i++) {
			if (lines[i].contains("Paper decision: Accept (Oral)")) {
//				System.out.println("Paper decision: Accept (Oral)" + i);
				oralStart = i;
			}
			if (lines[i].contains("Paper decision: Accept (Poster)")) {
//				System.out.println("Paper decision: Accept (Poster)" + i);
				oralEnd = i - 1;
				posterStart = i;
			}
			if (lines[i].contains("Paper decision: Invite to Workshop Track")) {
//				System.out.println("Paper decision: Invite to Workshop Track" + i);
				posterEnd = i - 1;
			}
			if (lines[i].contains("Paper decision: Reject")) {
//				System.out.println("Paper decision: Reject" + i);
				rejectedStart = i;
			}
		}
		
//		for (int i = 0; i< 40; i++) {
//		System.out.println(lines[i]);
//	}		
		
		int accptOranPaper = 0;
		for (int i = oralStart; i < oralEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			accptOranPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Oral Papers: " + accptOranPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());		
		
		
		int accptPosterPaper = 0;
		for (int i = posterStart; i < posterEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			accptPosterPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Poster Papers: " + accptPosterPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());	
		
		
		int rejectedPaper = 0;
		for (int i = rejectedStart; i < rejectedEnd + 1;i++) {
//			if (accptPosterPaper > 2) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			rejectedPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}
		System.out.println("Rejected Papers: " + rejectedPaper + ", total size is "  + papers.size());
		
		return papers;
		
	}
	
	public List<Paper> extractPapers2017Workshop(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		String paperInfo = content.split("\\s{4,}")[44];
		String[] lines = paperInfo.split("><");
		//locate the target ranges
		int accptStart = 0;
		int accptEnd = 0;
		int rejectedStart = 0;
		int rejectedEnd = lines.length - 1;
		for (int i = 0; i< lines.length; i++) {
			if (lines[i].contains("Paper decision: Accept")) {
//				System.out.println("Paper decision: Accept (Oral)" + i);
				accptStart = i;
			}
			if (lines[i].contains("Paper decision: Reject")) {
//				System.out.println("Paper decision: Reject" + i);
				accptEnd = i - 1;
				rejectedStart = i;
			}
		}

		int accptPaper = 0;
		for (int i = accptStart; i < accptEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			accptPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Papers: " + accptPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());		
		
		int rejectedPaper = 0;
		for (int i = rejectedStart; i < rejectedEnd + 1;i++) {
//			if (accptPosterPaper > 2) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			rejectedPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println(entryStart + " "+entryEnd + " " + accptOralPaper);
//			System.out.println("*********************");
		}
		System.out.println("Rejected Papers: " + rejectedPaper + ", total size is "  + papers.size());
		
		return papers;
	}
	
	public List<Paper> extractPapers2016All(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		String paperInfo = content.split("\\s{4,}")[115];
		String[] lines = paperInfo.split("sectionedit");
		String workshop2 = lines[3];
		String workshop3 = lines[4];
		String conference = lines[5];
//		System.out.println(workshop2.split("<ol>|</ol>")[1].split("<li class=\"level1\">")[0]);
		String[] entries = workshop2.split("<ol>|</ol>")[1].split("<li class=\"level1\">");
		int workshop2Num = 0;
		for (String entry: entries) {
//			if (workshop2Num > 4) break;
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016workshop(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					workshop2Num++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		System.out.println("workshop2 num of papers: " + workshop2Num + " " + papers.size());
		
		int workshop3Num = 0;
		entries = workshop3.split("<ol>|</ol>")[1].split("<li class=\"level1\">");
		for (String entry: entries) {
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016workshop(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					workshop3Num++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		System.out.println("workshop3 num of papers: " + workshop3Num + " " + papers.size());
		//This is splitter of workshop and conference
		papers.add(new PaperBuilder().build());
		
		int conferenceNum = 0;
		entries = conference.split("<ol>|</ol>")[1].split("<li class=\"level1\">");
		for (String entry: entries) {
//			if (conferenceNum > 19) break;
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016conf(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					conferenceNum++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		System.out.println("conference num of papers: " + conferenceNum + " " + papers.size());
		
		return papers;
	}
	
	public List<Paper> extractPapers2015All(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		//String paperInfo = content.split("\\s{4,}")[115];
		String[] lines = content.split("sectionedit14|sectionedit15|sectionedit17|sectionedit19|sectionedit21");
		String confOral = lines[1];
		String confPoster = lines[2];
		String workshop1 = lines[3];
		String workshop2 = lines[4];
		System.out.println(confOral);
		System.out.println(confPoster);
		System.out.println(workshop1);
		System.out.println(workshop2);
		System.out.println(confOral.split("<li class=\"level1\">")[1]);
		String[] entries = confOral.split("<li class=\"level1\">");
		int confOralNum = 0;
		for (String entry : entries) {
//			if (confOralNum > 4) break;
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016conf(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					confOralNum++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		System.out.println("confOralNum of papers: " + confOralNum + " " + papers.size());
		
		int confPosterNum = 0;
		entries = confPoster.split("<tr class=\"row");
//		System.out.println(entries[0]);
//		System.out.println(entries[1]);
//		System.out.println(entries[2]);
		for (String entry: entries) {
//			if (confPosterNum > 4) break;
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016conf(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					confPosterNum++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		System.out.println("confPosterNum of papers: " + confPosterNum + " " + papers.size());
		papers.add(new PaperBuilder().build());
		
		int workshopNum = 0;
		entries = workshop1.split("<tr class=\"row");
		for (String entry: entries) {
//			if (workshopNum > 3) break;
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016conf(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					workshopNum++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		entries = workshop2.split("<tr class=\"row");
		for (String entry: entries) {
//			if (workshopNum > 3) break;
			if (!entry.isEmpty()) {
				Paper paper = getPaperBuilder2016conf(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					workshopNum++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		
		System.out.println("workshopNum of papers: " + workshopNum + " " + papers.size());
		
		return papers;
	}
	
	public List<Paper> extractPapers2014 (String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		String paras = content.split("accepted to the International Conference on Learning Representations|id=\"sites-canvas-bottom-panel\"")[1];
		System.out.println(paras.length());
		System.out.println(paras);
		String[] entries = paras.split("<br>");
		System.out.println(entries.length);
		
		int accptConfPaper = 0;
		for (String entry : entries) {
			if (accptConfPaper > 4) break;
			if (!entry.isEmpty()) {
				Paper paper = getpaperBuilder2014(entry).build();
				if (paper.getPaperID() != null && !paper.getPaperID().isEmpty()) {
					papers.add(paper);
					accptConfPaper++;
//					paper.printAllFields();
//					System.out.println("**************************");
				}
			}
		}
		System.out.println("Accepted number of papers: " + accptConfPaper + " " + papers.size());
		
		return papers;
	}
	
	public List<Paper> extractPapers2013All(String content) {
		List<Paper> papers = new ArrayList<>();
		if (content == null || content.isEmpty()) {
			System.out.println("No content in this page.");
			return papers;
		}
		
		String[] lines = content.split("\\s{4,}|><");
		//locate the target ranges
		int confOralStart = 80;
		int confOralEnd = 452;
		int confPosterStart = 453;
		int confPosterEnd = 695;
		int workshopOralStart = 696;
		int workshopOralEnd = 882;
		int workshopPosterStart = 883;
		int workshopPosterEnd = 1541;
		int rejectedStart = 1542;
		int rejectedEnd = 1859;
//		for (int i = 0; i< lines.length; i++) {
//			if (lines[i].contains("Accepted for Oral Presentation")) {
//				System.out.println("Accepted for Oral Presentation " + i);
//			}
//			if (lines[i].contains("Accepted for Poster Presentation")) {
//				System.out.println("Accepted for Poster Presentation " + i);
//			}
//			if (lines[i].contains("Not selected for presentation at this time")) {
//				System.out.println("Paper decision: Reject " + i);
//			}
//			if (lines[i].contains("type=\"text/javascript\"")) {
//				System.out.println("Paper decision: Reject End " + i);
//				break;
//			}
//		}
		int accptOralPaper = 0;
		for (int i = confOralStart; i < confOralEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			accptOralPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Conference Oral Papers: " + accptOralPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
//		papers.add(new PaperBuilder().build());	
		
		int accptPosterPaper = 0;
		for (int i = confPosterStart; i < confPosterEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			accptPosterPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}
		System.out.println("Accepted Conference Poster Papers: " + accptPosterPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());	

		int workshopOralPaper = 0;
		for (int i = workshopOralStart; i < workshopOralEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			workshopOralPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}		
		System.out.println("Accepted Workshop Oral Papers: " + workshopOralPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
//		papers.add(new PaperBuilder().build());	
		
		int workshopPosterPaper = 0;
		for (int i = workshopPosterStart; i < workshopPosterEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			workshopPosterPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}
		System.out.println("Accepted Workshop Poster Papers: " + workshopPosterPaper + ", total size is "  + papers.size());
		//insert a empty paper as a splitter.
		papers.add(new PaperBuilder().build());	
		
		int rejectedPaper = 0;
		for (int i = rejectedStart; i < rejectedEnd + 1;i++) {
//			if (accptOralPaper > 9) break;
			if (!lines[i].contains("note_content_title")) {
				continue;
			}
			Paper paper = getPaperBuilder2017(lines, i + 1).build();
			rejectedPaper++;
			papers.add(paper);
//			paper.printAllFields();
//			System.out.println("*********************");
		}
		System.out.println("Rejected Papers: " + rejectedPaper + ", total size is "  + papers.size());
		
		return papers;
	}
	
	public PaperBuilder getPaperBuilder2017(String[] lines, int titleNum) {
		PaperBuilder builder = new PaperBuilder();
		String content = lines[titleNum];
		int authorNum = titleNum;
		while(!lines[authorNum].contains("class=\"signatures\"")) {
			authorNum++;
		}
		String authorLine = lines[authorNum + 1];
		String[] segments1 = content.split("<|>|\"");
		String pageLink = new String();
		String title = new String();
		String pdfLink = new String();
		for(int i = 0; i < segments1.length; i++) {
			String str = segments1[i];
			if (str.contains("https")&& str.contains("net/forum")) {
				pageLink = str;
				title = segments1[i + 2].trim().toLowerCase();
			} else if (str.contains("https")&& str.contains("net/pdf")) {
				pdfLink = str;
			} else if (str.contains("https") && str.contains("arxiv.org")) {
				pdfLink = str.replaceFirst("abs", "pdf");
			}
		}
		String id = pageLink.split("id=")[1];
		String[] segments2 = authorLine.split(",");
		List<String> authors = new ArrayList<>();
		for (String str : segments2) {
			String[] parts = str.split("<|>");
			if (parts.length == 1) {
				authors.add(parts[0].trim());
			} else for (int i = 0; i < parts.length; i++) {
				if (parts[i].contains("/a")) {
					authors.add(parts[i - 1].trim());
					break;
				}
			}
		}
		//abstract and keywords
		String pageHtml = getPaperHtml(pageLink);
//		System.out.println(pageHtml.length());
		String paperAbstract = getAbstract(pageHtml);
		List<String> keyWords = getKeyWords(pageHtml);
		builder.setPaperID(id).setTitle(title).setYear(2017).setPdfLink(pdfLink).setPaperAbstract(paperAbstract).setAuthors(authors);
		if (keyWords == null) {
			builder.setKeyWords(new ArrayList<String>());
		} else {
			builder.setKeyWords(keyWords);
		}
		return builder;
	}
		
	public String getPaperHtml(String pageLink) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(pageLink).openConnection();
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
	
	public String getAbstract(String html) {
		String[] lines = html.split("\\s{4,}");
		String paperAbstract = new String();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("Abstract:")) {
//				System.out.println(lines[i+ 1]);
				StringBuilder abstractPart = new StringBuilder();
				abstractPart.append(lines[i + 1]);
				if (!lines[i + 1].contains("/span")) {
					int j = i + 1;
					for (j = i + 2; !lines[j].contains("/span"); j++) {
						abstractPart.append(lines[j]);
					}
					abstractPart.append(lines[j]);
				}
				String[] parts = abstractPart.toString().split("<|>");
//				System.out.println("part length is " + parts.length);
//				System.out.println("part[2] is " + parts[2]);
				if (parts.length >= 3 && !parts[2].isEmpty()) { //normal case
					paperAbstract = parts[2].trim();
				} else { //edge case
//					System.out.println("part[2] is " + parts[2]);
					StringBuilder buider = new StringBuilder();
					for (int j = i + 2; !lines[j].contains("/span"); j++) {
						buider.append(lines[j]).append(" ");
					}
					paperAbstract = buider.toString().trim();
				}
				break;
			}
		}
		return paperAbstract == null ? new String() : paperAbstract;
	}
	
	public String getAbstractArxiv(String html) {
		String[] lines = html.split("\\s{4,}");
		String paperAbstract = new String();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("Abstract:")) {
				String[] parts = lines[i].split("blockquote|/blockquote");
				for (String part : parts) {
					if (part.contains("Abstract:")){
//						System.out.println(part);
						String[] smlparts = part.split("<|>");
						paperAbstract = smlparts[smlparts.length - 1].trim();
						break;
					}
				}
			}
		}
		return paperAbstract;
	}
	
	public List<String> getKeyWords(String html) {
		String[] lines = html.split("\\s{4,}");
		List<String> keywords = new ArrayList<>();
		int start = 0;
		for (;start < lines.length; start ++) {
			if (lines[start].contains("Keywords:")) {
				break;
			}
		}
		
		if (start < lines.length) {
			if (lines[start + 1].split("<|>").length < 3) {
				return keywords;
			}
			String[] words = lines[start + 1].split("<|>")[2].split(",");
			for (String word : words) {
				keywords.add(word.trim().toLowerCase());
			}
		}
		return keywords;
	}
	
	public int findNextEntryStart(String[] lines, int start, int boundary) {
		start++;
		while (!lines[start].contains("data-id") && start < boundary) {
			start++;
		}
		return start;
	}

	public PaperBuilder getPaperBuilder(String[] lines, int start, int end) {
		PaperBuilder builder = new PaperBuilder();
		for (int i = start; i < end + 1; i++){
			if (lines[i].contains("data-id")) {
				String[] segments = lines[i].split("\"");
				for (int j = 0; j < segments.length; j++) {
					if (segments[j].contains("data-id")) {
						builder.setPaperID(segments[j + 1]);
						break;
					}
				}
			} else if (lines[i].contains("https") && lines[i].contains("pdf")) {
				String[] segments = lines[i].split("\"");
				for (int j = 0; j < segments.length; j++) {
					if (segments[j].contains("https")) {
						builder.setPdfLink(segments[j]);
						break;
					}
				}
			} else if (lines[i].contains("<h4>")) {
				if (lines[i + 2].contains("No Title")) {
					return new PaperBuilder();
				}
				builder.setTitle(lines[i + 2].trim().toLowerCase());
			} else if (lines[i].contains("Conference Blind Submission")) {
				String[] segments = lines[i].split(" ");
				for (int j = 0; j < segments.length; j++) {
					if (segments[j].contains("Conference")) {
						builder.setYear(Integer.parseInt(segments[j - 1]));
						break;
					}
				}
			} else if (lines[i].contains("Abstract:")) {
				String[] segments = lines[i + 1].split("<|>");
				builder.setPaperAbstract(segments[2].trim());
			} else if (lines[i].contains("Keywords:")) {
				String[] segments = lines[i + 1].split("<|>");
				String[] words = segments[2].split(",");
				List<String> keyWords = new ArrayList<>();
				for (String word : words){
					keyWords.add(word.trim());
				}
				builder.setKeyWords(keyWords);
			} else if (lines[i].contains("note-authors")) {
				String[] segments = lines[i + 1].split(",");
				List<String> authors = new ArrayList<>();
				for (String segment : segments) {
					String[] parts = segment.split("<|>");
					if (parts.length < 2) {
						authors.add(parts[0].trim());
					} else {
						authors.add(parts[2].trim());
					}
				}
				builder.setAuthors(authors);
			}
		}
		if (builder.getKeyWords() == null) {
			builder.setKeyWords(new ArrayList<String>());
		}
		return builder;
	}
	
	public PaperBuilder getPaperBuilder2016workshop(String entry) {
		PaperBuilder builder = new PaperBuilder();
		String[] lines = entry.split("<|>|\"");
		String id = new String();
		String pdfLink= new String();
		String paperAbstract= new String();
		String title = new String();
		List<String> authors = new ArrayList<>();
		List<String> keyWords = new ArrayList<>();
		for (String line : lines) {
			if (line.contains("http")) {
				String pageLink = line.replaceFirst("beta.","").replaceFirst("http", "https");
//				System.out.println("****" + pageLink);
				pdfLink = pageLink.replaceFirst("forum","pdf");
				String[] parts = pageLink.split("id=");
				id = parts[parts.length - 1];
				String html = getPaperHtml(pageLink);
				paperAbstract = getAbstract(html);
				keyWords = getKeyWords(html);
				break;
			}
		}
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("/a")) {
				title = lines[i - 1];
			}
			if (lines[i].contains(",")) {
				String[] substrs = lines[i].split(",");
				for (String author : substrs) {
					authors.add(author.trim());
				}
			}
		}
		builder.setYear(2016).setPaperID(id).setPdfLink(pdfLink).setPaperAbstract(paperAbstract).setKeyWords(keyWords);
		builder.setTitle(title).setAuthors(authors);
		return builder;
	}
	
	public PaperBuilder getPaperBuilder2016conf (String entry) {
		PaperBuilder builder = new PaperBuilder();
		String[] lines = entry.split("<|>|\"");
		String id = new String();
		String pdfLink= new String();
		String paperAbstract= new String();
		String title = new String();
		List<String> authors = new ArrayList<>();
		List<String> keyWords = new ArrayList<>();
		for (String line : lines) {
			if (line.contains("http")) {
				String pageLink = line.replaceFirst("http", "https");
				pdfLink = pageLink.replaceFirst("abs","pdf");
				String[] parts = pageLink.split("/");
				id = parts[parts.length - 1];
				String html = getPaperHtml(pageLink);
				paperAbstract = getAbstractArxiv(html);
				break;
			}
		}
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("/a") && !lines[i-1].contains("[code]") && !lines[i-1].contains("[video]")) {
				title = lines[i - 1];
//				break;
			}
			if (lines[i].contains(",")) {
				String[] substrs = lines[i].split(",| and");
				for (String author : substrs) {
					if (!author.trim().isEmpty()) {
						authors.add(author.trim());
					}
				}
			}
		}
		builder.setYear(2016).setPaperID(id).setPdfLink(pdfLink).setPaperAbstract(paperAbstract).setKeyWords(keyWords);
		builder.setTitle(title).setAuthors(authors);
		return builder;
	}

	public PaperBuilder getPaperBuilder2015 (String entry) {
		PaperBuilder builder = new PaperBuilder();
		String[] lines = entry.split("<|>|\"");
		String id = new String();
		String pdfLink= new String();
		String paperAbstract= new String();
		String title = new String();
		List<String> authors = new ArrayList<>();
		List<String> keyWords = new ArrayList<>();
		for (String line : lines) {
			if (line.contains("http")) {
				String pageLink = line.replaceFirst("http", "https");
				pdfLink = pageLink.replaceFirst("abs","pdf");
				String[] parts = pageLink.split("/");
				id = parts[parts.length - 1];
				String html = getPaperHtml(pageLink);
				paperAbstract = getAbstractArxiv(html);
				break;
			}
		}
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("/a") && !lines[i-1].contains("[code]") && !lines[i-1].contains("[video]")) {
				title = lines[i - 1];
//				break;
			}
			if (lines[i].contains(",")) {
				String[] substrs = lines[i].split(",");
				for (String author : substrs) {
					authors.add(author.trim());
				}
			}
		}
		builder.setYear(2016).setPaperID(id).setPdfLink(pdfLink).setPaperAbstract(paperAbstract).setKeyWords(keyWords);
		builder.setTitle(title).setAuthors(authors);
		return builder;
	}
	
	public PaperBuilder getpaperBuilder2014 (String entry) {
		PaperBuilder builder = new PaperBuilder();
		String[] lines = entry.split("<|>|\"");
		String id = new String();
		String pdfLink= new String();
		String paperAbstract= new String();
		String title = new String();
		List<String> authors = new ArrayList<>();
		List<String> keyWords = new ArrayList<>();
		for (String line : lines) {
			if (line.contains("http")) {
				String pageLink = line.replaceFirst("http", "https");
				pdfLink = pageLink.replaceFirst("abs","pdf");
				String[] parts = pageLink.split("/");
				id = parts[parts.length - 1];
				String html = getPaperHtml(pageLink);
				paperAbstract = getAbstractArxiv(html);
				break;
			}
		}
		for (int i = 2; i < lines.length - 1; i++) {
			if (lines[i].contains("/a") && !lines[i-1].contains("[code]") && !lines[i-1].contains("[video]")) {
				title = lines[i - 1];
			}
			if (lines[i -1].contains("/span") && lines[i + 1].contains("/span")) {
				String[] substrs = lines[i].split(";| and");
				for (String author : substrs) {
					if (!author.trim().isEmpty() && !author.trim().contains("&nbsp")) {
						authors.add(author.trim());
					}
				}
			} else if ((lines[i -1].contains("white-space:pre-wrap") || lines[i - 2].contains("white-space:pre-wrap")) && lines[i + 1].contains("/span")) {
				String[] substrs = lines[i].split(";| and");
				for (String author : substrs) {
					if (!author.trim().isEmpty() && !author.trim().contains("&nbsp")) {
						authors.add(author.trim());
					}
				}
			}
		}
		
		builder.setYear(2014).setPaperID(id).setPdfLink(pdfLink).setPaperAbstract(paperAbstract).setKeyWords(keyWords);
		builder.setTitle(title).setAuthors(authors);
		return builder;
	}
}
