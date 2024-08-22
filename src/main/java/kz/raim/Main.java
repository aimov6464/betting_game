package kz.raim;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.raim.config.GameConfig;
import kz.raim.core.MatrixGenerator;
import kz.raim.core.RewardCalculator;

import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar <jar-file> --config=config.json --betting-amount=100");
            return;
        }

        // Extract the config file path from the arguments
        String configFilePath = args[0].split("=")[1];
        int betAmount = Integer.parseInt(args[1].split("=")[1]);

        try {
            ObjectMapper mapper = new ObjectMapper();
            GameConfig config = mapper.readValue(new File(configFilePath), GameConfig.class);

            MatrixGenerator generator = new MatrixGenerator(config);
            String[][] matrix = generator.generateMatrix();

            RewardCalculator calculator = new RewardCalculator(config, matrix);
            int reward = calculator.calculateReward(betAmount);

            System.out.println("Generated Matrix:");
            for (String[] row : matrix) {
                for (String symbol : row) {
                    System.out.print(symbol + " ");
                }
                System.out.println();
            }

            System.out.println("Final Reward: " + reward);

            // Custom output formatting
            System.out.println("\n\"applied_winning_combinations\": {");
            for (Map.Entry<String, java.util.List<String>> entry : calculator.getAppliedWinningCombinations().entrySet()) {
                System.out.print("  \"" + entry.getKey() + "\": [");
                System.out.print(String.join(", ", entry.getValue()));
                System.out.println("]");
            }
            System.out.println("},");
            System.out.println("\"applied_bonus_symbol\": \"" + calculator.getAppliedBonusSymbol() + "\"");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
