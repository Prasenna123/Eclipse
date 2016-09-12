package automation;

import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class PMBudget implements ActionListener {
	JFrame f;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton b4;
	JTextField t1;
	JTextField t2;
	JTextField p1;
	static String home = System.getProperty("user.home");
	File F = new File(home + "/clarity.properties");
	static WebDriver driver;
	static Robot rb;
	static String Username = "";
	static String Password = "";
	static String path = "";
	static String iepath = "";
	static String Pno = "";
	static String afixed = "";
	static String avariable = "";
	static String complete = "";
	static String date = "";

	public static void main(String[] args) throws Exception {
		PMBudget pme = new PMBudget();
		pme.gui();
	}
	
	public void start() throws Exception{
		System.setProperty("webdriver.ie.driver", iepath);
		driver = new InternetExplorerDriver();
		driver.get("https://pmt.tims.intra.aexp.com/niku/nu");
		driver.navigate().to("javascript:document.getElementById('overridelink').click()");
		rb = new Robot();
		type(Username);
		Thread.sleep(1000);
		rb.keyPress(KeyEvent.VK_TAB);
		type(Password);
		rb.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(5000);
		if(!driver.getTitle().matches("Intranet"))
		excel2();
		else {F.delete();gui();}
	}

	public static void clarity() throws Exception {
		Thread.sleep(10000);
		driver.get("https://pmt.tims.intra.aexp.com/niku/nu#action:mainnav.work&classCode=project");
		Thread.sleep(10000);
		driver.findElement(By.name("unique_code")).clear();
		driver.findElement(By.name("unique_code")).sendKeys(Pno);
		Thread.sleep(2000);
		driver.findElement(By.name("manager_id_text")).clear();
		Thread.sleep(2000);
		driver.findElement(By.name("filter")).click();
		Thread.sleep(5000);
		List<WebElement> web = driver.findElements(By.id("projmgr.projectDefaultTab"));
		if (!web.isEmpty() && web.size() < 2) {
			driver.findElement(By.id("projmgr.projectDefaultTab")).click();
			Thread.sleep(5000);
			driver.findElement(By.linkText("Financial Plans")).click();
			Thread.sleep(5000);
			List<WebElement> b = driver.findElements(By.tagName("td"));
			List<WebElement> d = new ArrayList<WebElement>();
			for (WebElement c : b) {
				if ((c.getAttribute("class").matches("tableContent")))
					d.add(c);
			}
			for (WebElement e : d) {
				if (e.getAttribute("column").matches("8"))
					afixed = e.getText();
				else if (e.getAttribute("column").matches("11"))
					avariable = e.getText();
			}
			if (afixed.matches("0.00") && avariable.matches("0.00")) {
				driver.get(
						"https://pmt.tims.intra.aexp.com/niku/nu#action:projmgr.projectProperties&odf_view=projectCreate.subObjList.aet_sub_proj_fund&id=7334442&odf_pk=7334442&parentObjectCode=project&odf_concrete_parent_object_code=project&odf_parent_id=7334442&odf_cncrt_parent_id=7334442");
				Thread.sleep(5000);
				List<WebElement> wb1 = driver.findElements(By.tagName("td"));
				List<WebElement> wb2 = new ArrayList<WebElement>();
				for (WebElement wb3 : wb1) {
					if ((wb3.getAttribute("class").matches("tableContent")))
						wb2.add(wb3);
					for (WebElement wb4 : wb2) {
						if (wb4.getAttribute("column").matches("2"))
							afixed = wb4.getText();
						else if (wb4.getAttribute("column").matches("3"))
							avariable = wb4.getText();
					}
				}
			}
		}
	}

	static void type(String s) throws AWTException {

		StringSelection stringSelection = new StringSelection(s);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
		rb.keyPress(KeyEvent.VK_CONTROL);
		rb.keyPress(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void excel() throws Exception {
		FileInputStream file = new FileInputStream(new File(path));
		Workbook wb = null;
		if (path.toLowerCase().endsWith(".xls")) {
			wb = new HSSFWorkbook(file);
		} else if (path.toLowerCase().endsWith(".xlsx")) {
			wb = new XSSFWorkbook(file);
		}
		Sheet sh = wb.getSheetAt(1);
		for (Row r : sh) {
			if (r.getCell(1).toString().matches("RIB ITT - MSO")) {
				Pno = r.getCell(6).toString();
				clarity();
				if (afixed != "" && avariable != "") {
					double af1 = Double.valueOf(afixed);
					double av1 = Double.valueOf(avariable);
					r.getCell(19).setCellValue(af1);
					r.getCell(20).setCellValue(av1);
				} else {
					r.getCell(19).setCellValue(afixed);
					r.getCell(20).setCellValue(avariable);
				}
				afixed = "";
				avariable = "";
			}
		}
		FileOutputStream fo = new FileOutputStream(path);
		wb.write(fo);
		fo.close();
		wb.close();
		file.close();
	}

	public static void PMScheduling() throws Exception {
		Thread.sleep(10000);
		driver.get("https://pmt.tims.intra.aexp.com/niku/nu#action:mainnav.work&classCode=project");
		Thread.sleep(10000);
		driver.findElement(By.name("unique_code")).clear();
		Thread.sleep(2000);
		driver.findElement(By.name("unique_code")).sendKeys(Pno);
		Thread.sleep(2000);
		driver.findElement(By.name("manager_id_text")).clear();
		Thread.sleep(2000);
		driver.findElement(By.name("filter")).click();
		List<WebElement> web1 = driver.findElements(By.tagName("td"));
		List<WebElement> web2 = new ArrayList<WebElement>();
		for (WebElement c : web1) {
			if (c.getAttribute("class").matches("tableContent"))
				web2.add(c);
		}
		for (WebElement e : web2) {
			if (e.getAttribute("title").matches("Complete"))
				complete = e.getText();
			System.out.println(complete);
			if (e.getAttribute("column").matches("8"))
				date = e.getText();
			System.out.println(date);
		}
	}

	public void excel2() throws Exception {
		FileInputStream file = new FileInputStream(new File(path));
		Workbook wb = null;
		if (path.toLowerCase().endsWith(".xls"))
			wb = new HSSFWorkbook(file);
		else if (path.toLowerCase().endsWith(".xlsx"))
			wb = new XSSFWorkbook(file);
		Sheet sh = wb.getSheetAt(0);
		Sheet sh2 = wb.getSheetAt(1);
		CellStyle cs = wb.createCellStyle();
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);
		for (Row r : sh) {
			if (r.getCell(1).toString().matches("RIB ITT - MSO")) {
				Pno = r.getCell(6).toString();
				PMScheduling();
				if (r.getCell(1).toString().matches("RIB ITT - MSO") && complete.matches("Complete")) {
					int lr = sh2.getLastRowNum() + 1;
					Row l = sh2.createRow(lr);
					for (int i = 0; i < 10; i++) {
						l.createCell(i).setCellValue(sh.getRow(r.getRowNum()).getCell(i).toString());
						l.getCell(i).setCellStyle(cs);
					}
					l.createCell(11).setCellValue(date);
					l.getCell(11).setCellStyle(cs);
					l.createCell(12).setCellValue(date);
					l.getCell(12).setCellStyle(cs);
					l.createCell(24).setCellValue("test support catogory-1");
					l.getCell(24).setCellStyle(cs);
					l.createCell(25).setCellValue("no");
					l.getCell(25).setCellStyle(cs);
					l.createCell(26).setCellValue("billing");
					l.getCell(26).setCellStyle(cs);
					l.createCell(27)
							.setCellValue("Test Support Category-2 Project will not have control on implementation");
					r.getCell(27).setCellStyle(cs);
				}
			}
		}

		FileOutputStream fo = new FileOutputStream(path);
		wb.write(fo);
		wb.close();
		fo.close();
		file.close();
		excel();
	}

	public void gui() {
		if (!F.exists()) {
			f = new JFrame("Credentials");
			JLabel l1 = new JLabel("Username with domain:");
			JLabel l2 = new JLabel("Password");
			t1 = new JTextField(20);
			p1 = new JPasswordField(20);
			b1 = new JButton("Update");
			b1.addActionListener(this);
			f.add(l1);
			f.add(t1);
			f.add(l2);
			f.add(p1);
			f.add(b1);
			f.setSize(400, 100);
			f.setLayout(new FlowLayout());
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			f = new JFrame("PM Budget");
			b2 = new JButton("Choose Excel File");
			b3 = new JButton("Execute");
			b4 = new JButton("IEDriverserver");
			b2.addActionListener(this);
			b3.addActionListener(this);
			b4.addActionListener(this);
			f.add(b2);
			f.add(b4);
			f.add(b3);
			f.setSize(500, 100);
			f.setLayout(new GridLayout(1,3));
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			Properties prop = new Properties();
			prop.setProperty("Username", t1.getText());
			prop.setProperty("Password", p1.getText());
			try {
				FileOutputStream fo = new FileOutputStream(F);
				prop.store(fo, null);
				fo.close();
				f.dispose();
				gui();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == b2) {
			JFileChooser j = new JFileChooser();
			if (j.showOpenDialog(f) == JFileChooser.APPROVE_OPTION)
				path = j.getSelectedFile().getPath();
		}
		if (e.getSource() == b3) {
			try {
				FileInputStream fi = new FileInputStream(F);
				Properties prop = new Properties();
				prop.load(fi);
				Username = prop.getProperty("Username");
				Password = prop.getProperty("Password");
				fi.close();
				start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == b4) {
			JFileChooser j = new JFileChooser();
			if (j.showOpenDialog(f) == JFileChooser.APPROVE_OPTION)
				iepath = j.getSelectedFile().getPath();
		}
	}
}
