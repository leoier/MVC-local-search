# Minimum Vertex Cover

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
5. The experiment results can be found in the "results" directory. 

## Clean up the program
Execute `make clean` to clean up the executables.
