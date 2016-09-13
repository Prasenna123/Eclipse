package home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Equip {
	static String home =System.getProperty("user.home");
	static String body = "";

	public static void wrapper(String tag, String word) {
		if (tag.matches("header"))
			body += "<th>" + word + "</th>";
		else if (tag.matches("data"))
			body += "<td>" + word + "</td>";
		else if (tag.matches("rend"))
			body = "<tr>" + body + "</tr>";
	}

	public static void mapping() throws IOException {
		ArrayList<String> tr = new ArrayList<String>();
		String path = "C:/Users/prase/Downloads/ATS.xls";
		String lname = "";
		String lid = "";
		FileInputStream file = new FileInputStream(new File(path));
		Workbook wb = null;
		if (path.toLowerCase().endsWith(".xls")) {
			wb = new HSSFWorkbook(file);
		} else if (path.toLowerCase().endsWith(".xlsx")) {
			wb = new XSSFWorkbook(file);
		}
		Sheet sh = wb.getSheetAt(0);
		for (Row r : sh) {
			if (r.getRowNum() == 2) {
				for (int j = 3; j < 12; j++)
					if (j != 5 && j != 6 && j != 10)
						wrapper("header", r.getCell(j).toString());
				wrapper("rend", null);
			} else if (r.getRowNum() > 2 && r.getRowNum() <= sh.getLastRowNum()) {
				for (int i = 3; i < 12; i++) {
					if (r.getCell(i) != null && !r.getCell(i).toString().matches("")) {
						if (i == 3) {
							lid = r.getCell(i).toString();
							wrapper("data", r.getCell(i).toString());
						} else if (i == 4) {
							lname = r.getCell(i).toString();
							wrapper("data", r.getCell(i).toString());
						} else if (i != 5 && i != 6 && i != 10)
							wrapper("data", r.getCell(i).toString());
					} else if (i == 3)
						wrapper("data", lid);
					else if (i == 4)
						wrapper("data", lname);
				}
				wrapper("rend", null);
				tr.add(body);
				body = "";
			}
		}
		for (String t : tr)
			body += t;
		body = "<table>" + body + "</table>";
	}

	public static void send() throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.starttls.enable", "true");
		Session ses = Session.getInstance(props, null);
		Message mes = new MimeMessage(ses);
		mes.setFrom(new InternetAddress("prasennavenkatesh@live.com"));
		mes.setSubject("Sup");
		mes.setRecipients(Message.RecipientType.TO, InternetAddress.parse("prasennavenkatesh@gmail.com"));
		mes.setContent("<style>table, th, td {border: 1px solid black;border-collapse: collapse;}</style>" + body,
				"text/html");
		Transport t = ses.getTransport("smtp");
		try {
			t.connect("smtp.live.com", "prasennavenkatesh@live.com", "Nobelprize123!@#");
			t.sendMessage(mes, mes.getAllRecipients());
		} catch (Exception e) {
			// if (f1.delete())
			// gui();
		}
	}

	public static void main(String[] args) throws Exception {
		mapping();
		send();
		System.out.println(body);
	}
}
