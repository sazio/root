/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticfit;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * @author: Simone 
 * email:simone.azeglio@gmail.com
 */

public class GeneratoreGrafico extends ApplicationFrame {

    private final Vector<String> equazioni;
    private static XYSeriesCollection collezione;

    
    GeneratoreGrafico(Vector<String> equazioni) {
        super(null);
        this.equazioni = equazioni;
    }
    
    public void disegnaGraficoDinamico(){        
        disegnaGraficoDinamicoImpl();        
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    public void disegnaGrafico() {        
        XYSeriesCollection collezione = new XYSeriesCollection();
        collezione.addSeries(creaDataSet("Seno", 0, Math.PI, 0.01, x -> Math.sin(x)));
        
        JFreeChart grafico = ChartFactory.createXYLineChart(
                    "Grafico di funzione", "variabile x", "variabile y", collezione, PlotOrientation.VERTICAL, true, true, false);
        
        ChartFrame finestradisplay = new ChartFrame("Finestra per i grafici", grafico);
        finestradisplay.setVisible(true);
        finestradisplay.repaint();  // pack();// impacchetta la finestra per rendere visibile il grafico
        
        int index = 0;
        for (String equazione : equazioni) {
            index++;
            // Modifico X1 in x in modo da passarlo direttamente a JFreeChart senza problemi 
            String regex = "X1", replacement = "x", str1;
            str1 = equazione.replaceAll(regex, replacement);
            collezione. addSeries(creaDataSet("GeneticPolynomial_".concat(String.valueOf(index)), 0, Math.PI, 0.01, x -> getFunction(str1, x)));
            grafico = ChartFactory.createXYLineChart(
                    "Grafico di funzione", "variabile x", "variabile y", collezione, PlotOrientation.VERTICAL, true, true, false);
        }   

    }

    public static XYSeries creaDataSet(String nomeSerie,
            double da, double a, double dx, Function funzione) {

        XYSeries dataset = new XYSeries(nomeSerie);
        for (double x = da; x < a; x += dx) {
            dataset.add(x, funzione.f(x));
        }

        return dataset;
    }

    private static Double getFunction(String str1, Double x) {
        final ExpressionBuilder expressionBuilder = new ExpressionBuilder(str1);
        expressionBuilder.variable("x");
        net.objecthunter.exp4j.Expression expression = expressionBuilder.build().setVariable("x", x);
        final Double result = expression.evaluate();
        return result;
    }

    private void disegnaGraficoDinamicoImpl() {
        final JPanel chartPanel = createPanel();
        // chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    public JPanel createPanel() {
        String regex = "X1", replacement = "x", str1;
        collezione = new XYSeriesCollection();
        XYSeries dataSetSeno = creaDataSet("Seno", 0, Math.PI, 0.01, x -> Math.sin(x));
        collezione.addSeries(dataSetSeno);
        int drawIndex = 0;
        for (String equazione : equazioni) {
            drawIndex++;
            collezione.addSeries(creaDataSet("GeneticPolynomial_".concat(String.valueOf(drawIndex)), 0, Math.PI, 0.01, (double x1) -> getFunction(equazione.replaceAll(regex, replacement), x1)));
        }
        
        JFreeChart chart = createChart(collezione);
        
        JPanel panel = new JPanel(new BorderLayout());
        JSlider slider = new JSlider(0, equazioni.size()-1 , 0);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(2);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.addChangeListener((ChangeEvent e) -> {
            JSlider s = (JSlider) e.getSource();
            int sliderIndex = s.getValue();
            System.out.println("slider.value: "+s.getValue());
            if (sliderIndex==0) {
                collezione.removeAllSeries();
                collezione.addSeries(dataSetSeno);
                int drawIndexInternal = 0;
                for (String equazione : equazioni) {
                    drawIndexInternal++;
                    collezione.addSeries(creaDataSet("GeneticPolynomial_".concat(String.valueOf(drawIndexInternal)), 0, Math.PI, 0.01, (double x1) -> getFunction(equazione.replaceAll(regex, replacement), x1)));
                }
            } else {
                final String equazione = equazioni.get(s.getValue()).replaceAll(regex, replacement);
                collezione.removeAllSeries();
                collezione.addSeries(dataSetSeno);
                collezione.addSeries(creaDataSet("GeneticPolynomial_".concat(String.valueOf(s.getValue())), 0, Math.PI, 0.01, (double x2) -> getFunction(equazione, x2)));
            }
        });
        panel.add(new ChartPanel(chart));
        panel.add(BorderLayout.SOUTH, slider);
        return panel;
    }
    
    private static JFreeChart createChart(XYSeriesCollection collezione) {
        JFreeChart grafico = ChartFactory.createXYLineChart("Grafico di funzione", "variabile x", "variabile y", collezione, PlotOrientation.VERTICAL, true, true, false);
        return grafico;
    }

}
