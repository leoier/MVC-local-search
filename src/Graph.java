/**
 * Class for unweighted graph implemented on adjancy list
 * Supports parsing edges from the graph file
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Graph {

	private Map<Integer, Set<Integer>> adjList;	// mapping of (vertex id -> adjacency list)
	private int numEdges;


	/** Initialize an empty graph */
	public Graph() {
		adjList = new HashMap<>();
		numEdges = 0;
	}


	/** Initialize an empty graph with the given number of vertices */
	public Graph(int numVertices) {
		adjList = new HashMap<>();
		numEdges = 0;
		for (int i = 1; i <= numVertices; ++i) {
			addVertex(i);
		}
	}


	/** Create a copy of graph g */
	public Graph(Graph g) {
		adjList = new HashMap<>();
		for (int v: g.adjList.keySet()) {
			adjList.put(v, new HashSet<>(g.adjList.get(v)));
		}
		numEdges = g.numEdges;
	}


	/** Add a vertex to a graph */
	public boolean addVertex(int id) {
		if (adjList.containsKey(id)) {
			// the vertex is already in the graph
			return false;
		}
		adjList.put(id, new HashSet<>());
		return true;
	}


	/** Remove a vertex from the graph */
	public boolean removeVertex(int id) {
		if (!adjList.containsKey(id)) {
			// the vertex does not exist in the graph
			return false;
		}
		// remove the vertex from the adjacency lists of its neighbours
		for (int v: adjList.get(id)) {
			adjList.get(v).remove(id);
			numEdges--;
		}
		adjList.remove(id);
		return true;
	}


	/** Add an edge from u to v */
	public boolean addEdge(int u, int v) {
		if (!adjList.containsKey(u) || !adjList.containsKey(v) || u == v) {
			return false;
		}
		adjList.get(u).add(v);
		adjList.get(v).add(u);
		numEdges++;
		return true;
	}


	/** Remove the edge from u to v */
	public boolean removeEdge(int u, int v) {
		if (!adjList.containsKey(u) || !adjList.containsKey(v) || u == v) {
			return false;
		}
		adjList.get(u).remove(v);
		adjList.get(v).remove(u);
		numEdges--;
		return true;
	}


	/** Return true if there is an edge between u and v, false otherwise */
	public boolean isConnected(int u, int v) {
		return adjList.get(u).contains(v);
	}


	/** Return a list of neighbours of vertex i */
	public List<Integer> getNeighbours(int i) {
		return new ArrayList<Integer>(adjList.get(i));
	}


	/** Return the degree of a vertex */
	public int getVertexDegree(int i) {
		return adjList.get(i) == null ? 0 : adjList.get(i).size();
	}


	/** Return the number of vertices in the graph */
	public int getNumVertex() {
		return adjList.size();
	}


	/** Return the number of edges in the graph */
	public int getNumEdge() {
		return numEdges;
	}


	/** Return a sorted list of vertices in the graph */
	public List<Integer> getVertexList() {
		List<Integer> vertices = new ArrayList<>(adjList.keySet());
		Collections.sort(vertices);
		return vertices;
	}

	public Set<Integer> getVertices() {
		return adjList.keySet();
	}


	/** Return a list of all the edges as int arrays [u, v] */
	public List<int[]> getEdgeList() {
		List<int[]> edges = new ArrayList<>();
		for (int u: adjList.keySet()) {
			for (int v: adjList.get(u)) {
				if (v > u) {
					// rule out the duplicate edges of the undirected graph
					edges.add(new int[]{u, v});
				}
			}
		}
		return edges;
	}


	public String toString() {
		StringBuilder ret = new StringBuilder();
		for (int vertex: adjList.keySet()) {
			ret.append(vertex + ": ");
			ret.append(getNeighbours(vertex));
			ret.append("\n");
		}
		return ret.toString();
	}


	/** Create a graph from the graph file */
	public static Graph parseFile(String filename) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("./DATA/" + filename));
		String line = br.readLine();
		String[] params = line.split(" ");
		int numVertices = Integer.parseInt(params[0]);
		int numEdges = Integer.parseInt(params[1]);
		Graph G = new Graph(numVertices);
		// add the list of adjacencies for vertex i
		for (int i = 1; i <= numVertices; ++i) {

			line = br.readLine();
			if (line != null && line.length() > 0) {
				String[] split = line.split(" ");
				for (String vertex: split) {
					int v = Integer.parseInt(vertex);
					if (v > i) {
						G.addEdge(i, v);
					}
				}
			}
		}
		br.close();
		return G;
	}

}
