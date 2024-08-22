package kz.raim.core;

import kz.raim.config.GameConfig;
import kz.raim.model.BonusSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardCalculator {
    private final GameConfig config;
    private final String[][] matrix;
    private int reward = 0;
    private String appliedBonusSymbol = null;
    private final Map<String, List<String>> appliedWinningCombinations = new HashMap<>();

    public RewardCalculator(GameConfig config, String[][] matrix) {
        this.config = config;
        this.matrix = matrix;
    }

    public int calculateReward(int betAmount) {
        for (Map.Entry<String, WinningCombination> entry : config.win_combinations.entrySet()) {
            String combinationName = entry.getKey();
            WinningCombination combination = entry.getValue();

            if (combination.when.equals("same_symbols")) {
                checkSameSymbolsCombination(combinationName, combination, betAmount);
            } else if (combination.when.equals("linear_symbols")) {
                checkLinearSymbolsCombination(combinationName, combination, betAmount);
            }
        }

        if (reward > 0) {
            applyBonusSymbol();
        }

        return reward;
    }

    private void checkSameSymbolsCombination(String combinationName, WinningCombination combination, int betAmount) {
        for (String symbol : config.symbols.keySet()) {
            int count = 0;
            for (String[] row : matrix) {
                for (String cell : row) {
                    if (cell.equals(symbol)) {
                        count++;
                    }
                }
            }

            // Only apply the combination if it meets or exceeds the current combination's count
            if (count >= combination.count) {
                int symbolReward = (int) (betAmount * config.symbols.get(symbol).reward_multiplier * combination.reward_multiplier);

                // Track the highest combination applied for each symbol
                List<String> existingCombinations = appliedWinningCombinations.get(symbol);
                if (existingCombinations == null || existingCombinations.isEmpty()) {
                    appliedWinningCombinations.put(symbol, new ArrayList<>());
                    appliedWinningCombinations.get(symbol).add(combinationName);
                    reward += symbolReward;
                } else {
                    String existingCombinationName = existingCombinations.get(0);
                    WinningCombination existingCombination = config.win_combinations.get(existingCombinationName);

                    // Replace the existing combination if the new one has a higher count
                    if (combination.count > existingCombination.count) {
                        reward -= betAmount * config.symbols.get(symbol).reward_multiplier * existingCombination.reward_multiplier;
                        reward += symbolReward;
                        appliedWinningCombinations.get(symbol).set(0, combinationName);
                    }
                }
            }
        }
    }


    private void checkLinearSymbolsCombination(String combinationName, WinningCombination combination, int betAmount) {
        for (List<String> area : combination.covered_areas) {
            String firstSymbol = null;
            boolean matches = true;

            for (String position : area) {
                String[] pos = position.split(":");
                int row = Integer.parseInt(pos[0]);
                int col = Integer.parseInt(pos[1]);

                if (firstSymbol == null) {
                    firstSymbol = matrix[row][col];
                } else if (!matrix[row][col].equals(firstSymbol)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                int symbolReward = (int) (betAmount * config.symbols.get(firstSymbol).reward_multiplier * combination.reward_multiplier);
                reward += symbolReward;
                appliedWinningCombinations.computeIfAbsent(firstSymbol, k -> new java.util.ArrayList<>()).add(combinationName);
            }
        }
    }

    private void applyBonusSymbol() {
        for (String[] row : matrix) {
            for (String cell : row) {
                if (config.symbols.get(cell).type.equals("bonus")) {
                    BonusSymbol bonus = (BonusSymbol) config.symbols.get(cell);
                    switch (bonus.impact) {
                        case "multiply_reward" -> reward *= (int) bonus.reward_multiplier;
                        case "extra_bonus" -> reward += bonus.extra;
                        case "miss" -> reward = 0;  // Set reward to 0 if MISS is applied
                    }
                    appliedBonusSymbol = cell;
                }
            }
        }
    }

    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }
}

