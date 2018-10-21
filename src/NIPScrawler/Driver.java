package NIPScrawler;

import java.io.File;
import java.util.List;

import org.json.JSONArray;

import entity.JsonConverter;
import entity.Paper;

public class Driver {

	public static void main(String[] args) {
		//Task 1: download the html pages of paperList between seleected years.
//		if (args.length < 2) {
//			System.out.println("Wrong arguments!");
//			return;
//		}
//		String start = args[0];
//		String end = args[1];
//		String[] argList = {args[0], args[1]}; 
//		CatalogCrawler.main(argList);
		
		//prepare the directories that store downloaded files.
		File resultDir = new File(FilePaths.resultDir);
		if (!resultDir.isDirectory()) {
			resultDir.mkdir();
		}
		File pdfDir = new File(FilePaths.pdfDir);
		if (!pdfDir.isDirectory()) {
			pdfDir.mkdir();
		}
		File htmlDir = new File(FilePaths.htmlDir);
		if (!htmlDir.isDirectory()) {
			htmlDir.mkdir();
		}
		File jsonDir = new File(FilePaths.jsonDir);
		if (!jsonDir.isDirectory()) {
			jsonDir.mkdir();
		}
		
		//Task 2: Extract the attributes of papers year by year
		//PDF file: download to local directory
		//Paper data: save as json files to local directory year by year
		int start = 2008;
		int end = 2017;
		int totalNum = 0;
		Extractor collector = new Extractor(FilePaths.pdfDir, FilePaths.htmlDir, FilePaths.host);
		for (int year = start; year < end + 1; year++) {
			List<Paper> papers = collector.getPaperList(year);
			totalNum += papers.size();
			System.out.print("Year: " + year + ", numPaper is " + papers.size() + "\n");
			JsonConverter converter = new JsonConverter();
			JSONArray jsonArr = converter.gatherJSONOjbByYear(papers);
			if (converter.saveAsJsonFile(jsonArr, year, FilePaths.jsonDir + "/Nips-"+String.valueOf(year)+".json")) {
				System.out.print("Year: " + year + ", succeed in writing to json file\n");
			} else {
				System.out.print("Year: " + year + ", fail in writing to json file\n");
			}
		}
		
		//print out the summary:
		int numPdfs = pdfDir.listFiles().length;
		int numHtml = htmlDir.listFiles().length;
		int numJson = jsonDir.listFiles().length;
		System.out.println("*******************************************");
		System.out.println("******************Summary******************");
		System.out.println("Range of years:       " + start + " to " + end);
		System.out.println("Total paper numbers:  " + totalNum);
		System.out.println("Number of pdf files:  " + numPdfs);
		System.out.println("Number of html files: " + numHtml);
		System.out.println("Number of json files: " + numJson);
		System.out.println("*******************************************");
		System.out.println("*******************************************");
		return;
	}

}
