public class Edge {

    /**
     * This class represent the edge of the
     * graph.
     */

    private Node node;
    private double distance;


    public Edge(Node node, double distance) {
        this.node = node;
        this.distance = distance;
    }

    /**
     * Getters for Atributes
     */

    public Node getNode() {
        return node;
    }

    public double getDistance(){
        return distance;
    }
}
