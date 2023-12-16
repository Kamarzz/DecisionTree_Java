package DecisionTree;

import DecisionTree.data.Split;
import DecisionTree.data.TreeNode;

import java.util.*;

public class DecisionTree {
    private TreeNode root;

    public void fit(int[][] X, int[] y) {
        this.root = createTree(X, y);
    }

    private TreeNode createTree(int[][] X, int[] y) {

        try {
            if (X.length == 0 || isPure(y)) {
                return TreeNode.createLeafNode(calculateCommonValue(y)); // Используем фабричный метод для создания листового узла
            }

            int bestFeatureIndex = findBestFeature(X, y);

            TreeNode node = TreeNode.createInternalNode(bestFeatureIndex); // Используем фабричный метод для создания внутреннего узла
            Map<Integer, Split> splitGroups = splitData(X, y, bestFeatureIndex);

            for (Integer key : splitGroups.keySet()) {
                Split split = splitGroups.get(key);
                node.addChild(key, createTree(split.getX(), split.getY())); // Рекурсивно создаем дочерние узлы
            }

            return node;
        }catch (StackOverflowError e){
            System.out.println(e + "please retry");
        }
        return null;
    }



    private int findBestFeature(int[][] X, int[] y) {
        int bestFeatureIndex = -1;
        double bestInfoGain = Double.NEGATIVE_INFINITY;

        for (int featureIndex = 0; featureIndex < X[0].length; featureIndex++) {
            double infoGain = calculateInfoGain(X, y, featureIndex);
            if (infoGain > bestInfoGain) {
                bestInfoGain = infoGain;
                bestFeatureIndex = featureIndex;
            }
        }
        return bestFeatureIndex;
    }

    private double calculateInfoGain(int[][] X, int[] y, int featureIndex) {
        double totalEntropy = calculateEntropy(y);
        Map<Integer, List<Integer>> partitions = partitionByFeature(X, y, featureIndex);
        double weightedEntropySum = 0.0;

        for (List<Integer> subset : partitions.values()) {
            double subsetEntropy = calculateEntropy(subset.stream().mapToInt(i -> i).toArray());
            weightedEntropySum += ((double) subset.size() / y.length) * subsetEntropy;
        }

        return totalEntropy - weightedEntropySum;
    }


    private double calculateEntropy(int[] array) {
        Map<Integer, Integer> labelCounts = new HashMap<>();
        for (int label : array) {
            labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
        }

        double entropy = 0.0;
        for (int count : labelCounts.values()) {
            double probability = (double) count / array.length;
            entropy -= probability * Math.log(probability) / Math.log(2);
        }

        return entropy;
    }


    private Map<Integer, List<Integer>> partitionByFeature(int[][] X, int[] y, int featureIndex) {
        Map<Integer, List<Integer>> partitions = new HashMap<>();
        for (int i = 0; i < X.length; i++) {
            int key = X[i][featureIndex];
            partitions.computeIfAbsent(key, k -> new ArrayList<>()).add(y[i]);
        }
        return partitions;
    }



    private Map<Integer, Split> splitData(int[][] X, int[] y, int featureIndex) {
        Map<Integer, List<Integer>> indexMap = new HashMap<>();

        // Группировка индексов строк по уникальным значениям признака
        for (int i = 0; i < X.length; i++) {
            int key = X[i][featureIndex];
            indexMap.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
        }

        Map<Integer, Split> splitData = new HashMap<>();

        // Создание подмножеств данных для каждого уникального значения признака
        for (Map.Entry<Integer, List<Integer>> entry : indexMap.entrySet()) {
            List<Integer> indices = entry.getValue();
            int[][] subsetX = new int[indices.size()][X[0].length];
            int[] subsetY = new int[indices.size()];

            for (int i = 0; i < indices.size(); i++) {
                int rowIndex = indices.get(i);
                subsetX[i] = X[rowIndex];
                subsetY[i] = y[rowIndex];
            }

            splitData.put(entry.getKey(), new Split(subsetX, subsetY));
        }

        return splitData;
    }



    private boolean isPure(int[] y) {
        if (y.length == 0) {
            return true; // Пустой массив считаем "чистым"
        }

        int firstValue = y[0];
        for (int i = 1; i < y.length; i++) {
            if (firstValue != y[i]) {
                return false; // Найдено значение, отличное от первого
            }
        }
        return true; // Все значения одинаковы
    }



    private int calculateCommonValue(int[] y) {
        if (y.length == 0) {
            return Integer.MIN_VALUE; // В случае пустого массива возвращаем минимальное значение
        }

        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int label : y) {
            frequencyMap.put(label, frequencyMap.getOrDefault(label, 0) + 1);
        }

        int mostCommonValue = Integer.MIN_VALUE;
        int maxFrequency = -1;
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                mostCommonValue = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }

        return mostCommonValue;
    }

    public int[] predict(int[][] X) {
        int[] predictions = new int[X.length];
        for (int i = 0; i < X.length; i++) {
            predictions[i] = predictSingleInstance(X[i], this.root);
        }
        return predictions;
    }

    private int predictSingleInstance(int[] x, TreeNode node) {

        try {


            if (node.isLeaf()) {
                return node.getValue();
            }

            int featureValue = x[node.getFeatureIndex()];
            TreeNode childNode = node.getChild(featureValue);
            if (childNode == null) {
                return Integer.MIN_VALUE;
            }

            return predictSingleInstance(x, childNode);

        }catch (StackOverflowError e){
            System.out.println(e + "retry pls");
        }
        return 0;
    }

}


