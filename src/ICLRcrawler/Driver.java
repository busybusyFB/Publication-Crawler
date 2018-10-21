package ICLRcrawler;

import java.io.File;
import java.util.List;

import org.json.JSONArray;

import entity.JsonConverter;
import entity.Paper;

public class Driver {
	public static void main(String[] args) {
		//prepare the directories that store downloaded files.
		File outputDir = new File(FilePaths.outputDir);
		if (!outputDir.isDirectory()) {
			outputDir.mkdir();
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
		//convert html of paper list into txt
		Preprocessor converter = new Preprocessor();
		converter.convertHtmlToText(FilePaths.htmlDir);
		
		Driver driver = new Driver();
		driver.process2018();
		driver.process2017();
		driver.process2016();
		driver.process2015();
		driver.process2014();
		driver.process2013();
	} 
	public void process2018() {

		
		//step 1: extract conference informations.
		Extractor crawler = new Extractor();
		String filePath = FilePaths.htmlDir + "/ICLR2018Conference.txt";
		List<Paper> papersConference = crawler.extractPapers2018Conference(crawler.readTxt(filePath));
		System.out.println("Total size of papers is :" + papersConference.size());
		
		//step 2: convert to json and save to json files
		JsonConverter converterJson  = new JsonConverter();
		JSONArray jsonArr = converterJson.gatherJSONOjbByYear(papersConference);
		String jsonPath = FilePaths.jsonDir + "/Iclr-2018Conference.json";
		if (converterJson.saveAsJsonFile(jsonArr, 2018, jsonPath)) {
			System.out.print("Year: " + 2018 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2018 + ", fail in writing to json file\n");
		}
		
		//step 3: extract workshop papers
		Extractor crawler2 = new Extractor();
		String filePath2 = FilePaths.htmlDir + "/ICLR2018Workshop.txt";
		List<Paper> papersWorkShop = crawler2.extractPapers2018Workshop(crawler2.readTxt(filePath2));
		System.out.println("Total size of papers is :" + papersWorkShop.size());
		
		//step 4: convert to json and save as json files
		JsonConverter converterJson2  = new JsonConverter();
		JSONArray jsonArr2 = converterJson2.gatherJSONOjbByYear(papersWorkShop);
		String jsonPath2 = FilePaths.jsonDir + "/Iclr-2018Workshop.json";
		if (converterJson2.saveAsJsonFile(jsonArr2, 2018, jsonPath2)) {
			System.out.print("Year: " + 2018 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2018 + ", fail in writing to json file\n");
		}
		return;
	}

	public void process2017() {
		//step 1: conference infor extraction
		Extractor crawler = new Extractor();
		String filePath = FilePaths.htmlDir + "/ICLR2017Conference.txt";
		List<Paper> papersConference = crawler.extractPapers2017Conference(crawler.readTxt(filePath));
		System.out.println("Total size of papers is :" + papersConference.size());
		
		//step 2: convert to json and save to json files
		JsonConverter converterJson  = new JsonConverter();
		JSONArray jsonArr = converterJson.gatherJSONOjbByYear(papersConference);
		String jsonPath = FilePaths.jsonDir + "/Iclr-2017Conference.json";
		if (converterJson.saveAsJsonFile(jsonArr, 2017, jsonPath)) {
			System.out.print("Year: " + 2017 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2017 + ", fail in writing to json file\n");
		}
		
		//step 3: workshop information extraction
		Extractor crawler2 = new Extractor();
		String filePath2 = FilePaths.htmlDir + "/ICLR2017Workshop.txt";
		List<Paper> papersWorkshop = crawler2.extractPapers2017Workshop(crawler2.readTxt(filePath2));
		System.out.println("Total size of papers is :" + papersWorkshop.size());
		
		//step 4: convert and save as json file
		JsonConverter converterJson2  = new JsonConverter();
		JSONArray jsonArr2 = converterJson2.gatherJSONOjbByYear(papersWorkshop);
		String jsonPath2 = FilePaths.jsonDir + "/Iclr-2017Workshop.json";
		if (converterJson2.saveAsJsonFile(jsonArr2, 2017, jsonPath2)) {
			System.out.print("Year: " + 2017 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2017 + ", fail in writing to json file\n");
		}
	}
	
	public void process2016() {
		//step 1: paper infor extraction
		Extractor crawler = new Extractor();
		String filePath = FilePaths.htmlDir + "/ICLR2016AllAccepted.txt";
		List<Paper> papersConference = crawler.extractPapers2016All(crawler.readTxt(filePath));
		System.out.println("Total size of papers is :" + papersConference.size());
		
		//step 2: convert to json and save to json files
		JsonConverter converterJson  = new JsonConverter();
		JSONArray jsonArr = converterJson.gatherJSONOjbByYear(papersConference);
		String jsonPath = FilePaths.jsonDir + "/Iclr-2016AllAccepted.json";
		if (converterJson.saveAsJsonFile(jsonArr, 2016, jsonPath)) {
			System.out.print("Year: " + 2016 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2016 + ", fail in writing to json file\n");
		}
	}

	public void process2015() {
		//step 1: paper infor extraction
		Extractor crawler = new Extractor();
		String filePath = FilePaths.htmlDir + "/ICLR2015AllAccepted.txt";
		List<Paper> papersConference = crawler.extractPapers2015All(crawler.readTxt(filePath));
		System.out.println("Total size of papers is :" + papersConference.size());
		
//		step 2: convert to json and save to json files
		JsonConverter converterJson  = new JsonConverter();
		JSONArray jsonArr = converterJson.gatherJSONOjbByYear(papersConference);
		String jsonPath = FilePaths.jsonDir + "/Iclr-2015AllAccepted.json";
		if (converterJson.saveAsJsonFile(jsonArr, 2015, jsonPath)) {
			System.out.print("Year: " + 2015 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2015 + ", fail in writing to json file\n");
		}
	}

	public void process2014() {
		int year = 2014;
		//step 1: conference infor extraction
		Extractor crawler = new Extractor();
		String filePath = FilePaths.htmlDir + "/ICLR2014ConferenceAccepted.txt";
		List<Paper> papersConference = crawler.extractPapers2014(crawler.readTxt(filePath));
		System.out.println("Total size of papers is :" + papersConference.size());
		
		//step 2: convert to json and save to json files
		JsonConverter converterJson  = new JsonConverter();
		JSONArray jsonArr = converterJson.gatherJSONOjbByYear(papersConference);
		String jsonPath = FilePaths.jsonDir + "/Iclr-2014ConferenceAccepted.json";
		if (converterJson.saveAsJsonFile(jsonArr, year, jsonPath)) {
			System.out.print("Year: " + year + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + year + ", fail in writing to json file\n");
		}
		
		//step 3: workshop information extraction
		Extractor crawler2 = new Extractor();
		String filePath2 = FilePaths.htmlDir + "/ICLR2014WorkshopAccepted.txt";
		List<Paper> papersWorkshop = crawler2.extractPapers2014(crawler2.readTxt(filePath2));
		System.out.println("Total size of papers is :" + papersWorkshop.size());
		
		//step 4: convert and save as json file
		JsonConverter converterJson2  = new JsonConverter();
		JSONArray jsonArr2 = converterJson2.gatherJSONOjbByYear(papersWorkshop);
		String jsonPath2 = FilePaths.jsonDir + "/Iclr-2014WorkshopAccpeted.json";
		if (converterJson2.saveAsJsonFile(jsonArr2, year, jsonPath2)) {
			System.out.print("Year: " + year + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + year + ", fail in writing to json file\n");
		}
		
	}
	
	public void process2013() {
		//refer 2017
//		//step 1: conference infor extraction
		Extractor crawler = new Extractor();
		String filePath = FilePaths.htmlDir + "/ICLR2013All.txt";
		List<Paper> papersConference = crawler.extractPapers2013All(crawler.readTxt(filePath));
		System.out.println("Total size of papers is :" + papersConference.size());
		
		//step 2: convert to json and save to json files
		JsonConverter converterJson  = new JsonConverter();
		JSONArray jsonArr = converterJson.gatherJSONOjbByYear(papersConference);
		String jsonPath = FilePaths.jsonDir + "/Iclr-2013All.json";
		if (converterJson.saveAsJsonFile(jsonArr, 2013, jsonPath)) {
			System.out.print("Year: " + 2013 + ", succeed in writing to json file\n");
		} else {
			System.out.print("Year: " + 2013 + ", fail in writing to json file\n");
		}
	}
}
