import java.util.ArrayList;

public class NodeMap {

    /**
     * A class which takes as input
     * the refined input and transform it
     * to a list of nodes.
     */


    private ArrayList<Node> NodeMap = new ArrayList<Node>();

    public ArrayList<Node> getNodeMap() {
        return NodeMap;
    }


    public  NodeMap(inputFromCSV parsed){

        for(String[] l : parsed.getData()){

            NodeMap.add(new Node(l[0],l[1],l[2]));

        }
    }

    /**
     *  An auxiliary method for testing
     *  the class output.
     */

    public void test_message(){
        for(Node n : NodeMap) System.out.println(n.toString());
    }



}
