#
# Author : Manu Kenchappa Junjanna
# Email : mkenchappajunjanna1@sheffield.ac.uk
# Created on Sun Dec 10 2023
#


.PHONY: clean

# This target cleans the built classes.
clean:
	find ./ -name "*.class" -exec rm -f {} +

# This target cleans all built class, then compiles and runs Aliens Program.
run: clean
	javac MyLanguageModel.java
	java MyLanguageModel
