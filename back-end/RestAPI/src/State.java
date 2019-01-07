import java.util.Comparator;

public class State {

    private Coordinates stateCoords;
    private Path statePath;
    private double gValue;
    private double hValue;
    private double fValue;

    public State(Coordinates stateCoords, double gValue, double hValue){

        this.stateCoords = stateCoords;
        statePath = new Path();
        
        this.gValue = gValue;
        this.hValue = hValue;
        this.fValue = gValue + hValue;
    }

    public State(Coordinates stateCoords, State successor, double gValue, double hValue){

        this.stateCoords = stateCoords;

        statePath = successor.getStatePath().clone();
        statePath.addPath(successor);
        this.gValue = gValue;
        this.hValue = hValue;
        this.fValue = gValue + hValue;

    }

    public double getfValue() {
        return fValue;
    }

    public Path getStatePath() {
        return statePath;
    }

    public double gethValue() {
        return hValue;
    }

    public double getgValue() {
        return gValue;
    }

    public Coordinates getStateCoords() {
        return stateCoords;
    }

    public static Comparator<State> stateFvalue = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {

            double f1 = o1.getfValue();
            double f2 = o2.getfValue();

            return Double.compare(f1, f2);
        }
    };


    public void print() {
        System.out.print(stateCoords.toString() + " : "+
                        "h : " + hValue +
                        " g : " + gValue+
                        " f : " + fValue+ " Path :" );
        statePath.print();
        System.out.println();
    }
}
