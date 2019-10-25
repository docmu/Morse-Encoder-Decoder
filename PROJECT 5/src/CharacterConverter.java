import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import bridges.connect.Bridges;
import bridges.validation.RateLimitException;
/*******************
 * Christine Do
 * Spring 2019
 * CMSC 256 - 003
 * Description: this class is used to encode english letters/symbols and decode morse code
 */
@SuppressWarnings("rawtypes")
public class CharacterConverter extends BSTNode{
	//instance variables
	@SuppressWarnings("unused")
	private File INPUT_FILE;
	private String symbol;
	private String morseCode;
	private BSTNode root;
	
	/* parameterized constructor takes in a file containing info for morse code and builds a tree */
	@SuppressWarnings({ "unused" })
	public CharacterConverter(File aConversionFile){
		INPUT_FILE = aConversionFile;
		Scanner scan;
		
		root = new BSTNode();
		BSTNode curr = root;
		BSTNode parent = null;
		MorseCharacter MCObj;
		
		try {
			scan = new Scanner(aConversionFile);
			
			//while file has info to read add a key and value to the tree using helper method, buildTree
			while(scan.hasNext()) {
				symbol = scan.next().trim();
				morseCode = scan.next().trim();
				this.buildTree(morseCode, symbol);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * helper method to build the tree given the key and value
	 */
	@SuppressWarnings("unchecked")
	private void buildTree(String morseCode, String symbol) {
		BSTNode curr = root;
		BSTNode parent = null;
		MorseCharacter MCObj;
		
		/*
		 * for every '.' go left and add to the tree if the position is empty, then set parent.left to curr
		 * for every '-' go right and add to the tree if the position is empty, then set parent.right to curr
		 */
		for (char c : morseCode.toCharArray()) {
			parent = curr;
			if(c == '.') {
				curr = (BSTNode) curr.left;
				if(curr == null) {		
					curr = new BSTNode();
				}
					parent.left = curr;
				}
		
			
			else {
				curr = (BSTNode) curr.right;
				if(curr == null) {
					
					curr = new BSTNode();
				}
				parent.right = (BSTNode) curr;
			}
		}
		
		/*
		 * instantiate MorseCharacter object to store morse code
		 * set the key and value of the tree
		 */
		MCObj = new MorseCharacter(morseCode);
		curr.key = MCObj;
		curr.value = symbol;
	}
	
	/*
	 * converts an english String to morse code
	 */
	@SuppressWarnings("unchecked")
	public String convertFromEnglish(String anInput) { 
		BSTNode curr = root;//current node
		String result = ""; //String to return
		String symbol = ""; //english symbol from the given String
		String otherStr = ""; //value of current node
		
		//used to iterate over tree
		curr.order = Order.LEVEL; 
		Iterator it = curr.iterator();
		
		for(int i = 0; i < anInput.length();i++) {
			
			symbol = anInput.substring(i, i + 1);//the first character of the string
			
			//while value of current node is null and there are more values to iterate over, iterate to next node
			while(curr.value == null && (it.hasNext())) {
				curr = (BSTNode) it.next();
			}
			
			//set otherStr to current node's value
			otherStr = (String) curr.value;
			
			/* while otherStr(current node's value) != symbol(first character in given String) 
			 * && there is another node to iterate over then,
			 * set curr to next node
			 * if curr is null, set it to empty String to avoid crashing
			 * otherwise, set otherStr to curr.vale 
			 */
			while(!(otherStr.equals(symbol)) && (it.hasNext())) {
					
					curr = (BSTNode) it.next();
					
					if(curr.value == null) {
						otherStr = "";
					}
					else{
						otherStr = (String) curr.value;
					}
					
						
				}
			
			/* when otherStr matches symbol, the node we are looking for is found
			 * concat the morse code key to the return String
			 */
			result = result.concat(curr.key.toString());
			
			//put a space between each letter
			if(i < anInput.length() -1) { result += " ";}
			
			//start back at the beginning of the tree to look for next character
			curr = root;
			it = curr.iterator();
		}
		
		return result;
	}
	
	/*
	 * converts a morse code String to english
	 */
	@SuppressWarnings("unchecked")
	public String convertToEnglish(String anInput) {
		BSTNode current = root;
		String str = ""; //stores morse code character
		String result = ""; //return String
	      
	    for (int i = 0; i < anInput.length(); i++) {
	        str = anInput.substring(i, i + 1); //the first character of given String
	        
	        /*
	         * if '.' then go left.
	         * if left has a node then set current to its left
	         * otherwise set current.left to new empty node and set current to current.left
	         */
	        if (str.equals(".")) {
	            if (current.left != null) {
	                current = (BSTNode) current.left;
	            } 
	            else {
	                current.left = new BSTNode();
	                current = (BSTNode) current.left;
	            }
	        } 
	        
	        /*
	         * if '-' then go right
	    	 * if right has a node then set current to its right
	         * otherwise set current.right to new empty node and set current to current.right
	         */
	        else if (str.equals("-")) {
	            if (current.right != null) {
	                current = (BSTNode) current.right;
	            } 
	            else {
	                current.right = new BSTNode();
	                current = (BSTNode) current.right;
	            }
	        } 
	        
	        /*
	         * when found concat result to current.value
	         * set current to root
	         */
	        else {
	             result = result.concat((String) current.value);
	             current = root;
	        }
	     }
	    	
	        result = result.concat((String) current.value); //concat result to current.value
	        return result;

	}
	
	/*
	 * returns ArrayList of MorseCharacters in morse code order
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<MorseCharacter> getSymbolAlphabet(){
		ArrayList<MorseCharacter> list = new ArrayList<MorseCharacter>();
		BSTNode curr = root;
		curr.order = Order.PRE; //pre order traversal will ensure the list will be in morse code order
		Iterator it = curr.iterator();
		
		/*
		 * while there is a node to iterate over
		 * set curr to it.next
		 * if the key is not null then key to the ArrayList
		 */
		while(it.hasNext()) {
			curr = (BSTNode) it.next();
			if(curr.key != null) {
				list.add((MorseCharacter) curr.key);
			}
		}
		
		return list;
	}
	
	/*
	 * bridges visualization of the tree
	 */
	public void visualize() {
		Bridges bridges = new Bridges(2,"docm", "1093327499414");
		bridges.setDataStructure(root);
		try {
			bridges.visualize();
		} catch (IOException | RateLimitException e) {
			e.printStackTrace();
		}
	}

}
