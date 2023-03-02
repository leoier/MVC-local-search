/*
 * Minimum Vertex Cover
 *
 * Run the experiments on the graphs and output the quality and running time
 *
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;
import java.util.List;


public class RunExperiments {

	public static void main(String[] args) throws Exception {

		// parse the input arguments
		String errMsg = "The arguments must be in the following format:\n" +
			"-inst <filename> -alg [BnB|Approx|LS1|LS2] -time <cutoff in seconds> -seed <random seed>";

		if (args.length < 8) {
			System.err.println(errMsg);
			System.exit(1);
		}

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("inst", null);
		parameters.put("alg", null);
		parameters.put("time", null);
		parameters.put("seed", null);

		for (int i = 0; i < args.length - 1; i += 2) {
			String key = args[i].substring(1);
			if (!parameters.containsKey(key)) {
				System.err.println(errMsg);
				System.exit(1);
			}
			parameters.put(key, args[i + 1]);
		}

		for (String key: parameters.keySet()) {
			if (parameters.get(key) == null) {
				System.err.println(errMsg);
				System.exit(1);
			}
		}

		String filename = parameters.get("inst");
		String method = parameters.get("alg");
		int cutoff = Integer.parseInt(parameters.get("time"));		// cut-off time in seconds
		int randSeed = Integer.parseInt(parameters.get("seed"));	// random seed

		Graph G = Graph.parseFile(filename);	// parse the graph from the input file
		Random rand = new Random(randSeed);		// generate the random object with randSeed

		// generate the minimum vertex cover
		MVC mvc = new MVC(G, method, cutoff, rand);

		List<Integer> bestResult = mvc.getSolution();
		int bestQuality = mvc.getQuality();
		List<Integer> qualityTrace = mvc.getQualityTrace();
		List<Double> runningTimeTrace = mvc.getRunningTimeTrace();

		new File("./output").mkdirs();	// create the output directory

		PrintWriter output;
		String instance = parameters.get("inst").split("\\.")[0];	// remove the .graph extension
		String outputInfo = instance + "_" + parameters.get("alg")
			+ "_" + parameters.get("time");
		if (parameters.get("alg").equals("Approx") || parameters.get("alg").equals("LS1") || parameters.get("alg").equals("LS2")) {
			outputInfo += "_" + parameters.get("seed");
		}
		// Write results to the solution file
		output = new PrintWriter("./output/" + outputInfo + ".sol");
		output.println(bestQuality);
		for (int i = 0; i < bestResult.size(); ++i) {
			output.print(bestResult.get(i));
			if (i < bestResult.size() - 1) {
				output.print(',');
			}
		}
		output.println();
		output.close();

		// Append results to the trace file
		output = new PrintWriter("./output/" + outputInfo + ".trace");
		for (int i = 0; i < qualityTrace.size(); ++i) {
			output.println(runningTimeTrace.get(i) + ", " + qualityTrace.get(i));
		}
		output.close();
	}

}
