package sim;
import java.util.ArrayList;
import model.*;

public class Simulator{
    private int numThreads;
    private ArrayList<Worker> workers;
    private int numPlayers;
    private Board knownBoard;
    private Player hero;
    private int numSims;
    private int numWins;
    private int numLoss;
    private int numTies;
    

    public Simulator(int numSims, int numPlayers, Player hero, Board knownBoard){
        workers = new ArrayList<>();
        this.numWins = 0;
        this.numLoss = 0;
        this.numTies = 0;
        this.numSims = numSims;
        this.numPlayers = numPlayers;
        this.knownBoard = knownBoard;
        this.hero = hero;
    }

    public void simulate(){
        this.numThreads = Runtime.getRuntime().availableProcessors();
        ArrayList<Thread> threadCol = new ArrayList<>();
        

        for(int i = 0; i < numThreads; i++){
            Worker worker = new Worker(this.numSims/this.numThreads, this.numPlayers, this.hero, this.knownBoard);
            this.workers.add(worker);
            Thread thread = new Thread(worker);
            thread.start();
            threadCol.add(thread);
        }

        for(int i = 0; i < numThreads; i++){
            try{
                threadCol.get(i).join();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

        for(int i = 0; i < this.workers.size(); i++){
            this.numTies += this.workers.get(i).getTies();
            this.numLoss += this.workers.get(i).getLosses();
            this.numWins += this.workers.get(i).getWins();
        }
    }

    public int getWins(){
        return this.numWins;
    }

    public int getTies(){
        return this.numTies;
    }

    public int getLosses(){
        return this.numLoss;
    }
}