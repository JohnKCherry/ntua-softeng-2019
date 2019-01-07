import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {

    private HashMap<String, ArrayList<Edge>> map;

    public ArrayList<Edge> getChildren(Coordinates coord){
        ArrayList<Edge> children = new ArrayList<Edge>();
        if(this.map.containsKey(coord.toString())) {
            children = this.map.get(coord.toString());
            //System.err.println(children.size());
        }
        else
            System.out.println("Malakia");

        return children;
    }

    public void addEdge(Node fromNode, Node toNode){

        double distance = fromNode.calculateDistance(toNode);

        addToMap(toNode, fromNode, distance);

        addToMap(fromNode, toNode, distance);
    }

    private void addToMap(Node aNode, Node bNode, double distance) {
        ArrayList<Edge> edges;

        if(this.map.containsKey(aNode.getXy_coord().toString()))
            edges = this.map.get(aNode.getXy_coord().toString());
        else
            edges = new ArrayList<Edge>();

        edges.add(new Edge(bNode, distance));
        this.map.put(aNode.getXy_coord().toString(),edges);
    }


    public Graph(NodeMap NMap) {

        this.map = new HashMap<String, ArrayList<Edge>>();

        int prevRoad = -1;
        Node prevNode = null;



        for (Node el : NMap.getNodeMap()) {


            if (prevRoad == -1) {
                prevNode = el;
                prevRoad = el.getId();
            }
            else {
                if (el.getId() == prevRoad)
                    addEdge(el, prevNode);
                else
                    prevRoad = el.getId();
                    prevNode = el;
            }
        }

    }

    public HashMap<String, ArrayList<Edge>> getMap() {
        return map;
    }

    public void print_graph(){
        for (Map.Entry<String, ArrayList<Edge>> entry : this.map.entrySet()) {
            String c = entry.getKey();
            ArrayList<Edge> E = entry.getValue();
            System.out.println(c.toString() + " : ");

            for (Edge e : E)
                System.out.println("\t"  + e.getNode().getId() + " with " + e.getDistance());
        }
    }


    public void print_intersections(){

        int cnt = 0;

        for( ArrayList<Edge> list : map.values()){
            Edge f = null;
            for(Edge e: list){
                if(f == null) f = e;
                if(f.getNode().getId() != e.getNode().getId()){
                    //System.out.println("Found");
                    cnt++;
                }
            }
        }
        System.out.println("There are " + cnt + " intersections");
    }
}