import java.util.ArrayList;
import java.util.Scanner;

public class Astar {

	private Coordinates finishCoords;
	private Node finishNode;
	private Graph myGraph;

	public Astar(Node finishNode, Graph myGraph){
		this.finishNode = finishNode;
		this.finishCoords = finishNode.getXy_coord();
		this.myGraph = myGraph;
	}
	
	public Result start(Node startNode){
		
		Result res = new Result();
		Coordinates startNodeCoords = startNode.getXy_coord();


		searchFrontier sFrontier = new searchFrontier();

		State currState;

		sFrontier.add(new State(startNodeCoords, 0.0, startNode.calculateDistance(finishNode)));

                
                
               
                Scanner in = new Scanner(System.in);
                
                
                sFrontier.printSFrontier();
                
                while(!in.hasNext());
                in.next();
                
		int cnt = 0;

		while(!sFrontier.isEmpty()){

			currState = sFrontier.getNewCurrentState();
			Coordinates coords = currState.getStateCoords();

			//System.out.println(currState.gethValue());
			//currState.print();
			//if(cnt++ == 1) System.exit(-1);

			if(coords.toString().equals(finishCoords.toString())) {
				System.out.println("Kai A kai Ou kai Falakros Guru!");
				res.addResult(currState.getStatePath(), currState.getfValue());
				break;
			}
                        
                        ArrayList<Edge> adjList = myGraph.getChildren(coords);
                        /*System.out.println(coords.toString() + " : ");
                        for(Edge v : adjList) System.out.print(v.getNode().toString() + " ");
                        System.out.println();
                        //for (Edge v: adjList) System.out.println(v.getNode().toString());
                        //if(cnt++ == 2) System.exit(-1);*/
			State newEntry;

			for(Edge v : adjList){

                                        //System.out.println("--> " + v.getNode().getXy_coord());
					double h = v.getNode().calculateDistance(finishNode);
					double g = v.getDistance() + currState.getgValue();
                                        
                                        //System.err.println(g+h);

					newEntry = new State(v.getNode().getXy_coord(), currState, g, h);
                                        
                                        //System.err.println(newEntry.getfValue());
                                        
					
					sFrontier.add(newEntry);
			}
                        
                        
                        sFrontier.printSFrontier();
                        
                        while(!in.hasNext());
                        in.next();
                        
                        
			if(sFrontier.isEmpty()) System.out.print("No Valid Path");

                        
                        

		}
		
		return res;
		
	}
	
}
