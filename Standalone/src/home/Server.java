package home;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(1234);
		Socket s = server.accept();
		DataInputStream i = new DataInputStream(s.getInputStream());
		DataOutputStream o = new DataOutputStream(s.getOutputStream());
		Scanner sc = new Scanner(System.in);
		String a;
		while((a=sc.nextLine())!=""){
			o.writeUTF(a);
			System.out.println(i.readUTF());
		}
		server.close();
	}

}
