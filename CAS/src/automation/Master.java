package automation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Master implements ActionListener {
	JFrame f;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton b4;
	JButton b5;
	JButton b6;

	public static void main(String[] args) {
		Master m = new Master();
		m.gui();
	}

	public void gui(){
		f = new JFrame("CAS Automation");
		b1 = new JButton("SASA Configure");
		b2 = new JButton("Onboarding");
		b3 = new JButton("VRF");
		b4 = new JButton("PM Validation");
		b5 = new JButton("Self Cert. Configure");
		b6 = new JButton("PM Budget");
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b6.addActionListener(this);
		f.add(b1);
		f.add(b5);
		f.add(b2);
		f.add(b3);
		f.add(b4);
		f.add(b6);
		f.setVisible(true);
		f.setSize(400, 100);
		f.setLayout(new FlowLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==b1){
			try {
				Mail m1 = new Mail();
				Mail.fn=Mail.f2;
				m1.inputs();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==b5){
			try {
				Mail m2 = new Mail();
				Mail.fn=Mail.f3;
				m2.inputs();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==b2){
			try {
				Onboarding m2 = new Onboarding();
				m2.gui();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==b3){
			try {
				VRF.main(null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==b4){
			try {
				PMO.main(null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==b6){
			try {
				PMBudget.main(null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
