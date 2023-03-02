#!/bin/bash

graphFiles=`ls ./DATA/ | grep .graph`

read -p 'Enter the instance name ("all" for all files): ' instance
read -p 'Enter the algorithm to use: ' algorithm
read -p 'Enter the cut-off time: ' cutoff
read -p 'Enter the random seed: ' seed

if [ ${instance} = 'all' ]; then
	for file in ${graphFiles}
	do
		instance=`echo ${file} | cut -d'.' -f1`
		echo 'Running test for' ${instance}
		java -Xmx16G -cp ./classes RunExperiments -inst ${file} -alg ${algorithm} -time ${cutoff} -seed ${seed}
	done
else 
	echo 'Running test for' ${instance}
	java -Xmx16G -cp ./classes RunExperiments -inst ${instance}.graph -alg ${algorithm} -time ${cutoff} -seed ${seed}
fi

echo 'Done'