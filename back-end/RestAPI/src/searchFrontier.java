import java.util.ArrayList;
import java.util.Collections;

public class searchFrontier {

    private ArrayList<State> states;

    public searchFrontier(){
        this.states = new ArrayList<State>();
    }

    public void add(State st){
        
        if(st.getfValue() != -1.0){
            this.states.add(st);
            sortStates();
            //System.out.println("Mpike me f=" + st.getfValue() );
        }
        //for(State s : states) System.out.print(s.getfValue() + " ");
        //System.out.println();
    }

    public boolean isEmpty(){
        return  states.isEmpty();
    }

    private void sortStates(){
        Collections.sort(this.states, State.stateFvalue);
    }

    public State getNewCurrentState(){


        State a = this.states.get(0);
        //System.out.println(a.getStateCoords());
        //a.getStatePath().print();
        states.remove(0);

        //if(!states.isEmpty()) System.out.println(states.get(0).getStateCoords());

        return a;
    }
    
    public void printSFrontier(){
        System.out.println("{");
        for(State s : states){
            System.out.print("(< " + s.getStateCoords() + " >, " + s.gethValue() + " , " + s.getfValue() + " , [ ");
            for(String p : s.getStatePath().getSuccessors())
                System.out.print(p + ", ");
            System.out.println("])");
        }
        System.out.println("}");
    }

}

