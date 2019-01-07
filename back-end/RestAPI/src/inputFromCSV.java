import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class inputFromCSV {

    /**
     * Class for parsing CSV and transforming
     * the input to ArrayList of Strings
     * for easier manipulation.
     */


    private ArrayList<String []> data =  new ArrayList<String []>();


    public ArrayList<String[]> getData() {
        return data;
    }

    public String[] lastLine(){
        return this.data.get(this.data.size() - 1);
    }

    /**
     * The constructor
     */

    public inputFromCSV(String fileName){

        String line;
        String[] aLine;
        try{

            /* Opening CSV */

            FileReader fd = new FileReader(fileName);
            try (BufferedReader br = new BufferedReader(fd)) {
                br.readLine(); // The first line is the header
                while( ( line = br.readLine() ) != null){
                    aLine = line.split(",");

                    /* Test Message

                    System.out.println(aLine[0] + "---->" + aLine[1] + "---->" + aLine[2]);

                    */

                    data.add(aLine);
                }

            }
            catch(IOException ex){
                System.out.println("Oops! Something's wrong!");
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("Please Check if the path is correct! Is it? :)");
            System.exit(0);
        }

    }

    /**
     *  An auxiliary method for testing
     *  the class output.
     */

    public void testMessage(){
        for(String[] line : data)
            if((line.length) > 2) System.out.println(line[0] + " | " + line[1] + " | " + line[2]);
            else System.out.println(line[0] + " | " + line[1]);
    }

}
