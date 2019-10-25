import java.util.Iterator;
import java.util.LinkedList;

import bridges.base.LinkVisualizer;
import bridges.base.TreeElement;

public class BinaryNode<K,V> extends TreeElement<K> implements Iterable<BinaryNode<K,V>>{

	protected K key;
	protected V value;
	protected BinaryNode<K,V> left;
	protected BinaryNode<K,V> right;
	protected enum Order{PRE,POST,IN,LEVEL};
	protected Order order = Order.LEVEL;
	
	BinaryNode(K aKey, V aValue){
		key = aKey;
		value = aValue;
	}
	
	BinaryNode(K aKey, V aValue, BinaryNode<K,V> aLeft, BinaryNode<K,V> aRight){
		key = aKey;
		value = aValue;
		left = aLeft;
		right = aRight;
	}
	
	public boolean isLeaf() {
		return left == null && right == null;
	}
	
	@Override
	public Iterator<BinaryNode<K,V>> iterator() {
		if(order == Order.PRE) {
			return new preOrderIterator(this);
		}
		else if(order == Order.POST) {
			return new postOrderIterator(this);
		}
		else if(order == Order.IN) {
			return new inOrderIterator(this);
		}
		else {
			return new levelOrderIterator(this);
		}
	}

	class preOrderIterator implements Iterator<BinaryNode<K,V>>{

		LinkedList<BinaryNode<K,V>> preOrderQueue = new LinkedList<BinaryNode<K,V>>();
		
		public preOrderIterator(BinaryNode<K,V> root) {
			preOrderTraversal(root);
		}
		
		private void preOrderTraversal(BinaryNode<K,V> root) {
			preOrderQueue.add(root);
			if(root.left != null){ preOrderTraversal(root.left);  }
			if(root.right != null){ preOrderTraversal(root.right); }
		}
		
		@Override
		public boolean hasNext() {
			return !preOrderQueue.isEmpty();
		}

		@Override
		public BinaryNode<K,V> next() {
			return preOrderQueue.removeFirst();
		}
		
	}
	
	class inOrderIterator implements Iterator<BinaryNode<K,V>>{

		LinkedList<BinaryNode<K,V>> inOrderQueue = new LinkedList<BinaryNode<K,V>>();
		
		public inOrderIterator(BinaryNode<K,V> root) {
			inOrderTraversal(root);
		}
		
		private void inOrderTraversal(BinaryNode<K,V> root) {
			if(root.left != null){ inOrderTraversal(root.left);  }
			inOrderQueue.add(root);
			if(root.right != null){ inOrderTraversal(root.right); }
		}
		
		@Override
		public boolean hasNext() {
			return !inOrderQueue.isEmpty();
		}

		@Override
		public BinaryNode<K,V> next() {
			return inOrderQueue.removeFirst();
		}
		
	}
	
	class postOrderIterator implements Iterator<BinaryNode<K,V>>{

		LinkedList<BinaryNode<K,V>> postOrderQueue = new LinkedList<BinaryNode<K,V>>();
		
		public postOrderIterator(BinaryNode<K,V> root) {
			postOrderTraversal(root);
		}
		
		private void postOrderTraversal(BinaryNode<K,V> root) {
			if(root.left != null){ postOrderTraversal(root.left);  }
			if(root.right != null){ postOrderTraversal(root.right); }
			postOrderQueue.add(root);
		}
		
		@Override
		public boolean hasNext() {
			return !postOrderQueue.isEmpty();
		}

		@Override
		public BinaryNode<K,V> next() {
			return postOrderQueue.removeFirst();
		}
		
	}

	class levelOrderIterator implements Iterator<BinaryNode<K,V>>{

		LinkedList<BinaryNode<K,V>> queue;
		
		public levelOrderIterator(BinaryNode<K,V> root) {
			 queue = new LinkedList<BinaryNode<K,V>>();
			 queue.add(root);
		}
		
		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public BinaryNode<K,V> next() {
			BinaryNode<K,V> node = queue.removeFirst();
			BinaryNode<K,V> toReturn = node;
			if(node.left != null) {queue.add(node.left);}
			if(node.right != null) {queue.add(node.right);}
			return toReturn;
		}
		
	}

	@Override
	public String getDataStructureRepresentation() {
		String json_str = QUOTE + "nodes"  + QUOTE + COLON + OPEN_CURLY  + preOrder(this) + CLOSE_CURLY + CLOSE_CURLY;
		return json_str;
	}
	
	private String preOrder(BinaryNode<K,V> root) {
		String json_str = "", elem_rep = "";
		String t_str;
		int numOfChildren = 2; //root.getNumberOfChildren();
		if (root != null) {
			root.setLabel("Key: " + root.key + "\n\n Value: " + root.value);
			// first get the node representation
			elem_rep = root.getElementRepresentation();
			// remove surrounding curly braces
			t_str = elem_rep.substring(1, elem_rep.length() - 1);
			json_str += t_str;
			// now get the children
			if (numOfChildren > 0) {
				json_str += COMMA + QUOTE + "children" + QUOTE + COLON + OPEN_BOX ;
			}
			//			else json_str += CLOSE_CURLY;
			for (int k = 0; k < numOfChildren; k++) {
				BinaryNode<K,V> child = k == 0 ? root.left : root.right;
				if (child == null) {
					json_str += OPEN_CURLY + QUOTE + "name" + QUOTE + COLON +
						QUOTE + "NULL" + QUOTE + CLOSE_CURLY + COMMA;
				}
				else {
					LinkVisualizer lv = root.getLinkVisualizer(child);
					json_str += OPEN_CURLY;
					if (lv != null) {
						json_str +=
							QUOTE + "linkProperties" + QUOTE + COLON + OPEN_CURLY +
							QUOTE + "color" + QUOTE + COLON +
							OPEN_BOX +
							Integer.toString(lv.getColor().getRed()) + COMMA +
							Integer.toString(lv.getColor().getGreen()) + COMMA +
							Integer.toString(lv.getColor().getBlue()) + COMMA +
							Float.toString(lv.getColor().getAlpha()) +
							CLOSE_BOX + COMMA +
							QUOTE + "thickness" + QUOTE + COLON +
							String.valueOf(lv.getThickness()) +
							CLOSE_CURLY + COMMA;
					}
					else
						json_str += "linkProperties" + COLON + "{}" + COMMA;
					// process its children
					json_str +=	preOrder(child);
					json_str += CLOSE_CURLY + COMMA;
				}
			}
			// remove last comma
			if (json_str.length() > 0) 	// deal with null tree case
				json_str = json_str.substring(0, json_str.length() - 1);
			// end of children
			json_str += CLOSE_BOX;
		}
		return json_str;
	}
	
	public String toString() {
		return "Key: " + key + " Value: " + value;
	}
}
