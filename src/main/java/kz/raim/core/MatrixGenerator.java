package kz.raim.core;

import kz.raim.config.GameConfig;

import java.util.Map;
import java.util.Random;

public class MatrixGenerator {
    private final GameConfig config;
    private final Random random;

    public MatrixGenerator(GameConfig config) {
        this(config, new Random());
    }

    public MatrixGenerator(GameConfig config, Random random) {
        this.config = config;
        this.random = random;
    }

    public String[][] generateMatrix() {
        String[][] matrix = new String[config.rows][config.columns];

        for (int row = 0; row < config.rows; row++) {
            for (int col = 0; col < config.columns; col++) {
                if (config.probabilities.bonus_symbols != null && random.nextDouble() < 0.1) {
                    matrix[row][col] = getRandomSymbol(config.probabilities.bonus_symbols);
                } else {
                    matrix[row][col] = getRandomSymbol(config.probabilities.standard_symbols);
                }
            }
        }

        return matrix;
    }

    private String getRandomSymbol(Map<String, Integer> symbolProbabilities) {
        if (symbolProbabilities == null || symbolProbabilities.isEmpty()) {
            throw new IllegalStateException("No symbol probabilities provided.");
        }

        int totalProbability = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomInt = random.nextInt(totalProbability);

        int currentSum = 0;
        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            currentSum += entry.getValue();
            if (randomInt < currentSum) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Random symbol selection failed");
    }
}

