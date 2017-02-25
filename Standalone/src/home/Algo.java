package home;


import java.util.Scanner;

public class Algo {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String a = s.nextLine();
		s.close();
		String b = "";
		char c = '0';
		String d = "";
		int ans = 0;
		int gl = 0, k = 0, grl=0, e=0;
		for (int l = 0; l < a.length(); l++)
			if (Character.isDigit(a.charAt(l))) {
				b = b + a.charAt(l);
			} else {
				c = a.charAt(l);
				d = a.substring(l + 1);
				break;
			}
		int bl = b.length();
		int dl = d.length() + 1;
		if (c == '+')
			ans = Integer.parseInt(b) + Integer.parseInt(d);
		else if (c == '-')
			ans = Integer.parseInt(b) - Integer.parseInt(d);
		else if (c == '*')
			ans = Integer.parseInt(b) * Integer.parseInt(d);
		int al = Integer.toString(ans).length();

		if(al>=dl && al>=bl)
			grl=al;
		else if(bl>=al && bl>=dl)
			grl=bl;
		else grl = dl;
		
		if (bl > dl) {
			gl = bl - dl;
			System.out.println(b);
			for (int i = 0; i < gl; i++)
				System.out.print(" ");
			System.out.println(c + d);
		}

		else {
			gl = dl - bl;
			for (int i = 0; i < gl; i++)
				System.out.print(" ");
			System.out.println(b);
			System.out.println(c + d);
		}
		
		if(grl!=al && grl!=dl)
			if(al>=dl)
				e = grl - al;
			else e = grl-dl;
		while(e!=0){
			System.out.print(" ");
			e--;
		}
		if (al > dl)
			while (k < al) {
				System.out.print("-");
				k++;
			}
		else
			while (k < dl) {
				System.out.print("-");
				k++;
			}
	}
}
