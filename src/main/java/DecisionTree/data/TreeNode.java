package DecisionTree.data;

import java.util.HashMap;
import java.util.Map;


public class TreeNode {
    private final Map<Integer, TreeNode> children; // Дочерние узлы
    private final Integer featureIndex; // Индекс признака для внутренних узлов (null для листовых узлов)
    private final Integer value; // Значение для листовых узлов (null для внутренних узлов)

    // Приватный конструктор
    private TreeNode(Integer featureIndex, Integer value) {
        this.featureIndex = featureIndex;
        this.value = value;
        this.children = new HashMap<>();
    }

    // Фабричный метод для создания внутреннего узла
    public static TreeNode createInternalNode(int featureIndex) {
        return new TreeNode(featureIndex, null);
    }

    // Фабричный метод для создания листового узла
    public static TreeNode createLeafNode(int value) {
        return new TreeNode(null, value);
    }

    // Добавление дочернего узла
    public void addChild(int key, TreeNode child) {
        children.put(key, child);
    }

    // Получение дочернего узла
    public TreeNode getChild(int key) {
        return children.get(key);
    }

    // Проверка, является ли узел листовым
    public boolean isLeaf() {
        return children.isEmpty();
    }

    // Получение значения узла
    public int getValue() {
        return value;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }
}
