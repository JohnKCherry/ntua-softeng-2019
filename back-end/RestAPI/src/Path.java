
import java.util.ArrayList;

public class Path {
	
	private ArrayList<String> successors;

	public Path(ArrayList<String> successors){
		this.successors = new ArrayList<String>();

	}

	public Path(){
		this.successors = new ArrayList<String>();
	}


	public ArrayList<String> getSuccessors() {
		return this.successors;
	}

	public void addPath(State successor) {
		/* Ignore cyclical routes */
		successors.add(successor.getStateCoords().toString());
		
	}


        private void setS(ArrayList<String> Successors){
            this.successors = Successors;
        }
        
	public Path clone(){
                Path temp = new Path();
                ArrayList<String> p = new ArrayList<String>();
		for(String s : this.successors){
                    p.add(s);
                }
                
                temp.setS(p);
                
                return temp;
	}

	public void print(){
		for(String s : this.successors) System.out.print("-->"  + s);
	}
        
        public boolean contains(String s){
            for(String p : this.successors)
                if(s.equals(p)){
                    System.err.println("Contained");
                    return true;
                }
            return false;
        }

}
