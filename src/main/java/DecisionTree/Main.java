package DecisionTree;

import DecisionTree.data.DataProcessor;
import DecisionTree.data.SplitDataSet;

import java.util.HashMap;
import java.util.Map;

import static DecisionTree.VisualizationUtility.*;

public class Main {

    public static void main(String[] args) {
        DataProcessor processor = new DataProcessor();
        int[][] rawData = processor.loadCSV("/Users/komarzz/Учёба/Системы искуственного интелекта/Лаба 6/DecisionTree/DecisionTree/src/main/java/DecisionTree/data/dataStorage/DATA.csv");

        double trainRatio = 0.8;

        // Разделение данных на обучающую и тестовую выборки
        SplitDataSet splitDataSet = processor.splitData(rawData, trainRatio);

        // Извлечение обучающих и тестовых данных
        int[][] trainX = splitDataSet.trainX();
        int[] trainY = splitDataSet.trainY();
        int[][] testX = splitDataSet.testX();
        int[] testY = splitDataSet.testY();

        // Обучение дерева решений на обучающей выборке
        DecisionTree tree = new DecisionTree();
        tree.fit(trainX, trainY);

        int[] predictions = tree.predict(testX);

        for (int str: predictions) {
            System.out.println(str);
        }

        System.out.println("\n\n\n\n");

        for (int str: testY) {
            System.out.println(str);
        }

        Map<String, Integer> cm = confMatrix(testY, predictions);
        printMetrics(cm);

        double[][] rocResults = getRocAuc(testY, predictions);
        double[] fpr = rocResults[0];
        double[] tpr = rocResults[1];
        double rocAuc = rocResults[2][0];

        System.out.println("ROC AUC: " + rocAuc);
        showROCCurve(fpr, tpr, "ROC Curve");


        double[][] prResults = getPrAuc(testY, predictions);
        double[] precision = prResults[0];
        double[] recall = prResults[1];
        double prAuc = prResults[2][0];
        System.out.println("PR AUC: " + prAuc);
        showPRCurve(precision, recall, "Precision-Recall Curve");


    }

    private static Map<String, Integer> confMatrix(int[] yTest, int[] yPred) {
        Map<String, Integer> cm = new HashMap<>();
        cm.put("TP", 0);
        cm.put("TN", 0);
        cm.put("FP", 0);
        cm.put("FN", 0);

        for (int i = 0; i < yTest.length; i++) {
            if (yTest[i] == 1 && yPred[i] == 1) cm.put("TP", cm.get("TP") + 1);
            else if (yTest[i] == 0 && yPred[i] == 0) cm.put("TN", cm.get("TN") + 1);
            else if (yTest[i] == 0 && yPred[i] == 1) cm.put("FP", cm.get("FP") + 1);
            else if (yTest[i] == 1 && yPred[i] == 0) cm.put("FN", cm.get("FN") + 1);
        }
        return cm;
    }

    private static void printMetrics(Map<String, Integer> cm) {
        double total = cm.get("TP") + cm.get("TN") + cm.get("FP") + cm.get("FN");
        double acc = total > 0 ? (cm.get("TP") + cm.get("TN")) / total : 0;
        double precision = cm.get("TP") + cm.get("FP") > 0 ? cm.get("TP") / (double) (cm.get("TP") + cm.get("FP")) : 0;
        double recall = cm.get("TP") + cm.get("FN") > 0 ? cm.get("TP") / (double) (cm.get("TP") + cm.get("FN")) : 0;
        System.out.printf("Accuracy: %.2f\nPrecision: %.2f\nRecall: %.2f\n", acc, precision, recall);
    }
}


