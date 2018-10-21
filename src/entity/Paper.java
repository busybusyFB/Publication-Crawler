package entity;

import java.util.List;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

public class Paper {
	
	//fields
	private int year;
	private String paperID;
	private String title;
	private String paperAbstract;
	private String pdfLink;
	private List<String> authors;
	private List<String> keyWords;
	//constructor
	private Paper(PaperBuilder builder) {
		this.year = builder.year;
		this.paperID = builder.paperID;
		this.title = builder.title;
		this.paperAbstract = builder.paperAbstract;
		this.pdfLink = builder.pdfLink;
		this.authors = builder.authors;
		this.keyWords = builder.keyWords;
	}
	//getters
	public int getYear() {
		return year;
	}
	public String getPaperID() {
		return paperID;
	}
	public String getTitle() {
		return title;
	}
	public String getPaperAbstract() {
		return paperAbstract;
	}
	public String getPdfLink() {
		return pdfLink;
	}
	public List<String> getAuthors() {
		return authors;
	}
	public List<String> getKeyWords() {
		return keyWords;
	}
	
	//PaperBuilder
	public static class PaperBuilder {
		private int year;
		private String paperID;
		private String title;
		private String paperAbstract;
		private String pdfLink;
		private List<String> authors;
		private List<String> keyWords;
		public PaperBuilder setYear(int year) {
			this.year = year;
			return this;
		}
		public PaperBuilder setPaperID(String paperID) {
			this.paperID = paperID;
			return this;
		}
		public PaperBuilder setTitle(String title) {
			this.title = title;
			return this;
		}
		public PaperBuilder setPaperAbstract(String paperAbstract) {
			this.paperAbstract = paperAbstract;
			return this;
		}
		public PaperBuilder setPdfLink(String pdfLink) {
			this.pdfLink = pdfLink;
			return this;
		}
		public PaperBuilder setAuthors(List<String> authors) {
			this.authors = authors;
			return this;
		}
		public PaperBuilder setKeyWords(List<String> keyWords) {
			this.keyWords = keyWords;
			return this;
		}
		
		public List<String> getKeyWords() {
			return this.keyWords;
		}
		public Paper build() {
			return new Paper(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + paperID.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paper other = (Paper) obj;
		if (paperID != other.paperID)
			return false;
		return true;
	}

//	public JSONObject toJSONObject() {
//		JSONObject obj = new JSONObject();
//		try {
//			obj.put("title", title);
//			obj.put("year", year);
//			obj.put("paperID", paperID);
//			obj.put("abstract", paperAbstract);
//			obj.put("pdfLink",pdfLink);
//			obj.put("authors", new JSONArray(authors));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return obj;
//	}
	
	public void printAllFields() {
		System.out.println("title: " + title);
		System.out.println("year: " + year);
		System.out.println("paperID: " + paperID);
		System.out.println("abstract: " + paperAbstract);
		System.out.println("pdfLink: " + pdfLink);
		System.out.println("authors: ");
		for (String author: authors) {
			System.out.println(author + " ");
		}
		System.out.println("key words: ");
		for (String word: keyWords) {
			System.out.println(word + " ");
		}
	}
}
