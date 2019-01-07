import java.util.ArrayList;

public class Result {
	
	private ArrayList<Double> distance;
	private ArrayList<Path> finalPaths;


	public Result(){
		distance = new ArrayList<Double>();
		finalPaths = new ArrayList<Path>();
	}

	public void addResult(Path finalPath, double dist){
		this.distance.add(dist);
		this.finalPaths.add(finalPath);
	}
        
        public void printPath(){
            for(int i=0; i<distance.size(); ++i){
                System.out.print(distance.get(i) + " : ");
                for(String p : finalPaths.get(i).getSuccessors())
                    System.out.print("-->" + p + " ");
                System.out.println("");
            }
        }


}
