/*
 *  TightFit (c) 2008 The TightFit Development Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 */
package tightfit.util;

import java.util.*;

public class MultinaryTree<E> extends AbstractCollection<E> {

	MultinaryTreeNode root;
	
	public Iterator iterator() {
		LinkedList linear = new LinkedList();
		Stack others = new Stack();
		MultinaryTreeNode current;
		
		if(root != null) {
			others.push(root);
			
			while(!others.isEmpty()) {
				current = (MultinaryTreeNode) others.pop();
				linear.add(current.data);
				Iterator itr = current.children.iterator();
				while(itr.hasNext()) {
					others.push(itr.next());
				}
			}
		}
		return linear.iterator();
	}

	public int size() {
		int s = 0;
		Stack others = new Stack();
		MultinaryTreeNode current;
		
		others.push(root);
		
		while(!others.isEmpty()) {
			current = (MultinaryTreeNode) others.pop();
			s++;
			Iterator itr = current.children.iterator();
			while(itr.hasNext()) {
				others.push(itr.next());
			}
		}
		
		return s;
	}

	public MultinaryTreeNode addChild(Object d) {
		MultinaryTreeNode n = new MultinaryTreeNode(d);
		if(root == null)
			root = n;
		else
			root.children.add(n);
		
		return n;
	}
	
	public MultinaryTreeNode addChild(MultinaryTreeNode r, Object d) {
		MultinaryTreeNode n = new MultinaryTreeNode(d);
		if(root == null)
			root = n;
		else
			r.children.add(n);
		
		return n;
	}
	
	public class MultinaryTreeNode {
		public LinkedList children = new LinkedList();
		public Object data;
		
		public MultinaryTreeNode(Object d) {
			data = d;
		}
	};
}
