Student name: Saloni Karnik
Net-id: suk140030@utdallas.edu

To run the kMeans algorithm for clustering Tweets for part 2:

Run as follows:

./TweetsKMeans <numberOfClusters> <input-file-name> <output-file-name>

Example:
./TweetsKMeans  25  D:/Study/Machine Learning/Homework/Assignment 5/InitialSeeds.txt D:/Study/Machine Learning/Homework/Assignment 5/Tweets.json D:/Machine Learning/Homework/Assignment 5/tweets-k-means-output.txt


SSE value for (k=25) is 38.86875745974837

Note:
This program might take a long while to execute because of the triple for-loops which reduced the efficiency and increased the running time of the program.

Also,to read the json files,i used the json.org.simple.parser package. In case the program can't detect such a package, it will have to be downloaded from:
https://code.google.com/p/json-simple/
Click on the link where "json-simple-1.1.1.jar" is given. Download this jar in the same folder as the class in contained. In this case, class is stored in the part2 folder. 
Save this jar file into the part2 folder.


