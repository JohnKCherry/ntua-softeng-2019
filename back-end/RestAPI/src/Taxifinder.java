import java.util.ArrayList;

public class Taxifinder {

    public static void main(String[] args) throws Throwable {

        System.out.println("Taxi-Finder App Start!");

        /* File Names & Paths */

        String CSV_Path = "/home/evanraft/NetBeansProjects/TaxiFinder/test/";

        String nodes_file = "nodes.csv";
        String taxi_file = "taxis.csv";
        String client_file = "client.csv";

        /* Parsing Files */

        inputFromCSV nodes_pr = new inputFromCSV(CSV_Path + nodes_file);
        inputFromCSV taxis_pr = new inputFromCSV(CSV_Path + taxi_file);
        inputFromCSV client_pr = new inputFromCSV(CSV_Path + client_file);

        
        
        /* Test Messages

        nodes_pr.testMessage();
        taxis_pr.testMessage();
        client_pr.testMessage();


        */

        /* Making the Nodes of the Map from parsed data */

        System.out.println("Loading Map ...");

        NodeMap newNodeMap = new NodeMap(nodes_pr);
        Graph myMap = new Graph(newNodeMap);

        //myMap.print_graph();

        /* Test Graph and Intersections

        myMap.print_graph();
        myMap.print_intersections();


        */

        NodeMap taxisNodes = new NodeMap(taxis_pr);
        Node clientNode = new Node(client_pr.lastLine()[0],client_pr.lastLine()[1], "-1");


        Node finishNode = clientNode.nearestNode(newNodeMap);
        Astar aStarNavigation = new Astar(finishNode, myMap);

        ArrayList<Node> startNodes = new ArrayList<Node>();
        for(Node temp : taxisNodes.getNodeMap()) startNodes.add(temp.nearestNode(newNodeMap));

        System.out.println("Edo den kolla sigoyra");

        Result res = aStarNavigation.start(startNodes.get(0));
        
        res.printPath();
        //for(Node temp: startNodes) aStarNavigation.start(temp);
        
        
    }
}
