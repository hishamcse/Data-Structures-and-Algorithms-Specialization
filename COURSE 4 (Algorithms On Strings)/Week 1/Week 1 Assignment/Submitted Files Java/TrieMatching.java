import java.io.*;
import java.util.*;

class Node {
	public static final int Letters = 4;
	public static final int NA = -1;
	public int[] next;

	Node() {
		next = new int[Letters];
		Arrays.fill(next, NA);
	}

	public boolean isLeaf() {
		for (int i : next) {
			if (i != NA) {
				return false;
			}
		}
		return true;
	}
}

public class TrieMatching implements Runnable {
	int letterToIndex(char letter) {
		switch (letter) {
			case 'A':
				return 0;
			case 'C':
				return 1;
			case 'G':
				return 2;
			case 'T':
				return 3;
			default:
				assert (false);
				return Node.NA;
		}
	}

	List<Node> buildTrie(String[] patterns) {
		List<Node> trie = new ArrayList<Node>();
		Node root = new Node();
		trie.add(root);

		for (String pattern : patterns) {
			Node current = root;
			for (int i = 0; i < pattern.length(); i++) {
				char currentSymbol = pattern.charAt(i);
				int id = current.next[letterToIndex(currentSymbol)];
				if (id != Node.NA) {
					current = trie.get(id);
				} else {
					Node newNode = new Node();
					trie.add(newNode);
					current.next[letterToIndex(currentSymbol)] = trie.size() - 1;
					current = newNode;
				}
			}
		}

		return trie;
	}

	private boolean prefixTrieMatching(String text, List<Node> trie) {
		int i = 0;
		char symbol = text.charAt(0);
		Node v = trie.get(0);
		while (true) {
			int id = v.next[letterToIndex(symbol)];
			if (v.isLeaf()) {
				return true;
			} else if (v.next[letterToIndex(symbol)] != Node.NA) {
				v = trie.get(id);
				if (i + 1 < text.length()) {
					symbol = text.charAt(++i);
				} else {
					return v.isLeaf();
				}
			} else {
				return false;
			}
		}
	}

	List<Integer> solve(String text, List<String> patterns) {

		List<Node> trie = buildTrie(patterns.toArray(new String[0]));

		List<Integer> result = new ArrayList<Integer>();
		int i = 0;
		while (!text.isEmpty()) {
			if (prefixTrieMatching(text, trie)) {
				result.add(i);
			}
			text=text.substring(1);
			i++;
		}

		return result;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String text = in.readLine();
			int n = Integer.parseInt(in.readLine());
			List<String> patterns = new ArrayList<String>();
			for (int i = 0; i < n; i++) {
				patterns.add(in.readLine());
			}

			List<Integer> ans = solve(text,patterns);

			for (int j = 0; j < ans.size(); j++) {
				System.out.print("" + ans.get(j));
				System.out.print(j + 1 < ans.size() ? " " : "\n");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		new Thread(new TrieMatching()).start();
	}
}

