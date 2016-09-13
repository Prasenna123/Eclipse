package automation;

/**<h1> Daily Timesheet Automation <h1>
*
* This program logs into PeopleSoft HRMS and fills 8.00 hours for every working day.
* And all of it runs in the background, only displaying a GUI to retrieve credentials the first time,
* or when the password has changed.
* 
* Awesome, no?
* Of course, it is!
* 
* @authors Prasenna Venkatesh, Nagamani Yarlagadda, Ritika Sahay
* @version 1.0
*/
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
 
public class Finance implements ActionListener {
       static JFrame f;
       static JButton b;
       static JTextArea ta1;
       static JTextArea ta2;
       String UserID = "";
       String Password = "";
       String home = System.getProperty("user.home");
       File f1 = new File(home + "/config.properties");
 
       public static void main(String[] args) throws IOException,
                     InterruptedException {
              Finance fin = new Finance();
              fin.fill();
       }
 
       public void fill() throws IOException, InterruptedException {
              /**
              * This method is responsible for identifying the rows, and then fill
              * it. It begins by checking for the config.properties file in the
              * user's Home folder[C:\Users\(Username)\]. If not found, loads the 
               * default credentials specified [blank values now] and attempts to login.
               * If login fails, calls gui method to get new credentials.
              */
              if (f1.exists()) {
                     FileInputStream hi = new FileInputStream(f1); // I know, I give weird names. Deal with it!
                     Properties props = new Properties();
                     props.load(hi);
                     UserID = props.getProperty("UserID");
                     Password = props.getProperty("Password");
                     hi.close();
              }
              WebDriver driver = new HtmlUnitDriver(true);   // This throws a ton of warnings, but who cares
              driver.get("https://finance.corp.syntel.in/");//  as long as it works, right? ;)
              driver.findElement(By.name("userid")).sendKeys(UserID);// Kidding.Those are expected warnings,
              driver.findElement(By.name("pwd")).sendKeys(Password);//  nothing to panic.
              driver.findElement(By.name("Submit")).click();
              Thread.sleep(2000);
              if (driver.getTitle().matches("Employee-facing registry content")) {   //Kind of screwed if
                     WebElement tab = driver.findElement(By.className("PSLEVEL1GRID"));// they change title
                     List<WebElement> b = tab.findElements(By.tagName("tr"));             
                     for (WebElement c : b) {
                    	 if(c.findElements(By.className("PSGRIDCOLUMNHDRSORTNONE")).isEmpty()&&!(c.equals(b.get(0)))){
                    		 c.findElement(By.className("PSEDITBOX")).clear();
                             c.findElement(By.className("PSEDITBOX")).sendKeys("8.00");
                     }
                    	 }
                     driver.findElement(By.id("SY_TIME_WRK_SAVE_PB")).click();
                     driver.close();
              } else {
                     driver.close();
                     gui();
              }
       }
 
       public void update(String uname, String pwd) throws IOException,
                     InterruptedException {
              /**
              * Creates a new props file in the User's Home folder, stores the
              * new creds in it, tries to login and fill again.
              */
              Properties props1 = new Properties();
              File f2 = new File(home + "/config.properties");// This is Bill.
              FileOutputStream fo = new FileOutputStream(f2);//  Bill knows this file is important.
              props1.setProperty("UserID", uname);              //   Bill doesn't delete that file. Bill is smart.
              props1.setProperty("Password", pwd);            //    Be like Bill.
              props1.store(fo, null);
              fo.close();
              f.dispose();
              fill();
       }
 
       public void gui() {// Do you pronounce this as goo-ey or just G.U.I?
              /**
              * Displays a GUI [duh!] to get the new creds [double duh!] Also, if
              * your creds have changed, it retrieves and displays old values in the
              * GUI so that it makes it easier to update new values. I'm super-lazy,
              * if you haven't figured that out already.
              */
              f = new JFrame("Update Credentials");
              JLabel l1 = new JLabel("UserID");
              JLabel l2 = new JLabel("Password");
              if (f1.exists()) {
                     ta1 = new JTextArea(UserID, 1, 15);
                     ta2 = new JTextArea(Password, 1, 15);
              }
              ta1 = new JTextArea(1, 15);
              ta2 = new JTextArea(1, 15);
              b = new JButton("Update");
              f.add(l1);
              f.add(ta1);
              f.add(l2);
              f.add(ta2);
              f.add(b);
              b.addActionListener(this);
              f.setSize(500,100);
              f.setLayout(new FlowLayout());
              f.setVisible(true);
       }
 
       public void actionPerformed(ActionEvent e) {
              /**
              * When you click 'Update', gets the text entered and updates the
              * respective values, and finally calls the update method to
              * create/update the properties file.
              */
              if (e.getSource() == b) {
                     UserID = ta1.getText();
                     Password = ta2.getText();
                     try {
                           update(UserID, Password);
                     } catch (IOException | InterruptedException e1) {
                           e1.printStackTrace();
                     }
              }
       }
}
 