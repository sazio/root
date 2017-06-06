package geneticfit;

import java.util.Vector;


/**
 * @author: Simone 
 * email:simone.azeglio@gmail.com
 */
public class GeneticFitMain {

    public static void main(String[] args) {
        int seedForRandomNumber = -1;
        String fileName = "resources/problem.dat";
        
        if (args.length == 2) {
            seedForRandomNumber = Integer.valueOf(args[0]);
            fileName = args[1];
        } else if (args.length == 1) {
            fileName = args[0];
        }
        
        final InputData inputData = new LettoreDatiDaFile().getDatiDaFile(fileName);

        final TinyGpAlgorithm algoritmoTinyGp = new TinyGpAlgorithm(inputData, seedForRandomNumber);
        algoritmoTinyGp.calcolaValori();
        algoritmoTinyGp.evolve();
        
        final Vector<String> equazioni = algoritmoTinyGp.getEquazioni();
        
        final GeneratoreGrafico generatoreGrafico = new GeneratoreGrafico(equazioni);
        generatoreGrafico.disegnaGraficoDinamico();

    }

}
