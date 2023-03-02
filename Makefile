PROG = RunExperiments

all:
	@mkdir -p classes
	@javac -d classes -cp src src/$(PROG).java
	@echo Program created: classes/$(PROG)

clean:
	@echo Cleaning up the program...
	rm -rf classes
 