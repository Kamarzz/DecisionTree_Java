package DecisionTree.data;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DataProcessor {

    public int[][] loadCSV(String filePath) {
        List<int[]> data = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.newFormat(';'))) {
            for (CSVRecord record : parser) {
                // Пропускаем первые две строки с заголовками
                if (record.getRecordNumber() <= 2) {
                    continue;
                }

                // Уменьшаем размер строки на один, чтобы не включать первый столбец
                int[] row = new int[record.size() - 1];

                // Начинаем считывание с второго столбца (индекс 1)
                for (int i = 1; i < record.size(); i++) {
                    row[i - 1] = Integer.parseInt(record.get(i));
                }

                // Преобразуем оценку в бинарное значение
                int gradeIndex = row.length - 1;
                row[gradeIndex] = row[gradeIndex] >= 5 ? 1 : 0;

                data.add(row);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return data.toArray(new int[0][]);
    }

    public SplitDataSet splitData(int[][] data, double trainRatio) {
        Collections.shuffle(Arrays.asList(data)); // Перемешивание данных

//        int[] selectedFeatures = new int[]{4, 24, 26, 28, 12, 30};

        int[] selectedFeatures = generateUniqueRandomNumbers(1, 31, 6);
        System.out.println(Arrays.toString(selectedFeatures));

        int totalRows = data.length;
        int trainSize = (int) (totalRows * trainRatio);
        int testSize = totalRows - trainSize;

        int[][] trainData = new int[trainSize][selectedFeatures.length];
        int[][] testData = new int[testSize][selectedFeatures.length];

        int[] trainY = new int[trainSize];
        int[] testY = new int[testSize];

        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < selectedFeatures.length; j++) {
                if (i < trainSize) {
                    trainData[i][j] = data[i][selectedFeatures[j]];
                } else {
                    testData[i - trainSize][j] = data[i][selectedFeatures[j]];
                }
            }
            if (i < trainSize) {
                trainY[i] = data[i][data[i].length - 1];
            } else {
                testY[i - trainSize] = data[i][data[i].length - 1];
            }
        }

        return new SplitDataSet(trainData, trainY, testData, testY);
    }

    private static int[] generateUniqueRandomNumbers(int min, int max, int count) {
        Set<Integer> uniqueNumbers = new HashSet<>();
        Random random = new Random();
        while (uniqueNumbers.size() < count) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            uniqueNumbers.add(randomNumber);
        }

        // Преобразование Set в массив
        int[] result = new int[count];
        int index = 0;
        for (Integer number : uniqueNumbers) {
            result[index++] = number;
        }
        return result;
    }}
