package automation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Mail implements ActionListener {
	JFrame f;
	JTextField ta1;
	JTextField ta2;
	JTextField ta3;
	JButton b;
	JButton b2;
	JButton b3;
	JButton b4;
	JTextField j1;
	JTextField j2;
	JTextField j3;
	JTextField j4;
	JTextArea ta4;
	static String UserID = null;
	static String Password = null;
	static String Email = null;
	static String To = null;
	static String Subject = null;
	static String Body = null;
	static String Date = "";
	static String Month = null;
	static ArrayList<String> datel = new ArrayList<String>();
	static ArrayList<File> F = new ArrayList<File>();
	static String home = System.getProperty("user.home");
	static File f1 = new File(home + "/config.properties");
	static File fn = null;
	static File f2 = new File(home + "/SASA.properties");
	static File f3 = new File(home + "/SelfCert.properties");

	public static void main(String[] args) throws Exception {
		F.add(f2);
		F.add(f3);
		if(!f1.exists()){
			Mail m = new Mail();
			m.getcreds();
		}
		else
		for (File ff : F) {
			fn = ff;
			Mail mail = new Mail();
			mail.gui();
		}
	}

	public void send() throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.starttls.enable", "true");
		Session ses = Session.getInstance(props, null);
		Message mes = new MimeMessage(ses);
		mes.setFrom(new InternetAddress(Email));
		mes.setSubject(Subject);
		mes.setRecipients(Message.RecipientType.TO, InternetAddress.parse(To));
		mes.setText(Body);
		Transport t = ses.getTransport("smtp");
		try {
			t.connect("smtp.live.com", UserID, Password);
			t.sendMessage(mes, mes.getAllRecipients());
			JOptionPane.showMessageDialog(f, "The mail with subject: "+Subject+" sent successfully!");
		} catch (Exception e) {
			if (f1.delete())
				gui();
		}
	}

	public void gui() throws Exception {
		if (!f1.exists())
			getcreds();
		else if (!fn.exists())
			inputs();
		else
			getdata();
	}
	
	public void getcreds(){
		f = new JFrame("Update Credentials");
		ta1 = new JTextField(20);
		ta2 = new JPasswordField(20);
		ta3 = new JTextField(50);
		b = new JButton("Update");
		JLabel l1 = new JLabel("Username");
		JLabel l2 = new JLabel("Password");
		JLabel l3 = new JLabel("Email ID");
		f.add(l3);
		f.add(ta3);
		f.add(l1);
		f.add(ta1);
		f.add(l2);
		f.add(ta2);
		f.add(b);
		f.setSize(680, 100);
		f.setLayout(new FlowLayout());
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		b.addActionListener(this);
	}

	public void getdata() throws Exception {
		FileInputStream fi = new FileInputStream(f1);
		Properties p = new Properties();
		p.load(fi);
		UserID = p.getProperty("Username");
		Password = p.getProperty("Password");
		Email = p.getProperty("EmailID");
		FileInputStream fi2 = new FileInputStream(fn);
		p.load(fi2);
		To = p.getProperty("To");
		Subject = p.getProperty("Subject");
		Body = p.getProperty("Body");
		Date = p.getProperty("Date");
		if(fn==f2) Month = p.getProperty("Month");
		dget();
		fi.close();
		fi2.close();
		SimpleDateFormat sd = new SimpleDateFormat("d");
		SimpleDateFormat sd2 = new SimpleDateFormat("M");
		String now = sd.format(new Date());
		String when = sd2.format(new Date());
		int when1 = Integer.parseInt(when);
		if(fn==f2){
			int Month1=Integer.parseInt(Month);
			if(when1==Month1||when1==Month1+3||when1==Month1+6||when1==Month1+9)
				if (datel.contains(now))
					send();
		}
		else if (datel.contains(now))
			send();
	}

	public void now() throws Exception {
		gui();
	}

	public void dget() {
		String a[] = Date.split(",");
		for (String s : a)
			datel.add(s);
	}

	public void inputs() {
		String Title ="";
		if(fn==f2) Title = "SASA Inputs";
		if(fn==f3) Title = "Self Cert. Inputs";
		f = new JFrame(Title);
		b2 = new JButton("Done");
		b3 = new JButton("Add Date(s)");
		b4 = new JButton("View Date(s)");
		JLabel l4 = new JLabel("To");
		JLabel l5 = new JLabel("Subject");
		JLabel l6 = new JLabel("Message");
		JLabel l7 = new JLabel("Schedule");
		JLabel l8 = new JLabel("Month");
		j1 = new JTextField(50);
		j2 = new JTextField(50);
		j3 = new JTextField(10);
		j4 = new JTextField(2);
		ta4 = new JTextArea(25,50);
		JScrollPane scroll = new JScrollPane(ta4);
		f.add(l4);
		f.add(j1);
		f.add(l5);
		f.add(j2);
		f.add(l6);
		f.add(scroll);
		f.add(l7);
		f.add(j3);
		if(fn==f2){
			f.add(l8);
			f.add(j4);
		}
		f.add(b3);
		f.add(b4);
		f.add(b2);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		f.setSize(625, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLayout(new FlowLayout());
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b) {
			Properties prop = new Properties();
			prop.setProperty("Username", ta1.getText());
			prop.setProperty("Password", ta2.getText());
			prop.setProperty("EmailID", ta3.getText());
			f.dispose();
			try {
				FileOutputStream fo = new FileOutputStream(f1);
				prop.store(fo, null);
				fo.close();
				gui();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == b2) {
			Properties prop = new Properties();
			prop.setProperty("To", j1.getText());
			prop.setProperty("Subject", j2.getText());
			prop.setProperty("Body", ta4.getText());
			prop.setProperty("Date", Date);
			if(fn==f2) prop.setProperty("Month", j4.getText());
			f.dispose();
			try {
				FileOutputStream fo = new FileOutputStream(fn);
				prop.store(fo, null);
				fo.close();
				Date = "";
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == b3) {
			JOptionPane.showMessageDialog(f, "Added:" + " " + j3.getText());
			Date = Date.concat(j3.getText() + ",");
		}
		if (e.getSource() == b4) {
			if (fn.exists()) {
				try {
					FileInputStream fii = new FileInputStream(fn);
					Properties propo = new Properties();
					propo.load(fii);
					String dates = propo.getProperty("Date");
					JOptionPane.showMessageDialog(f, "Old Date(s):"+dates+" New Date(s):"+Date);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(f, Date);
		}

	}
}