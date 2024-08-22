package kz.raim.config;

import kz.raim.core.WinningCombination;
import kz.raim.model.Symbol;

import java.util.Map;

public class GameConfig {
    public int rows;
    public int columns;
    public Map<String, Symbol> symbols;
    public Probabilities probabilities;
    public Map<String, WinningCombination> win_combinations;

    public GameConfig() {
    }

    public static class Probabilities {
        public Map<String, Integer> standard_symbols;
        public Map<String, Integer> bonus_symbols;

        public Probabilities() {
        }
    }
}
