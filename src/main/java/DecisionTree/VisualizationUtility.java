package DecisionTree;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class VisualizationUtility {

    private VisualizationUtility(){}


    public static void showROCCurve(double[] tpr, double[] fpr, String title) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("ROC Curve");

        for (int i = 0; i < tpr.length; i++) {
            series.add(fpr[i], tpr[i]);
        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "False Positive Rate",
            "True Positive Rate",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

        displayChart(chart, title);
    }

    public static void showPRCurve(double[] precision, double[] recall, String title) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("PR Curve");

        for (int i = 0; i < precision.length; i++) {
            series.add(recall[i], precision[i]);
        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "Recall",
            "Precision",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

        displayChart(chart, title);
    }

    public static double[][] getRocAuc(int[] yTest, int[] yPred) {
        Integer[] indices = new Integer[yPred.length];
        for (int i = 0; i < yPred.length; i++) {
            indices[i] = i;
        }

        // Сортировка индексов по значениям yPred
        Arrays.sort(indices, Comparator.comparingDouble(i -> -yPred[i]));

        int numPositive = 0;
        int numNegative = 0;
        for (int i : yTest) {
            if (i == 1) numPositive++;
            else numNegative++;
        }

        double[] tpr = new double[yTest.length + 1];
        double[] fpr = new double[yTest.length + 1];

        int tp = 0;
        int fp = 0;

        for (int i = 0; i < yTest.length; i++) {
            if (yTest[indices[i]] == 1) {
                tp++;
            } else {
                fp++;
            }
            tpr[i + 1] = (double) tp / numPositive;
            fpr[i + 1] = (double) fp / numNegative;
        }

        // Вычисление ROC AUC используя метод трапеций
        double rocAuc = 0.0;
        for (int i = 1; i < tpr.length; i++) {
            rocAuc += 0.5 * (fpr[i] - fpr[i - 1]) * (tpr[i] + tpr[i - 1]);
        }

        return new double[][]{fpr, tpr, new double[]{rocAuc}};
    }

    public static double[][] getPrAuc(int[] yTest, int[] yPred) {
        // Сортировка индексов
        Integer[] indices = new Integer[yPred.length];
        for (int i = 0; i < yPred.length; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, Comparator.comparingDouble(i -> -yPred[i]));

        double[] precision = new double[yTest.length + 1];
        double[] recall = new double[yTest.length + 1];

        int tp = 0;
        int fp = 0;
        int totalPositives = 0;
        for (int y : yTest) {
            if (y == 1) totalPositives++;
        }

        for (int i = 0; i < yTest.length; i++) {
            if (yTest[indices[i]] == 1) {
                tp++;
            } else {
                fp++;
            }
            precision[i + 1] = (double) tp / (tp + fp);
            recall[i + 1] = (double) tp / totalPositives;
        }

        // Вычисление PR AUC
        double prAuc = 0.0;
        for (int i = 1; i < precision.length; i++) {
            prAuc += 0.5 * (recall[i] - recall[i - 1]) * (precision[i] + precision[i - 1]);
        }

        return new double[][]{precision, recall, new double[]{prAuc}};
    }

    private static void displayChart(JFreeChart chart, String title) {
        JPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}

