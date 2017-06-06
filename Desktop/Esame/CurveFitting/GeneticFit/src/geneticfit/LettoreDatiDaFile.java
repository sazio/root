/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticfit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author: Simone 
 * email:simone.azeglio@gmail.com
 */
public class LettoreDatiDaFile {

    public InputData getDatiDaFile(String fileName) {
        long timeStart = System.currentTimeMillis();
        System.out.println("Lettura dati di input da file " + fileName);
        final InputData inputData = new InputData();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(fileName));
            String line = in.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line);
            final Integer varNumber = Integer.parseInt(stringTokenizer.nextToken().trim());
            inputData.setVarNumber(varNumber);
            final Integer randomNumber = Integer.parseInt(stringTokenizer.nextToken().trim());
            inputData.setRandomNumber(randomNumber);
            final Double minRandom = Double.parseDouble(stringTokenizer.nextToken().trim());
            inputData.setMinRandom(minRandom);
            final Double maxRandom = Double.parseDouble(stringTokenizer.nextToken().trim());
            inputData.setMaxRandom(maxRandom);
            final Integer fitnessCases = Integer.parseInt(stringTokenizer.nextToken().trim());
            inputData.setFitnessCases(fitnessCases);

            final double[][] targets = new double[fitnessCases][varNumber + 1];
            if (varNumber + randomNumber >= Constant.FSET_START) {
                System.out.println("Too many variables and constants");
            }

            for (int x = 0; x < fitnessCases; x++) {
                line = in.readLine();
                stringTokenizer = new StringTokenizer(line);
                for (int y = 0; y <= varNumber; y++) {
                    targets[x][y] = Double.parseDouble(stringTokenizer.nextToken().trim());
                }
            }
            inputData.setValuesXY(targets);

        } catch (IOException ex) {
            Logger.getLogger(LettoreDatiDaFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(LettoreDatiDaFile.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        System.out.println("Lettura dati da file - tempo impiegato [ms]: "+(System.currentTimeMillis()-timeStart));
        System.out.println("Dati da file: "+inputData);
        System.out.println("Valori di x e y da file: "+inputData.toStringValuesXY());
        return inputData;
    }

}
