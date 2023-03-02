#!/bin/bash

graphFiles=`ls ./DATA/ | grep .graph`

read -p 'Enter the algorithm to use: ' algorithm
read -p 'Enter the cut-off time: ' cutoff


for file in ${graphFiles}
do
	instance=`echo ${file} | cut -d'.' -f1`
	echo 'Running test for' ${instance}
	for i in {1..10}
	do
		echo 'Random seed ' $i
		java -Xmx16G -cp ./classes RunExperiments -inst ${file} -alg ${algorithm} -time ${cutoff} -seed ${i}
	done
done

echo 'Done'