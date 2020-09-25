import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NonSharedSubstring implements Runnable {

//    From discussion forum:
//    I used the hint in the problem description, i.e., construct suffix tree of TEXT1#TEXT2$.
//    Then you will find that the leaves whose path start from TEXT1 always contain # sign (type L leaf).
//    And those don't contain # sign and ending with $ sign are leaves whose path starts from TEXT2 (type R leaf).
//    The next step is to check every type L leaf. A candidate answer would be the path + the first letter of
//    type L leaf (except for the case that type L leaf starts with #), because we are not sure whether the path is
//    shared with some type R leaves, by adding the first letter of the current type L leaf we can make sure the
//    substring is not shared by any type R leaf (an exception in next paragraph). Then you just have to select the
//    shortest candidate.
//
//    However, note that if a non-leaf node has type L leaves only, it means that the candidate answer would be only
//    the path ending at the current non-leaf node instead of the path + the first letter of the left leaf. In this case,
//    we are sure that the path is shared by type L leaves only, thus there is no need to add an extra letter.

	static class Node {
		String text;
		boolean candidate;
		List<Node> children = new ArrayList<>();
	}

	static class CommonPrefix {   // common prefix of two string
		int len;
		Node prefixNode;
	}

	public void createSuffixTree(String text, Node root) {
		Node whole = new Node();
		whole.text = text;
		root.children.add(whole);

		for (int i = 1; i < text.length(); i++) {
			appendSuffixesToTree(root, new StringBuilder(text.substring(i)));
		}
	}

	private void appendSuffixesToTree(Node current, StringBuilder str) {
		while (str.length() != 0) {
			CommonPrefix commonPrefix = findCommonPrefix(current, str);
			if (commonPrefix.len == 0) {   // no common prefix
				addChild(current, str.toString());
				str.delete(0, str.length());
			} else {
				current = commonPrefix.prefixNode;
				if (current.text.length() == commonPrefix.len) {   // common prefix string length equal to current
					str.delete(0, commonPrefix.len);
				} else {
					split(current, commonPrefix);
					addChild(current, str.substring(commonPrefix.len));
					str.delete(0, str.length());
				}
			}
		}
	}

	private CommonPrefix findCommonPrefix(Node current, StringBuilder str) {
		CommonPrefix prefix = new CommonPrefix();

		for (Node child : current.children) {
			int commonLength = 0;
			for (int i = 0; i < str.length() && i < child.text.length(); i++) {
				if (child.text.charAt(i) == str.charAt(i)) {
					commonLength++;
				} else {
					break;
				}
			}
			if (commonLength > prefix.len) {
				prefix.len = commonLength;
				prefix.prefixNode = child;
			}
		}
		return prefix;
	}

	private void addChild(Node node, String str) {
		Node newNode = new Node();
		newNode.text = str;
		node.children.add(newNode);
	}

	private void split(Node current, CommonPrefix commonPrefix) {
		Node newNode = new Node();
		newNode.text = current.text.substring(commonPrefix.len);
		newNode.children.addAll(current.children);
		current.text = current.text.substring(0, commonPrefix.len);
		current.children.clear();
		current.children.add(newNode);
	}

	String solve(String p, String q) {

		String sb = p + "#" + q + "$";
		List<String> ans = new ArrayList<>();
		Node root = new Node();
		createSuffixTree(sb, root);

		possibleCandidates(root);
		candidateAnswers(root, "", ans);

		String answer = ans.get(0);
		for (int i = 1; i < ans.size(); i++) {
			if (ans.get(i).length() < answer.length()) {
				answer = ans.get(i);
			}
		}

		return answer;
	}

	private void possibleCandidates(Node node) {
		// leaf node
		if (node.text != null && node.text.contains("#") && node.children.isEmpty()) {
			node.candidate = true;
		}
		for (Node child : node.children) {
			possibleCandidates(child);
		}

		// non leaf node
		int c = 0;
		if (!node.children.isEmpty()) {   // if all the children are candidate,then the parent should be candidate too
			for (Node child : node.children) {
				if (!child.candidate) {
					c = 1;
					break;
				}
			}
			if (c == 0) {
				node.candidate = true;
			}
		}
	}

	private void candidateAnswers(Node node, String path, List<String> list) {
		if (node.candidate && !node.text.startsWith("#")) {
			list.add(path + node.text.charAt(0));
		}
		String str;
		for (Node child : node.children) {
			str = node.text==null?"":node.text;
			candidateAnswers(child, path + str, list);
		}
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String p = in.readLine();
			String q = in.readLine();

			String ans = solve(p, q);

			System.out.println(ans);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		new Thread(new NonSharedSubstring()).start();
	}
}

