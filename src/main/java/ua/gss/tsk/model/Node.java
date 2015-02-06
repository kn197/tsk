package ua.gss.tsk.model;

import java.util.LinkedList;
import java.util.List;

public class Node {

	private Symbol symbol;
	private int length;
	private Node parent;
	private List<Node> children;

	public Node() {
		children = new LinkedList<Node>();
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		children.add(child);
		child.parent = this;
	}

}
