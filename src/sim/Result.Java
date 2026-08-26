package sim;

public class Result{
    private int numWins;
    private int numLosses;
    private int numTie;
    private int totalSim;

    public Result(int nW, int nL, int nT, int tS){
        this.numWins = nW;
        this.numLosses = nL;
        this.numTie = nT;
        this.totalSim = tS;
    }

}