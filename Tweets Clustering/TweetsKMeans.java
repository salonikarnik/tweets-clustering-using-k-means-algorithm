package part2;
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;



public class TweetsKMeans {

	public static void main(String[] args) throws IOException,FileNotFoundException, ParseException {
		//Taking inputs from command line
		String numberOfClusters=args[0];
		String initialSeedsFile=args[1];
		String inputFile=args[2];		
		String outputFile=args[3]";
		
		//Initializing variable to store tweets and initial seeds
		Scanner sc=new Scanner(new File(inputFile));
		Scanner initial=new Scanner(new File(initialSeedsFile));
		

		List<Long> tweetListId=new ArrayList<Long>();
		List<String> tweetListText=new ArrayList<String>();
		
		List<Long> seeds=new ArrayList<Long>();
		JSONParser parser=new JSONParser();
		
		//Extracting tweets from the JSON file
		while(sc.hasNextLine()){
			
			String line=sc.nextLine();
			Object obj=parser.parse(line);
			JSONObject jsonObject = (JSONObject) obj;
			Long id = (Long) jsonObject.get("id");
			String text = (String) jsonObject.get("text");
			tweetListId.add(id);
			tweetListText.add(text);
		}
		sc.close();
		
		long [] idTweet=new long[tweetListId.size()];
		for(int p=0;p<tweetListId.size();p++){
			idTweet[p]=tweetListId.get(p).longValue();
		}
		
		String textTweet[]=tweetListText.toArray(new String[tweetListText.size()]);
		int clusterNo[]=new int[tweetListText.size()];
		
		System.out.println("kMeans Algorithm for clustering similar Tweets together");
		System.out.println("Initializing...");
		
		//Extracting initial seeds from the csv file
				List<String> id=new ArrayList<String>();
				while(initial.hasNextLine()){
					String line=initial.nextLine();
					id.addAll(Arrays.asList(line.split(",")));
				}
				initial.close();
				
				for(String s:id){					
					Long seed=Long.valueOf(s);
					seeds.add(seed);
				}
				
		long seedArray[]=new long[seeds.size()];
		for(int p=0;p<seeds.size();p++){
			seedArray[p]=seeds.get(p).longValue();
		}

		TweetsKMeans object=new TweetsKMeans();	
		Tweets newObject=new Tweets();
		
		System.out.println("Setting initial seeds as cluster points...");
		Tweets centers[]=new Tweets[Integer.valueOf(numberOfClusters)];
		centers=object.assignClusterIds(seedArray,idTweet,textTweet,clusterNo,centers,newObject);

		System.out.println("Assigning Tweets to the clusters nearest to them...");
		List<Tweets> getClusters=new ArrayList<Tweets>();
		getClusters=object.clusterTweets(centers,idTweet,textTweet,clusterNo,newObject);
		int iteration=0;
		Tweets newCentroids[]=new Tweets[centers.length];
		List<Tweets> newCluster= new ArrayList<Tweets>();
		
		while(iteration<Integer.parseInt(numberOfClusters)){
			
			System.out.println("Calculating New Centroids for Each Cluster...");
			newCentroids=object.computeNewClusterCentroids(getClusters,centers.length); 
			
			System.out.println("Reclustering Tweets...");
			newCluster=object.recluster(newCentroids,getClusters,newObject);			
			
			if(getClusters.equals(newCluster)){
				System.out.println("Convergence after " + iteration + " iterations.");
				break;
			}
			
		getClusters=newCluster;	
		iteration++;
		}
		
		System.out.println("Validating the goodness of the clustering...");
		double sse=0;
		sse=object.validate(numberOfClusters,newCluster,sse,newCentroids);
		System.out.println("SSE = "+ sse);
		
		PrintWriter writer = new PrintWriter(new File(outputFile));
		writer.println("Cluster ID" + "\t" + "\t" + "Point IDs");
		
		for(int i=0;i<newCentroids.length;i++){
			
			List<Long> points = new ArrayList<Long>();
			
			for(int j=0;j<newCluster.size();j++){
				
				if(newCluster.get(j).clusterId==(i+1)){
					
					points.add(newCluster.get(j).id);
				}	
			}
			
			writer.print(i+1 + "\t"  + "\t");
			
			for(long p6:points){
				writer.print(p6 + " , ");
				
			}
			
			writer.println();
			writer.println();
		}
		
		writer.close();	
	}
	
	

	public Tweets[] assignClusterIds(long[] seedArray, long[] id,String[] text,int[] number,Tweets[] cluster,Tweets obj) {
		
		int len=0;
		while(len<seedArray.length){
			
			for(int i=0;i<id.length;i++){
				
				if(seedArray[len]==id[i]){
					cluster[len]=obj.set(id[i], text[i], len+1);
				}
			}
			
			len++;
		}
		
		return cluster;
	}
	
	public List<Tweets> clusterTweets(Tweets[] centers, long[] idTweet, String[] textTweet, int[] clusterNo, Tweets newObject){
		double distance=0;
		double intersectionSize=0;
		double unionSize=0;
		List<Tweets> cluster=new ArrayList<Tweets>();
		
		for(int i=0;i<textTweet.length;i++){
			
			List<String> list1=Arrays.asList(textTweet[i].split("(?=[,.])|\\s+"));
			List<Double>min=new ArrayList<Double>();
			for(int j=0;j<centers.length;j++){
				
				List<String> list2=Arrays.asList(centers[j].text.split("(?=[,.])|\\s+"));
				List<String> intersection=new ArrayList<String>(list1);
				List<String> union=new ArrayList<String>(list1);
				
				intersection.retainAll(list2);
				union.addAll(list2);
				intersectionSize=intersection.size();
				unionSize=union.size();
				
				distance=1-(intersectionSize/unionSize);
				min.add(distance);
			}
			Double minimum=Collections.min(min);
			int index=min.indexOf(minimum);
			newObject=newObject.set(idTweet[i], textTweet[i], index+1);
			cluster.add(newObject);
			
		}
		
		return cluster;		
	}
	
	public Tweets[] computeNewClusterCentroids(List<Tweets> getClusters, int length) {
			
		List <Tweets> cluster = new ArrayList<Tweets>();
		Tweets anotherObject=new Tweets();
		Tweets centroids[]=new Tweets[length];
		double intersectionSize=0;
		double unionSize=0;
		double distance=0;
		int index=0;
		int i=0;
		
	
		
		
		while(i<length){
			
			for(Tweets t:getClusters){
				
				if(t.clusterId==i+1){
					
					cluster.add(t);						
					
				}
			
			}
			
			for(int p=0;p<cluster.size();p++){
				
				List<Double> minDist=new ArrayList<Double>();
				List <String> list1=Arrays.asList(cluster.get(p).text.split("(?=[,.])|\\s+"));
				
				for(int q=0;q<cluster.size();q++){
					
					if(q==p)
						continue;
					
					List <String> list2=Arrays.asList(cluster.get(q).text.split("(?=[,.])|\\s+"));
					
					List<String> intersection=new ArrayList<String>(list1);
					List<String> union=new ArrayList<String>(list1);
					
					intersection.retainAll(list2);
					union.addAll(list2);
					intersectionSize = intersection.size();
					unionSize = union.size();
					
					distance=1-(intersectionSize/unionSize);
					minDist.add(distance);						
					
				}					
				
				double minimum=Collections.min(minDist);
				index=minDist.indexOf(minimum);
				
								
			}
			
			centroids[i]=anotherObject.set(cluster.get(index).id,cluster.get(index).text,i+1);
			i++;		
		}
				
			
		return centroids;
	} 	
	
	
	
	public List<Tweets> recluster(Tweets [] centers,List<Tweets> allClusters,Tweets object) {
		
		double distance=0;
		double intersectionSize=0;
		double unionSize=0;
		List<Tweets> cluster=new ArrayList<Tweets>();
		
		for(int i=0;i<allClusters.size();i++){
			
			List<String> list1=Arrays.asList(allClusters.get(i).text.split("(?=[,.])|\\s+"));
			List<Double>min=new ArrayList<Double>();
			for(int j=0;j<centers.length;j++){
				
				List<String> list2=Arrays.asList(centers[j].text.split("(?=[,.])|\\s+"));
				List<String> intersection=new ArrayList<String>(list1);
				List<String> union=new ArrayList<String>(list1);
				
				intersection.retainAll(list2);
				union.addAll(list2);
				intersectionSize=intersection.size();
				unionSize=union.size();
				
				distance=1-(intersectionSize/unionSize);
				min.add(distance);
			}
			Double minimum=Collections.min(min);
			int index=min.indexOf(minimum);
			object=object.set(allClusters.get(i).id,allClusters.get(i).text, index+1);
			cluster.add(object);
			
		}
		
		return cluster;	
		
	} 
	
	public double validate(String numberOfClusters, List<Tweets> newCluster, double sse, Tweets[] newCentroids) {
		
		int noOfClusters = Integer.parseInt(numberOfClusters);
		double distance=0;
		double squareDistance=0;
		double intersectionSize=0;
		double unionSize=0;
		double sum[]=new double[noOfClusters];
		
		for(int i=0;i<noOfClusters;i++){
			
			List<String> list1=Arrays.asList(newCentroids[i].text.split("(?=[,.])|\\s+"));
			for(Tweets t:newCluster){
				
				
				
				if(t.clusterId==(i+1)){
					
					List<String> list2=Arrays.asList(t.text.split("(?=[,.])|\\s+"));
					List<String> intersection=new ArrayList<String>(list1);
					List<String> union=new ArrayList<String>(list1);
					
					intersection.retainAll(list2);
					union.addAll(list2);
					intersectionSize=intersection.size();
					unionSize=union.size();
					
					distance=1-(intersectionSize/unionSize);
					squareDistance=Math.pow(distance, 2);
					sum[i]=sum[i] + squareDistance;
				}				
			}		
		}
		
		for(double s:sum){
			sse=sse+s;
		}
	
		return sse;
	}

}

class Tweets{
	Long id;
	String text="";
	int clusterId;
	
	public Tweets setObjects(Long i, String t){
		Tweets t1=new Tweets();
		t1.id=i;
		t1.text=t;
		return t1;
	}


	public Tweets set(Long id,String text,int cluster){
		Tweets t3=new Tweets();
		t3.id=id;
		t3.text=text;
		t3.clusterId=cluster;		
		return t3;
	}
	
}
