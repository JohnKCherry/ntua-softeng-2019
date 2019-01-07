public class Node {

    /**
     * This class represents the nodes
     * of the map. In this class, there are
     * defined useful and interesting methods
     * which have significant meaning for the
     * nodes and the relations with other ones.
     */

    private Coordinates xy_coord;
    private int id;

    public Node(String x, String y, String id) {
        this.xy_coord = new Coordinates(x,y);
            this.id = Integer.parseInt(id);
    }

    /**
     * The below methods calculate the
     * distance between two nodes, using
     * the Haversine distance.
     */

    private double Haversine(double latt, double longt){

        double phi_1 = Math.toRadians(latt);
        double phi_2 = Math.toRadians(this.xy_coord.getY());
        double df = Math.toRadians(this.xy_coord.getY() - latt);
        double dl = Math.toRadians(this.xy_coord.getX() - longt);

        double a = Math.sin(df/2) * Math.sin(df/2) +
                + Math.cos(phi_1) * Math.cos(phi_2) *
                 Math.sin(dl/2) * Math.sin(dl/2);

        return a;

    }

    public double calculateDistance(Node a){

        final int earthRadius = 6371;

        double alpha = Haversine(a.getY(), a.getX());

        double c = 2 * Math.atan2(Math.sqrt(alpha), Math.sqrt((1-alpha)));

        return Math.abs((Math.round(earthRadius * c * 100000))/100.0);

    }

    public Node nearestNode(NodeMap map){

        double min = Double.MAX_VALUE;
        Node minNode = null;

        for(Node n : map.getNodeMap()){

            if(minNode == null){
                minNode = n;
                min = this.calculateDistance(minNode);
            }

            if(this.calculateDistance(n) < min) {
                minNode = n;
                min = this.calculateDistance(minNode);
            }
        }

        /*
        System.out.println(this.getXy_coord().toString() + " - " 
        					+ minNode.getXy_coord().toString() 
        					+ " = "  + min);
        */
        
        return minNode;

    }

    /**
     * Getters and toString Methods
     */

    public Coordinates getXy_coord(){
        return xy_coord;
    }

    public double getX(){
        return xy_coord.getX();
    }

    public double getY(){
        return xy_coord.getY();
    }

    public int getId() {
        return id;
    }

    @Override
    public  String toString(){
        return ("X: " + this.getX() + " Y: " + this.getY() + " ID: " + this.id);
    }
}
