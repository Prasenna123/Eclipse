package home;

import java.util.ArrayList;

public class Solution {

	public static void main(String[] args) {
		
		ArrayList<Character> ch = new ArrayList<Character>();
		ch.add('a');
		ch.add('b');
		ch.add('c');
		ch.add('d');
		ArrayList<Node> n = new ArrayList<Node>();
		Node n2 = new Node('h',getSubRecs(ch));
	}
	
	public static ArrayList<Node> getSubRecs(ArrayList<Character> c) {
		
		if(c==null) return null;
		else {
			int length = c.size()-2;
			
		}
		//if list empty return null 
		//calculate possible roots
		//for each root word
		//remove root and
		//node n = new node(root,getsubrec(all possible roots)) and add to list
		
		return null;
	}
}

class Node{
	char data;
	ArrayList<Node> no;
	
	public Node(char data, ArrayList<Node> no) {
		this.data=data;
		this.no=no;
	}
}
