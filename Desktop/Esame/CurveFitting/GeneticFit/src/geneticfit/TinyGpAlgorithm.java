package geneticfit;

/**
 * This is a version of TinyGP algorithm from Riccardo Poli, it has been modified by me 
 * In order to be used with JFreeChart 
 * email:simone.azeglio@gmail.com
 */

import java.util.Random;
import java.util.Vector;

public class TinyGpAlgorithm {

    double[] fitness;
    char[][] pop;

    public Vector<String> equazioni = new Vector<String>(5);

    public Vector<String> getEquazioni() {
        return equazioni;
    }
    StringBuffer sb;
    static Random rd = new Random();
    static double[] x = new double[Constant.FSET_START];
    static double minRandom, maxRandom;
    static char[] program;
    static int PC;
    static int varNumber, fitnessCases, randomNumber;
    static double fBestPop = 0.0, fAvgPop = 0.0;
    static long seed;
    static double avgLen;

    
    public static final double 
            PMUT_PER_NODE = 0.05, // Mutation Probability 
            CROSSOVER_PROB = 0.9; // Crossover Probability 
    
    
    static double[][] targets;

    public TinyGpAlgorithm(InputData inputData, long seedForRandomNumber) {
        varNumber = inputData.getVarNumber();
        randomNumber = inputData.getRandomNumber();
        minRandom = inputData.getMinRandom();
        maxRandom = inputData.getMaxRandom();
        fitnessCases = inputData.getFitnessCases();
        targets = inputData.getValuesXY();
        seed = seedForRandomNumber;
    }

    double run() {
        /* Interpreter */
        char primitive = program[PC++];
        if (primitive < Constant.FSET_START) {
            return (x[primitive]);
        }
        switch (primitive) {
            case Constant.ADD:
                return (run() + run());
            case Constant.SUB:
                return (run() - run());
            case Constant.MUL:
                return (run() * run());
        }
        return (0.0); // should never get here
    }

    int traverse(char[] buffer, int buffercount) {
        if (buffer[buffercount] < Constant.FSET_START) {
            return (++buffercount);
        }

        switch (buffer[buffercount]) {
            case Constant.ADD:
            case Constant.SUB:
            case Constant.MUL:
                return (traverse(buffer, traverse(buffer, ++buffercount)));
        }
        return (0); // should never get here
    }

    double fitnessFunction(char[] Prog) {
        double result, fit = 0.0;
        for (int i = 0; i < fitnessCases; i++) {
            for (int j = 0; j < varNumber; j++) {
                x[j] = targets[i][j];
            }
            program = Prog;
            PC = 0;
            result = run();
            fit += Math.abs(result - targets[i][varNumber]);
        }
        return (-fit);
    }

    // Initialize Population 
    int grow(char[] buffer, int pos, int max, int depth) {
        char prim = (char) rd.nextInt(2);
        int one_child;

        if (pos >= max) {
            return (-1);
        }
        if (pos == 0) {
            prim = 1;
        }
        if (prim == 0 || depth == 0) {
            prim = (char) rd.nextInt(varNumber + randomNumber);
            buffer[pos] = prim;
            return (pos + 1);
        } else {
            prim = (char) (rd.nextInt(Constant.FSET_END - Constant.FSET_START + 1) + Constant.FSET_START);
            switch (prim) {
                case Constant.ADD:
                case Constant.SUB:
                case Constant.MUL:
                    buffer[pos] = prim;
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0) {
                        return (-1);
                    }
                    return (grow(buffer, one_child, max, depth - 1));
            }
        }
        return (0); // should never get here
    }

    int printIndiv(char[] buffer, int buffercounter) {
        int a1 = 0, a2;
        if (buffer[buffercounter] < Constant.FSET_START) {
            if (buffer[buffercounter] < varNumber) {
                System.out.print("X" + (buffer[buffercounter] + 1) + " ");
            } else {
                System.out.print(x[buffer[buffercounter]]);
            }
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case Constant.ADD:
                System.out.print("(");
                a1 = printIndiv(buffer, ++buffercounter);
                System.out.print(" + ");
                break;
            case Constant.SUB:
                System.out.print("(");
                a1 = printIndiv(buffer, ++buffercounter);
                System.out.print(" - ");
                break;
            case Constant.MUL:
                System.out.print("(");
                a1 = printIndiv(buffer, ++buffercounter);
                System.out.print(" * ");
                break;
        }
        a2 = printIndiv(buffer, a1);
        System.out.print(")");
        return (a2);
    }

    int newPrintIndiv(char[] buffer, int buffercounter) {
        int a1 = 0, a2;
        if (buffer[buffercounter] < Constant.FSET_START) {
            if (buffer[buffercounter] < varNumber) {
                sb.append("X").append(buffer[buffercounter] + 1).append(" ");
            } else {
                sb.append(x[buffer[buffercounter]]);
            }
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case Constant.ADD:
                sb.append("(");
                a1 = newPrintIndiv(buffer, ++buffercounter);
                sb.append(" + ");
                break;
            case Constant.SUB:
                sb.append("(");
                a1 = newPrintIndiv(buffer, ++buffercounter);
                sb.append(" - ");
                break;
            case Constant.MUL:
                sb.append("(");
                a1 = newPrintIndiv(buffer, ++buffercounter);
                sb.append(" * ");
                break;
        }
        a2 = newPrintIndiv(buffer, a1);
        sb.append(")");
        return (a2);
    }

    static char[] buffer = new char[Constant.MAX_LEN];

    char[] createRandomIndiv(int depth) {
        char[] ind;
        int len = grow(buffer, 0, Constant.MAX_LEN, depth);
        while (len < 0) {
            len = grow(buffer, 0, Constant.MAX_LEN, depth);
        }
        ind = new char[len];
        System.arraycopy(buffer, 0, ind, 0, len);
        return (ind);
    }

    char[][] createRandomPop(int n, int depth, double[] fitness) {
        char[][] popInternal = new char[n][];
        for (int i = 0; i < n; i++) {
            popInternal[i] = createRandomIndiv(depth);
            fitness[i] = fitnessFunction(popInternal[i]);
        }
        return (popInternal);
    }

    void stats(double[] fitness, char[][] pop, int gen) {
        int i, best = rd.nextInt(Constant.POPSIZE);
        int node_count = 0;
        fBestPop = fitness[best];
        fAvgPop = 0.0;

        for (i = 0; i < Constant.POPSIZE; i++) {
            node_count += traverse(pop[i], 0);
            fAvgPop += fitness[i];
            if (fitness[i] > fBestPop) {
                best = i;
                fBestPop = fitness[i];
            }
        }
        avgLen = (double) node_count / Constant.POPSIZE;
        fAvgPop /= Constant.POPSIZE;
        System.out.print("Generation=" + gen + " Avg Fitness=" + (-fAvgPop)
                + " Best Fitness=" + (-fBestPop) + " Avg Size=" + avgLen
                + "\nBest Individual: ");
        sb = new StringBuffer();
        newPrintIndiv(pop[best], 0);
        equazioni.addElement(sb.toString());

        System.out.print("\n");
        System.out.flush();
    }

    int tournament(double[] fitness, int tsize) {
        int best = rd.nextInt(Constant.POPSIZE), i, competitor;
        double fbest = -1.0e34;

        for (i = 0; i < tsize; i++) {
            competitor = rd.nextInt(Constant.POPSIZE);
            if (fitness[competitor] > fbest) {
                fbest = fitness[competitor];
                best = competitor;
            }
        }
        return (best);
    }

    int negativeTournament(double[] fitness, int tsize) {
        int worst = rd.nextInt(Constant.POPSIZE), i, competitor;
        double fworst = 1e34;

        for (i = 0; i < tsize; i++) {
            competitor = rd.nextInt(Constant.POPSIZE);
            if (fitness[competitor] < fworst) {
                fworst = fitness[competitor];
                worst = competitor;
            }
        }
        return (worst);
    }

    char[] crossover(char[] parent1, char[] parent2) {
        int xo1start, xo1end, xo2start, xo2end;
        char[] offspring;
        int len1 = traverse(parent1, 0);
        int len2 = traverse(parent2, 0);
        int lenoff;

        xo1start = rd.nextInt(len1);
        xo1end = traverse(parent1, xo1start);

        xo2start = rd.nextInt(len2);
        xo2end = traverse(parent2, xo2start);

        lenoff = xo1start + (xo2end - xo2start) + (len1 - xo1end);

        offspring = new char[lenoff];

        System.arraycopy(parent1, 0, offspring, 0, xo1start);
        System.arraycopy(parent2, xo2start, offspring, xo1start,
                (xo2end - xo2start));
        System.arraycopy(parent1, xo1end, offspring,
                xo1start + (xo2end - xo2start),
                (len1 - xo1end));

        return (offspring);
    }

    char[] mutation(char[] parent, double pmut) {
        int len = traverse(parent, 0), i;
        int mutsite;
        char[] parentcopy = new char[len];

        System.arraycopy(parent, 0, parentcopy, 0, len);
        for (i = 0; i < len; i++) {
            if (rd.nextDouble() < pmut) {
                mutsite = i;
                if (parentcopy[mutsite] < Constant.FSET_START) {
                    parentcopy[mutsite] = (char) rd.nextInt(varNumber + randomNumber);
                } else {
                    switch (parentcopy[mutsite]) {
                        case Constant.ADD:
                        case Constant.SUB:
                        case Constant.MUL:
                            parentcopy[mutsite]
                                    = (char) (rd.nextInt(Constant.FSET_END - Constant.FSET_START + 1)
                                    + Constant.FSET_START);
                    }
                }
            }
        }
        return (parentcopy);
    }

    public void calcolaValori() {
        fitness = new double[Constant.POPSIZE];
        if (seed >= 0) {
            rd.setSeed(seed);
        }
        for (int i = 0; i < Constant.FSET_START; i++) {
            x[i] = (maxRandom - minRandom) * rd.nextDouble() + minRandom;
        }
        pop = createRandomPop(Constant.POPSIZE, Constant.DEPTH, fitness);
    }

    void evolve() {
        int gen, indivs, offspring, parent1, parent2, parent;
        double newfit;
        char[] newind;
        stats(fitness, pop, 0);
        double error = -6;//-1e-2;

        for (gen = 1; gen < Constant.GENERATIONS; gen++) {
            // fBestPop > error --> keep it little but not to much

            if (fBestPop > error) {
                System.out.print("PROBLEM SOLVED\n");
                return;//System.exit( 0 );
            }
            for (indivs = 0; indivs < Constant.POPSIZE; indivs++) {
                if (rd.nextDouble() < Constant.CROSSOVER_PROB) {
                    parent1 = tournament(fitness, Constant.TSIZE);
                    parent2 = tournament(fitness, Constant.TSIZE);
                    newind = crossover(pop[parent1], pop[parent2]);
                } else {
                    parent = tournament(fitness, Constant.TSIZE);
                    newind = mutation(pop[parent], Constant.PMUT_PER_NODE);
                }
                newfit = fitnessFunction(newind);
                offspring = negativeTournament(fitness, Constant.TSIZE);
                pop[offspring] = newind;
                fitness[offspring] = newfit;
            }
            stats(fitness, pop, gen);
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
    }

}
