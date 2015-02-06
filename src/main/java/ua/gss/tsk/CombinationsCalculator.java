package ua.gss.tsk;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import ua.gss.tsk.model.Node;
import ua.gss.tsk.model.Symbol;

public class CombinationsCalculator {

	private List<Symbol> alphabet;
	private int delta;

	public CombinationsCalculator(List<Symbol> symbols, int delta) {
		super();
		this.alphabet = symbols;
		this.delta = delta;
	}

	public BigDecimal calculateNk(int ns, Node previousNode) {
		BigDecimal nk = new BigDecimal(0);
		for (Symbol currentSymbol : alphabet) {
			if (currentSymbol.equals(previousNode.getSymbol())) {
				continue;
			}
			for (int lSymb = delta; lSymb <= ns - delta; lSymb++) {
				Node newNode = new Node();
				newNode.setSymbol(currentSymbol);
				newNode.setLength(lSymb);
				previousNode.addChild(newNode);
				nk = nk.add(calculateNk(ns - lSymb, newNode));
			}
			nk = nk.add(new BigDecimal(1));
			Node newNode = new Node();
			newNode.setSymbol(currentSymbol);
			newNode.setLength(ns);
			previousNode.addChild(newNode);
		}
		return nk;
	}
	
	public static List<List<Symbol>> parseNodeTree(Node rootNode) {
		List<List<Symbol>> combinations = new LinkedList<List<Symbol>>();
		for(Node node : rootNode.getChildren()) {
			List<Symbol> comb = new LinkedList<Symbol>();
			parseNodeTree(node, combinations, comb);
		}
		
		return combinations;
	}
	
	private static void parseNodeTree(Node node, List<List<Symbol>> combinations, List<Symbol> comb) {
		for(int i = 0; i<node.getLength(); i++) {
			comb.add(node.getSymbol());
		}
		
		if(node.getChildren() == null || node.getChildren().isEmpty()) {
			combinations.add(comb);
			return;
		}
		
		for(Node child : node.getChildren()) {
			List<Symbol> newComb = new LinkedList<Symbol>(comb);
			parseNodeTree(child, combinations, newComb);
		}
	}

}
