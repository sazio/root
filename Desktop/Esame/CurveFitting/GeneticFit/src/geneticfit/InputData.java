/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticfit;

import java.util.Arrays;

/**
 * @author: Simone 
 * email:simone.azeglio@gmail.com
 */
public class InputData {

    private Integer varNumber;
    private Integer randomNumber;
    private Double minRandom;
    private Double maxRandom;
    private Integer fitnessCases;
    private double[][] valuesXY;

    public Integer getVarNumber() {
        return varNumber;
    }

    public void setVarNumber(Integer varNumber) {
        this.varNumber = varNumber;
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Integer randomNumber) {
        this.randomNumber = randomNumber;
    }

    public Double getMinRandom() {
        return minRandom;
    }

    public void setMinRandom(Double minRandom) {
        this.minRandom = minRandom;
    }

    public Double getMaxRandom() {
        return maxRandom;
    }

    public void setMaxRandom(Double maxRandom) {
        this.maxRandom = maxRandom;
    }

    public Integer getFitnessCases() {
        return fitnessCases;
    }

    public void setFitnessCases(Integer fitnessCases) {
        this.fitnessCases = fitnessCases;
    }

    double[][] getValuesXY() {
        return this.valuesXY;
    }
     
    void setValuesXY(double[][] valuesXY) {
        this.valuesXY = valuesXY;
    }

    @Override
    public String toString() {
        return "InputData{" + "varNumber=" + varNumber + ", randomNumber=" + randomNumber + ", minRandom=" + minRandom + ", maxRandom=" + maxRandom + ", fitnessCases=" + fitnessCases + '}';
    }
    
    public String toStringValuesXY() {
        return Arrays.deepToString(valuesXY);
    }
    

}
