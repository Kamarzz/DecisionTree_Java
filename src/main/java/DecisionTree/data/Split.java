package DecisionTree.data;

public class Split {
    int[][] X; // Подмножество признаков
    int[] y;   // Подмножество целевых переменных

    public Split(int[][] X, int[] y) {
        this.X = X;
        this.y = y;
    }

    public int[][] getX() {
        return X;
    }

    public int[] getY() {
        return y;
    }
}
