package NIPScrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatalogCrawler {

	public static void main(String[] args) {
		String outputDir = args[0];
		int start = Integer.parseInt(args[1]);
		int end = Integer.parseInt(args[2]);
		
		for (int i = start; i <= end; i++) {
			System.out.println("Trying year " + i);
			if (queryCatalogByYear(i, outputDir)) {
				System.out.println("Year " + i + " success.");
			} else {
				System.out.println("Year " + i + " fail.");
			}
		}
		System.out.println("Download Finish.\nFiles in Directory: " + outputDir);
	}

	public static boolean queryCatalogByYear(int year, String outputDir) {
		String url = "https://papers.nips.cc/book/advances-in-neural-information-processing-systems-" + String.valueOf(year);
		System.out.println(url);
		try {
			//connecting to Nips and query webpage.
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000); //set timeout to 5 seconds
			int responseCode = connection.getResponseCode();
			System.out.println("Sending request to URL: " + url);
			System.out.println("Response code is " + responseCode);
			if (responseCode != 200) {
				return false;
			}
			
			//Record response into local StringBuilder
			System.out.println("Extracting web content.");
			StringBuilder response = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inLine = new String();
			while ((inLine = in.readLine()) != null) {
				response.append(inLine);
			}
			in.close();
			
			//Output response into local directory
			String fileName = "NipsListPage-" + String.valueOf(year) + ".txt";
			System.out.println("Writing to local file:" + outputDir + "/" + fileName + ".");
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + "/" + fileName, false));
			writer.append(response);
			writer.close();
			System.out.println("Writing success\nFinished");
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			System.out.println("Waiting time > 5s.");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
