package sim;
import java.util.ArrayList;

public class Simulator{
    private int numThreads;
    private ArrayList<Worker> workers;

    public void simulate(){
        this.numThreads = Runtime.getRuntime().availableProcessors();
        for(int i = 0; i < numThreads; i++){
            
        }
    }
}