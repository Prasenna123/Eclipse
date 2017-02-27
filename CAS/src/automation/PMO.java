package automation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PMO implements ActionListener {
	JFrame f;
	JButton b;
	JButton c;
	JButton d;
	static String newf = null;
	static String oldf = null;

	public static void main(String[] args) throws IOException {
		PMO x = new PMO();
		x.gui();
	}

	@SuppressWarnings("deprecation")
	public void mapping(String newf, String oldf) throws IOException {
		Workbook wb1 = null;
		Workbook wb2 = null;
		FileInputStream file1 = new FileInputStream(newf);
		FileInputStream file2 = new FileInputStream(oldf);
		if (newf.toLowerCase().endsWith("xls"))
			wb1 = new HSSFWorkbook(file1);
		else if (newf.toLowerCase().endsWith("xlsm") || newf.toLowerCase().endsWith("xlsx"))
			wb1 = new XSSFWorkbook(file1);
		if (oldf.toLowerCase().endsWith("xls"))
			wb2 = new HSSFWorkbook(file2);
		else if (oldf.toLowerCase().endsWith("xlsx"))
			wb2 = new XSSFWorkbook(file2);
		Sheet sh1 = wb1.getSheetAt(0);
		Sheet sh2 = wb2.getSheetAt(0);
		CellStyle cs = wb2.createCellStyle();
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cs.setAlignment(CellStyle.ALIGN_LEFT);
		ArrayList<String> cella = new ArrayList<String>();
		ArrayList<String> cellb = new ArrayList<String>();
		for (Row r : sh1) {
			String c = r.getCell(4).toString();
			cella.add(c);
		}
		for (Row r : sh2) {
			String c = r.getCell(4).toString();
			cellb.add(c);
		}
		for (String s : cella) {
			if (cellb.contains(s)) {
				int i = cellb.indexOf(s);
				sh2.getRow(i).getCell(11).setCellValue("Confirm");
				if (sh2.getRow(i).getCell(12) == null)
					sh2.getRow(i).createCell(12).setCellStyle(cs);
				if (sh2.getRow(i).getCell(13) == null)
					sh2.getRow(i).createCell(13).setCellStyle(cs);
			} else if (!s.matches(cella.get(0))) {
				int lr = sh2.getLastRowNum() + 1;
				Row r = sh2.createRow(lr);
				int i = cella.indexOf(s);
				for (int j = 0; j < 11; j++) {
					r.createCell(j).setCellValue(sh1.getRow(i).getCell(j).toString());
					r.createCell(12).setCellStyle(cs);
					r.createCell(13).setCellStyle(cs);
					r.getCell(j).setCellStyle(cs);
					r.createCell(11).setCellValue("Add");
					r.getCell(11).setCellStyle(cs);
				}
				r.getCell(2).setCellValue(sh1.getRow(i).getCell(2).getNumericCellValue());
				r.getCell(2).setCellStyle(cs);
				r.getCell(3).setCellValue(sh1.getRow(i).getCell(3).getNumericCellValue());
				r.getCell(3).setCellStyle(cs);
			}
		}
		for (int k = 0; k < cellb.size(); k++) {
			if (!cella.contains(cellb.get(k).toString())) {
				if (sh2.getRow(k).getCell(1).toString().matches("RIB ITT - MSO")) {
					sh2.getRow(k).createCell(11).setCellValue("Remove");
					sh2.getRow(k).getCell(11).setCellStyle(cs);
					sh2.getRow(k).createCell(12).setCellStyle(cs);
					sh2.getRow(k).createCell(13).setCellStyle(cs);
				}
			}
		}
		FileOutputStream wb = new FileOutputStream(new File(oldf));
		wb2.write(wb);
		wb.close();
		file1.close();
		file2.close();
	}

	public void gui() {
		f = new JFrame("PM Validation");
		b = new JButton("Current File");
		c = new JButton("Master File");
		d = new JButton("Compare");
		b.addActionListener(this);
		c.addActionListener(this);
		d.addActionListener(this);
		f.add(b);
		f.add(c);
		f.add(d);
		f.setSize(400, 200);
		f.setLayout(new GridLayout(1, 3));
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == b) {
			JFileChooser fc = new JFileChooser();
			if (fc.showOpenDialog(f) == JFileChooser.APPROVE_OPTION)
				oldf = fc.getSelectedFile().getPath();
		}
		if (a.getSource() == c) {
			JFileChooser fc = new JFileChooser();
			if (fc.showOpenDialog(f) == JFileChooser.APPROVE_OPTION)
				newf = fc.getSelectedFile().getPath();
		}
		if (a.getSource() == d) {
			try {
				mapping(newf, oldf);
				JOptionPane.showMessageDialog(f, "Done");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(f, "Error!");
				e.printStackTrace();
			}
		}
	}
}