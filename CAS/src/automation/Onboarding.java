package automation;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Onboarding implements ActionListener {
	static String email = "";
	static String Username = "";
	static String Password = "";
	static String emailid = "";
	static String fpath = "";
	static String tpath = "";
	static String adir = "";
	JFrame f;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton b4;
	JButton b5;
	JButton b6;
	JTextField t1;
	JTextField t2;
	JTextField t3;
	JLabel l1;
	JLabel l2;
	JLabel l3;
	ArrayList<String> attachments = new ArrayList<String>();
	String home = System.getProperty("user.home");
	File F = new File(home + "/config.properties");
	File temp = new File(home+"/temp.docx");
	File f1;
	static String name = "";
	static String CID = "";
	static String JTitle = "";
	static String billd = "";

	public static void main(String[] args) throws Exception {
		Onboarding m2 = new Onboarding();
		m2.gui();
	}

	public void start() throws Exception {
		File f = new File(fpath);
		FileInputStream f1 = new FileInputStream(f);
		Workbook wb = null;
		if (f.getName().endsWith("xls"))
			wb = new HSSFWorkbook(f1);
		if (f.getName().endsWith("xlsx"))
			wb = new XSSFWorkbook(f1);
		Sheet sh = wb.getSheetAt(0);
		for (Row r : sh) {
			if (!r.getCell(6).toString().matches("Sent") && r.getRowNum()!= 0) {
				name = r.getCell(1).toString();
				CID = r.getCell(2).toString();
				email = r.getCell(3).toString();
				JTitle = r.getCell(4).toString();
				billd = r.getCell(5).toString();
				set();
				attach();
				send();
				r.getCell(6).setCellValue("Sent");
			}
		}
		FileOutputStream fo = new FileOutputStream(fpath);
		wb.write(fo);
		wb.close();
		fo.close();
		f1.close();
	}

	public void send() throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.starttls.enable", "true");
		Session ses = Session.getInstance(props);
		MimeMessage mes = new MimeMessage(ses);
		MimeBodyPart m1 = new MimeBodyPart();
		m1.setContent("Hi,<br/><br/>Please refer the attached document titled 'Procedure.docx' to proceed further", "text/html");
		MimeMultipart M = new MimeMultipart();
		M.addBodyPart(m1);
		for (String s : attachments) {
			MimeBodyPart m2 = new MimeBodyPart();
			m2.attachFile(s);
			M.addBodyPart(m2);
		}
		Transport t = ses.getTransport("smtp");
		mes.setFrom(new InternetAddress(emailid));
		mes.setSubject("Amex Onboarding Documents");
		mes.setContent(M);
		mes.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email));
		try {
			t.connect("smtp.live.com", Username, Password);
			t.sendMessage(mes, mes.getAllRecipients());
			new File(adir+"/Procedure.docx").delete();
			System.out.println("Check it!");
		} catch (Exception e) {
			if (F.delete())
				gui();
		}
	}
	
	public void set() throws Exception{
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(tpath));
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            if (text != null && text.contains("%NAME%")) {
		                text = text.replace("%NAME%", name);
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains("%CID%")) {
		                text = text.replace("%CID%", CID);
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains("%JTITLE%")) {
		                text = text.replace("%JTITLE%", JTitle);
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains("%DATE%")) {
		                text = text.replace("%DATE%", billd);
		                r.setText(text, 0);
		            }
		        }
		    }
		}doc.write(new FileOutputStream(adir+"/Procedure.docx"));
		reset();
		//doc.close();
	}
	
	public void reset() throws Exception{
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(tpath));
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            if (text != null && text.contains(name)) {
		                text = text.replace(name, "%NAME%");
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains(CID)) {
		                text = text.replace(CID, "%CID%");
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains(JTitle)) {
		                text = text.replace(JTitle, "%JTITLE%");
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains(billd)) {
		                text = text.replace(billd, "%DATE%");
		                r.setText(text, 0);
		            }
		        }
		    }
		}doc.write(new FileOutputStream(temp));
		temp.delete();
		doc.close();
	}
	
	public void attach(){
		File[] f11 = f1.listFiles();
		for (File f12 : f11) {
			if (f12.isFile())
				attachments.add(f12.getPath());
		}
	}

	public void gui() throws Exception {
		if (!F.exists()) {
			f = new JFrame("Update Credentials");
			l1 = new JLabel("Username:");
			l2 = new JLabel("Password:");
			l3 = new JLabel("Email ID:");
			t1 = new JTextField(20);
			t2 = new JPasswordField(20);
			t3 = new JTextField(50);
			b4 = new JButton("Update");
			f.add(l1);
			f.add(t1);
			f.add(l2);
			f.add(t2);
			f.add(l3);
			f.add(t3);
			f.add(b4);
			b4.addActionListener(this);
			f.setVisible(true);
			f.setSize(630, 150);
			f.setLayout(new FlowLayout());
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			FileInputStream fi = new FileInputStream(F);
			Properties prop = new Properties();
			prop.load(fi);
			Username = prop.getProperty("Username");
			Password = prop.getProperty("Password");
			emailid = prop.getProperty("EmailID");
			fi.close();
			f = new JFrame("Amex Onboarding Mailer");
			b1 = new JButton("Choose File");
			b2 = new JButton("Attachments Directory");
			b3 = new JButton("Send");
			b5 = new JButton("Template");
			b6 = new JButton ("Legend");
			b1.addActionListener(this);
			b2.addActionListener(this);
			b3.addActionListener(this);
			b5.addActionListener(this);
			b6.addActionListener(this);
			f.add(b1);
			f.add(b2);
			f.add(b5);
			f.add(b3);
			f.add(b6);
			f.setVisible(true);
			f.setSize(900, 100);
			f.setLayout(new GridLayout(1, 6));
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			JFileChooser j = new JFileChooser();
			if (j.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
				File f1 = j.getSelectedFile();
				fpath = f1.getPath();
			}

		}
		if (e.getSource() == b2) {
			JFileChooser j = new JFileChooser();
			j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			j.setAcceptAllFileFilterUsed(false);
			if (j.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
				f1 = j.getSelectedFile();
				adir = f1.getPath();
			}
		}
		if (e.getSource() == b3) {
			try {
				start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == b4) {
			try {
				f.dispose();
				FileOutputStream fo = new FileOutputStream(F);
				Properties props = new Properties();
				props.setProperty("Username", t1.getText());
				props.setProperty("Password", t2.getText());
				props.setProperty("EmailID", t3.getText());
				props.store(fo, null);
				fo.close();
				gui();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==b5){
			JFileChooser j = new JFileChooser();
			if(j.showOpenDialog(f)== JFileChooser.APPROVE_OPTION)
				tpath = j.getSelectedFile().getPath();
		}
		if(e.getSource()==b6){
			JFrame f3 = new JFrame("     Template Variables");
			JLabel ll1 = new JLabel("     Name --> %NAME%");
			JLabel ll2 = new JLabel("     Contractor ID --> %CID%");
			JLabel ll3 = new JLabel("     Job Title --> %JTITLE%");
			JLabel ll4 = new JLabel("     Billing Date --> %DATE%");
			f3.add(ll1);
			f3.add(ll2);
			f3.add(ll3);
			f3.add(ll4);
			f3.setVisible(true);
			f3.setSize(300, 300);
			f3.setLayout(new GridLayout(4,1));
			f3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
}
