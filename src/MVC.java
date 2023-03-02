/**
 *
 * Algorithms for solving the Minimum vertex problem
 * LS1() 	-> Local Search: Hill Climbing
 * LS2()	-> Local Search: Simulated Annealing
 */

import java.util.*;

public class MVC {

    private Graph G;
    private int cutoff;
    private Random rand;

    private List<Integer> solution; // the list of in vertices in the MVC
	private List<Integer> qualityTrace;
	private List<Double> runningTimeTrace;

	private final long SEC_TO_NANOSEC = 1000000000;

    public MVC(Graph G, String method, int cutoff, Random rand) {
        // initialize the global variables
    	this.G = G;
        this.cutoff = cutoff;
        this.rand = rand;
        solution = new ArrayList<>();
		qualityTrace = new ArrayList<>();
		runningTimeTrace = new ArrayList<>();

        switch (method) {
			case "LS1":
				LS1();
				break;
			case "LS2":
				LS2();
				break;
			default:
				System.err.println("The method arugment must be one of the following: LS1, LS2");
				System.exit(1);
		}

    }

    public int getQuality() {
        return solution.size();
    }

    public List<Integer> getSolution() {
        return solution;
    }

	public List<Integer> getQualityTrace() {
		return qualityTrace;
	}

	public List<Double> getRunningTimeTrace() {
		return runningTimeTrace;
	}


    /* -------------- Local Search 1 - Heuristic Hill Climbing -------------- */

    private void LS1() {
		// find an initial solution
		Set<Integer> cover = initialSolution();
		qualityTrace.add(cover.size());
		runningTimeTrace.add(0.0);

		Set<Integer> bestSol = new HashSet<Integer>(cover);

		long nanoCutoff = (long)cutoff * SEC_TO_NANOSEC;
		long startTime = System.nanoTime();

		// Hill Climbing
		while (System.nanoTime() - startTime < nanoCutoff) {
			if (isVertexCover(cover)) {
				// update the best solution and trace files if the current solution is better
				if (cover.size() < bestSol.size()) {
					bestSol = new HashSet<Integer>(cover);
					qualityTrace.add(bestSol.size());
					runningTimeTrace.add((System.nanoTime() - startTime) / (double)SEC_TO_NANOSEC);
				}

                // remove the vertex with minimum cost
				int vertexToRemove = -1;
				int minCost = Integer.MAX_VALUE;
				for (int v: cover) {
					int cost = vertexCost(cover, v);
					if (cost < minCost) {
						vertexToRemove = v;
						minCost = cost;
					}
				}
				cover.remove(vertexToRemove);
			}

			// remove a vertex with minimum cost by selecting 50 elements
			int vertexToRemove = -1;
			int minCost = Integer.MAX_VALUE;
			for (int k = 0; k < 50; ++k) {
				int idx = rand.nextInt(cover.size());
				int i = 0;
				int v = -1;
				for (int u: cover) {
					if (i == idx) {
						v = u;
						break;
					}
					++i;
				}
				int cost = vertexCost(cover, v);
				if (cost < minCost) {
					vertexToRemove = v;
					minCost = cost;
				}
			}
            cover.remove(vertexToRemove);


			// find a random uncovered edge and calculate the gain of the two endpoints
			// where gain is the number of uncovered edges covered by adding the vertex
			List<int[]> uncovered = getUncoveredEdges(cover);
			if (uncovered.size() > 0) {
                int idx = rand.nextInt(uncovered.size());
				int[] randEdge = uncovered.get(idx);
				int gain0 = vertexGain(cover, randEdge[0]);
				int gain1 = vertexGain(cover, randEdge[1]);
				if (gain0 > gain1) {
					cover.add(randEdge[0]);
				} else if (gain0 < gain1) {
					cover.add(randEdge[1]);
				} else {
                	cover.add(randEdge[rand.nextInt(2)]);
				}
			}

		}

		solution = new ArrayList<>(bestSol);
		Collections.sort(solution);
    }


	/** Generate an initial solution by Edge Deletion */
	private Set<Integer> initialSolution() {
		Set<Integer> cover = new HashSet<>();
		for (int[] edge: G.getEdgeList()) {
			// the edge is regarded as 'deleted' if either of the end points are in the vertex cover
			if (cover.contains(edge[0]) || cover.contains(edge[1])) {
				continue;
			}
            // add the vertex with higher degree
            if (G.getVertexDegree(edge[0]) > G.getVertexDegree(edge[1])) {
                cover.add(edge[0]);
            } else {
                cover.add(edge[1]);
            }
		}
		return cover;
	}


	/** Return a list of uncovered edges under a partial vertex cover */
	private List<int[]> getUncoveredEdges(Set<Integer> cover) {
		List<int[]> uncovered = new ArrayList<>();
		for (int[] edge: G.getEdgeList()) {
			if (!cover.contains(edge[0]) && !cover.contains(edge[1])) {
				uncovered.add(edge);
			}
		}
		return uncovered;
	}


	/** Verify cover is a vertex cover or not */
	private boolean isVertexCover(Set<Integer> cover) {
		for (int[] edge: G.getEdgeList()) {
			if (!cover.contains(edge[0]) && !cover.contains(edge[1])) {
				return false;
			}
		}
		return true;
	}


	/** Return the number of edges become uncovered if vertex is removed from C */
	private int vertexCost(Set<Integer> C, int vertex) {
		if (!C.contains(vertex)) {
			return Integer.MAX_VALUE;
		}
		int count = 0;
		for (int v: G.getNeighbours(vertex)) {
			if (!C.contains(v)) {
				++count;
			}
		}
		return count;
	}


	/** Return the number of edges become covered if vertex is added to C */
	private int vertexGain(Set<Integer> C, int vertex) {
		if (C.contains(vertex)) {
			return 0;
		}
		int count = 0;
		for (int v: G.getNeighbours(vertex)) {
			if (!C.contains(v)) {
				++count;
			}
		}
		return count;
	}


    /* -------------- Local Search 2 - Simulated Annealing -------------- */

    private void LS2() {
		// find an initial solution
		Set<Integer> cover = initialSolution();
		qualityTrace.add(cover.size());
		runningTimeTrace.add(0.0);

		Set<Integer> bestSol = new HashSet<Integer>(cover);

		int noImprovementCnt = 0;

		// initial temperature is tuned such that the initial probability is about 0.8
		final double INIT_TEMP = 8.96;

		double T = INIT_TEMP;	// initial temperature

		long nanoCutoff = (long)cutoff * SEC_TO_NANOSEC;
		long startTime = System.nanoTime();

		// Simulated Annealing
		while (System.nanoTime() - startTime < nanoCutoff) {
			if (isVertexCover(cover)) {
				// update the best solution and trace files if the current solution is better
				if (cover.size() < bestSol.size()) {
					bestSol = new HashSet<Integer>(cover);
					qualityTrace.add(bestSol.size());
					runningTimeTrace.add((System.nanoTime() - startTime) / (double)SEC_TO_NANOSEC);
				}

				// remove a random vertex
				int removeIdx = rand.nextInt(cover.size());
				int idx = 0;
				for (int u: cover) {
					if (idx == removeIdx) {
						cover.remove(u);
						break;
					}
					++idx;
				}

			}

			Set<Integer> candidate = new HashSet<>(cover);

			// remove a random vertex from the cover
			int exitIdx = rand.nextInt(candidate.size());
			int idx = 0;
			for (int u: candidate) {
				if (idx == exitIdx) {
					candidate.remove(u);
					break;
				}
				++idx;
			}

			// include a random end point from a random uncovered edge
			List<int[]> uncovered = getUncoveredEdges(candidate);
			if (uncovered.size() > 0) {
				int[] edge = uncovered.get(rand.nextInt(uncovered.size()));
				candidate.add(edge[rand.nextInt(2)]);
			}

			// compare the cost, accept a worse candidate with some probability
			int delta = -(coverCost(candidate) - coverCost(cover));
			if (delta > 0) {
				cover = candidate;
				noImprovementCnt = 0;
			} else {
				++noImprovementCnt;
				if (rand.nextDouble() < Math.exp(delta / T)) {
					cover = candidate;
				}
			}

			T *= 0.95;

			// restart annealing if there is no improvement for a long time
			if (noImprovementCnt > 1000) {
				noImprovementCnt = 0;
				T = INIT_TEMP;
			}

		}

		solution = new ArrayList<>(bestSol);
		Collections.sort(solution);
    }

	/** Return the number of uncovered edges in the partial cover */
	private int coverCost(Set<Integer> cover) {
		int count = 0;
		for (int[] edge: G.getEdgeList()) {
			if (!cover.contains(edge[0]) && !cover.contains(edge[1])) {
				++count;
			}
		}
		return count;
	}

}
