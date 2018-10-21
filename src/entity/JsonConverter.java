package entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonConverter {
	public JSONObject toJSONObject(Paper paper) {
		JSONObject paperObj = new JSONObject();
		try {
			paperObj.put("title",paper.getTitle());
			paperObj.put("id", paper.getPaperID());
			paperObj.put("abstract", paper.getPaperAbstract());
			paperObj.put("pdfLink", paper.getPdfLink());
			paperObj.put("year", paper.getYear());
			JSONArray arr = new JSONArray();
			List<String> authors = paper.getAuthors();
			if (authors == null) authors = new ArrayList<String>();
			for (String author : authors) {
				arr.put(author);
			}
			paperObj.put("authors", arr);
			arr = new JSONArray();
			List<String> keyWords = paper.getKeyWords();
			if (keyWords == null) keyWords = new ArrayList<String>();
			for (String word : keyWords) {
				arr.put(word);
			}
			paperObj.put("keywords", arr);
			return paperObj;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paperObj;
	}
	public JSONArray gatherJSONOjbByYear(List<Paper> papers) {
		JSONArray jsonArr = new JSONArray();
		for (Paper paper : papers) {
			JSONObject obj = toJSONObject(paper);
			jsonArr.put(obj);
		}
		return jsonArr;
	}
	public boolean saveAsJsonFile(JSONArray jsonArray, int year, String filePath) {
		if (jsonArray.toString().isEmpty()) {
			return false;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
			writer.append(jsonArray.toString());
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
