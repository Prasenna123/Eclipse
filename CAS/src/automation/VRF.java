package automation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VRF implements ActionListener {
	JFrame f;
	JButton b1;
	JButton b2;
	JButton b3;
	static String path = null;
	static String npath = null;
	static int rn=1;

	public static void main(String[] args) throws IOException {
		VRF a = new VRF();
		a.gui();
	}

	public void mapping() throws IOException {
		FileInputStream file = new FileInputStream(new File(path));
		Workbook wb = null;
		String ext = ".xlsx";
		if (path.toLowerCase().endsWith(".xls")) {
			wb = new HSSFWorkbook(file);
			ext = ".xls";
		} else if (path.toLowerCase().endsWith(".xlsx")) {
			wb = new XSSFWorkbook(file);
			ext = ".xlsx";
		}
		Sheet sh = wb.getSheetAt(1);
		Sheet nsh = wb.getSheetAt(0);
		short cn = sh.getRow(sh.getLastRowNum()).getLastCellNum();
		if(rn<=sh.getLastRowNum()){
		while(!sh.getRow(rn).getCell(12).toString().equalsIgnoreCase("Yes"))
			if(rn!=sh.getLastRowNum())
			rn++;
			else {JOptionPane.showMessageDialog(f, "Done");System.exit(0);}
		String[] lol = new String[15];
		sh.getRow(rn).getCell(12).setCellValue("No");
		FileOutputStream f2 = new FileOutputStream(new File(path));
		wb.write(f2);
		for (int i = 0; i < cn; i++)
			if(sh.getRow(rn).getCell(i)!=null)
			lol[i] = sh.getRow(rn).getCell(i).toString();
			else lol[i]="N/A";
		nsh.getRow(6).getCell(1).setCellValue(lol[1]);
		nsh.getRow(6).getCell(5).setCellValue(lol[2]);
		nsh.getRow(9).getCell(1).setCellValue(lol[3]);
		nsh.getRow(10).getCell(1).setCellValue(lol[4]);
		nsh.getRow(35).getCell(5).setCellValue(lol[5]);
		nsh.getRow(8).getCell(1).setCellValue(lol[6]);
		nsh.getRow(8).getCell(5).setCellValue(lol[9]);
		nsh.getRow(17).getCell(5).setCellValue(lol[8]);
		nsh.getRow(13).getCell(0).setCellValue(lol[7]);
		CreationHelper createHelper = wb.getCreationHelper();
		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_EMAIL);
	    link.setAddress("mailto:"+lol[7]);
	    nsh.getRow(13).getCell(0).setHyperlink(link);
		nsh.getRow(37).getCell(1).setCellValue(lol[11]);
		if(lol[9].startsWith("Change")||lol[9].startsWith("Terminate")){
			nsh.getRow(16).getCell(1).setCellValue("VarBil");
		}
		else
			if(lol[9].matches("New Resource")){
				nsh.getRow(37).getCell(4).setCellValue(lol[10]);
			}
		FileOutputStream out = new FileOutputStream(
				new File(npath + "\\VRF" + " " + lol[9] + " - " + lol[1] + " " + lol[2] + ext));
		wb.removeSheetAt(1);
		wb.write(out);
		wb.close();
		out.close();
		System.out.println("Excel written successfully..");
		file.close();
		mapping();
		}
	}

	public void gui() {
		f = new JFrame("VRF");
		b1 = new JButton("Choose File");
		b2 = new JButton("Execute");
		b3 = new JButton("Save to");
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		f.add(b1);
		f.add(b3);
		f.add(b2);
		f.setSize(800, 100);
		f.setVisible(true);
		f.setLayout(new GridLayout(1, 3));
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			JFileChooser j = new JFileChooser();
			if (j.showOpenDialog(f) == JFileChooser.APPROVE_OPTION)
				path = j.getSelectedFile().getPath();
		}
		if (e.getSource() == b3) {
			JFileChooser j = new JFileChooser();
			j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			j.setAcceptAllFileFilterUsed(false);
			j.setCurrentDirectory(new java.io.File("."));
			if (j.showOpenDialog(f) == JFileChooser.APPROVE_OPTION)
				npath = j.getSelectedFile().getPath();
		}
		if (e.getSource() == b2) {
			try {
				mapping();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(f, "Error!");
				e1.printStackTrace();
			}
		}
	}
}