package home;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws Exception, Exception {
		Socket client = new Socket("localhost",1234);
		DataOutputStream dop = new DataOutputStream(client.getOutputStream());
		DataInputStream di = new DataInputStream(client.getInputStream());
		Scanner sc = new Scanner(System.in);
		String a;
		while(true) {
		if((a=di.readUTF())!=""){
			System.out.println(a);
			
		}
		if((a=sc.nextLine())!="") {
			dop.writeUTF(a);
		}else dop.writeUTF("");
		}
	}

}
