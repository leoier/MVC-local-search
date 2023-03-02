# Local Search Algorithms for Minimum Vertex Cover

## Introduction

This repository contains an implementation of two local search algorithms for finding the minimum vertex cover of an undirected graph: hill-climbing heuristic and simulated annealing. The purpose of this repository is to provide a comparison of the performance of these two algorithms.

A [minimum vertex cover](https://en.wikipedia.org/wiki/Vertex_cover) is a set of vertices in an undirected graph such that every edge in the graph is incident to at least one vertex in the set. The problem of finding the minimum vertex cover is an important problem in graph theory with many practical applications, such as in scheduling, resource allocation, and network design.

The hill-climbing heuristic and simulated annealing are two popular local search algorithms for finding the minimum vertex cover:

- The hill-climbing heuristic works by iteratively selecting a neighbor of the current solution that reduces the size of the vertex cover, until a local minimum is reached. This implementation adopts the heuristic optimizations by [Shaowei(2015)](https://dl.acm.org/doi/10.5555/2832249.2832353), which efficiently helps the process jump out of local optimum and converge to the better optimum quickly.

- Simulated annealing is a probabilistic algorithm that allows for occasional uphill moves to avoid getting trapped in local minima.

## Directory structure 
```bash
.
├── classes             # the directory of executables 
├── DATA                # CREATE this directory and place the graph files to be used
├── Makefile            # makefile to compile and clean up the executable
├── output              # the output files of the experiments are found HERE
├── README.md
├── runRandomSeed.sh    # script for running the experiments with varying random seed
├── runTests.sh         # script for running the experiments 
└── src
    ├── Graph.java      # Graph class
    ├── MVC.java        # the algorithms to find the minimum vertex cover
    └── RunExperiments.java     # driver for file I/O and experiments
```


## Instruction to run the experiments
1. Unzip all files into one directory. 
2. Create a "DATA" directory under the current directory and put the graph files in it.
3. Execute `make` to complie the Java source codes.
4. Execute `./runTests.sh` to input the arguments and run the experiments. Alternatively, you may execute `java -cp ./classes RunExperiments -inst <filename> -alg [LS1|LS2] -time <cutoff in seconds> -seed <random seed>` to run the program.
5. The experiment results can be found in the "output" directory. 

## Clean up the program
Execute `make clean` to clean up the executables.
