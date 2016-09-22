package home;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fusionchart {

	public static void main(String[] args) throws IOException, JSONException {
		String path = "D:/Downloads/Untitled_Message/tr05.lst";
		String fname = new File(path).getName();
		System.out.println(path.replace(fname, "tr06.lst"));
		BufferedReader br = new BufferedReader(new FileReader(path));
		Pattern p = Pattern.compile("ENTRC\\s\\w\\w\\w\\w");
		FileWriter dawg = new FileWriter("D:/Miscellaneous/Java/Sup/WebContent/flare.json");
		JSONArray ja = search(p, br);
		JSONObject head = new JSONObject();
		head.put("name", fname);
		head.put("children", ja);
		System.out.println(head);
		dawg.write(head.toString());
		br.close();
		dawg.close();

	}

	static JSONArray search(Pattern p, BufferedReader br) throws JSONException, IOException {
		String line;
		int linecount = 0;
		int currentline = 0;
		JSONArray ja = new JSONArray();
		while ((line = br.readLine()) != null) {
			linecount++;
			Matcher m = p.matcher(line);
			while (m.find() && currentline != linecount) {
				currentline = linecount;
				JSONObject yo = new JSONObject();
				yo.put("name", line.substring(m.start() + 6, m.end()));
				ja.put(yo);
				System.out.println(yo.getString("name"));
			}
		}
		return ja;
	}
}