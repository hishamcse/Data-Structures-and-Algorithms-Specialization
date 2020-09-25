import java.util.*;
import java.io.*;

public class tree_height {

	static class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(in.readLine());
			return tok.nextToken();
		}

		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}

	public static class Node {
		private int index;
		public Stack<Node> childNodes;

		public Node(int i) {
			index=i;
			childNodes=new Stack<>();
		}

		public void addChild(Node p) {
			childNodes.push(p);
		}
	}

	public static class TreeHeight {
		int n;
		int[] parent;
		Node root;
		Node[] nodes;

		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			nodes = new Node[n];
			for (int i = 0; i < n; i++) {
				nodes[i] = new Node(i);
			}
			parent = new int[n];
			for (int i = 0; i < n; i++) {
				parent[i] = in.nextInt();
				if (parent[i] == -1) {
					root = nodes[i];
				} else {
					nodes[parent[i]].addChild(nodes[i]);
				}
			}
		}

		int computeHeight() {
			// Replace this code with a faster implementation
			Queue<Node> queue=new LinkedList<>();
			queue.add(root);
			int h=0;
			while (!queue.isEmpty()){
				int size= queue.size();
				h++;
				for(int j=0;j<size;j++) {
					Node item = queue.remove();
					for(Node x:item.childNodes) {
						queue.add(x);
					}
				}
			}
			return h;
		}
	}

	static public void main(String[] args) throws IOException {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					new tree_height().run();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}

	public void run() throws IOException {
		TreeHeight tree = new TreeHeight();
		tree.read();
		System.out.println(tree.computeHeight());
	}
}

