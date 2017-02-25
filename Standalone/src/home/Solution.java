package home;


import java.util.*;

public class Solution {

	 public static void main(String[] args) {
	       Scanner s = new Scanner(System.in);
	       String a = s.nextLine();
	       String c = a.trim();
	       String[] b = c.split("[\\_\\@ !,?.']+");
	       System.out.println(b[0]);
	       if(c.length()==0) {System.out.println("0");}
	       else {System.out.println(b.length);
	       for(String g: b) System.out.println(g);
	    }}
}
