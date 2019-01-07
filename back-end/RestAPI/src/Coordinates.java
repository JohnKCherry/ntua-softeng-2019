public class Coordinates {

    /**
     * This class exists in order to
     * 1. casting the coordinates
     * 2. representation of location features (latitude, longitude)
     */

    private double x;
    private double y;

    public Coordinates(String x, String y){
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
    }


    /**
     * Getters
     *
     */

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * toString
     *
     */

    @Override
    public String toString(){
        return (x + "," + y);
    }

}
