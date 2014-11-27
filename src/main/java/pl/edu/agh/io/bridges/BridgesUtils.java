package pl.edu.agh.io.bridges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class BridgesUtils {
	
	//atrybuty
	public static final String VISITED = "visited";
	public static final String SPANNING_TREE = "spanningTree"; //czy krawêdŸ nalezy do drzewa rozpinaj¹cego
	public static final String DFS_NUMBER = "dfsNumber";
	public static final String LOW = "low";

	private Integer dfsCount;
	private List<Edge> bridges;

	public List<Edge> getBridges(Node root) {
		dfsCount = 1;
		bridges = new ArrayList<Edge>();

		dfs(root);
		return bridges;
	}

	public void dfs(Node root) {
		root.setAttribute(VISITED, true);
		root.addAttribute(DFS_NUMBER, dfsCount);

		Iterator<Node> ite = root.getNeighborNodeIterator();
		while (ite.hasNext()) {
			Node n = ite.next();

			if (!(boolean) n.getAttribute(VISITED)) {
				root.getEdgeBetween(n).setAttribute(SPANNING_TREE, true);
				dfsCount++;
				dfs(n);
			}
		}

		int low = getLow(root);
		root.addAttribute(LOW, low);
		Node parent = getSpanningTreeParent(root);

		if (low == (Integer) root.getAttribute(DFS_NUMBER) && parent != null)
			bridges.add(root.getEdgeBetween(parent));
	}

	private int getLow(Node n) {
		int low = Math.min((Integer) n.getAttribute(DFS_NUMBER),
				getMinChildrenLow(n));
		low = Math.min(low, getMinNonChildrenNumber(n));

		return low;
	}

	//min parametrów Low wszystkich dzieci n na drzewie rozpinaj¹cym
	private int getMinChildrenLow(Node n) {

		Integer min = Integer.MAX_VALUE;

		Iterator<Node> ite = n.getNeighborNodeIterator();
		while (ite.hasNext()) {
			Node neighbor = ite.next();
			if (isSpanningTreeChild(n, neighbor)
					&& (min > (Integer) neighbor.getAttribute(LOW)))
				min = (Integer) neighbor.getAttribute(LOW);
		}
		return min;
	}

	// min numerów DFS wierzcho³ków po³¹czonych z n za pomoc¹ krawêdzi wtórnych 
	private int getMinNonChildrenNumber(Node n) {
		Integer min = Integer.MAX_VALUE;

		Iterator<Node> ite = n.getNeighborNodeIterator();
		while (ite.hasNext()) {
			Node neighbor = ite.next();
			Integer dfsNumber = neighbor.getAttribute(DFS_NUMBER);

			if (!isSpanningTreeEdge(n.getEdgeBetween(neighbor))
					&& (min > dfsNumber)) {
				min = dfsNumber;
			}
		}
		return min;
	}

	private Node getSpanningTreeParent(Node n) {

		Iterator<Node> ite = n.getNeighborNodeIterator();
		while (ite.hasNext()) {
			Node neighnor = ite.next();

			if (isSpanningTreeChild(neighnor, n))
				return neighnor;
		}
		return null;
	}

	private boolean isSpanningTreeChild(Node parent, Node child) {
		Integer parentNumber = (Integer) parent.getAttribute(DFS_NUMBER);
		Integer childNumber = (Integer) child.getAttribute(DFS_NUMBER);

		return (childNumber > parentNumber)
				&& (isSpanningTreeEdge(parent.getEdgeBetween(child)));

	}

	private boolean isSpanningTreeEdge(Edge e) {
		return (boolean) e.getAttribute(SPANNING_TREE);
	}

}
