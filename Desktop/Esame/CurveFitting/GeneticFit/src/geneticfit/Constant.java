
package geneticfit;


/**
 * @author: Simone 
 * email:simone.azeglio@gmail.com
 */
public class Constant {

    protected static final int ADD = 110;
    protected static final int SUB = 111;
    protected static final int MUL = 112;

    protected static final int FSET_START = ADD;
    protected static final int FSET_END = MUL;

    static final int MAX_LEN = 10000;
    static final int POPSIZE = 100000;
    static final int DEPTH = 5;
    static final int GENERATIONS = 100;
    static final int TSIZE = 2;

    // Mutation Probabilit    y
    static final double PMUT_PER_NODE = 0.05;
            //Crossover Probability 
    static final double CROSSOVER_PROB = 0.9;

}
