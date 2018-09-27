To compile
	javac *.java
To run
	1. include A4PartA/testing and A4PartA/training under directory COMP 3190 A4 Minghao Li/PartA. 
		(since the file reader path is A4PartA/training/ham and etc.)
	2. java Main

Output is in such format:
	first ham and spam classified in testing/spam,
	first ham and spam classified in testing/ham,
	correct and miss count of each type and their percentage,
	overall error rate.

Three source files:
	1. Main.java: used to call training and testing method
	2. Util.java: include all training and testing strategy
	3. Word.java: used to store as the "value" part of the key-value pair in a hash map

Implementation:
	For training:
	First read all files in training, get rid of header, build a frequency table using word spliting strategy described below.
	Then calculate probability of each element: p(Ham), p(Spam), p(a1|Ham)...
	Remove some words that are not good indicator using the method described on the paper.

	For testing:
	Read all files in testing, for each word that appers in frequency table, take the logarithm of each of them and add them up.
	Compare which one is bigger, then decide whether it's a spam or a ham.
	Print the statistics at the end of testing.

Word Splitting strategy:
	First using the method described on http://slendermeans.org/ml4h-ch3.html, which is: 
		1. strip out weird '3D' artefacts 
		2. strip out html tags and attributes and character codes 
	Then spilt by "\\s+"
	Then check whether each token is a trigram or not. If its not a trigram, strip out all the punctuations.

	
