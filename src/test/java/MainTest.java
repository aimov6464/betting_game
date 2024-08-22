import kz.raim.Main;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {
    public MainTest() {
    }

    @Test
    public void testGameFlow_SimpleWin() {
        String configPath = this.getTestResourceFilePath("config_simple_win.json");
        String[] args = new String[]{"--config=" + configPath, "--betting-amount=100"};
        String output = this.captureOutput(() -> {
            Main.main(args);
        });
        assertTrue(output.contains("\"applied_winning_combinations\": {"));
        assertTrue(output.contains("\"applied_bonus_symbol\": \"null\""));
        assertTrue(output.matches("(?s).*Final Reward: \\d{1,5}.*"));
    }

    @Test
    public void testGameFlow_WithBonus() {
        String configPath = this.getTestResourceFilePath("config_with_bonus.json");
        String[] args = new String[]{"--config=" + configPath, "--betting-amount=100"};
        String output = this.captureOutput(() -> {
            Main.main(args);
        });
        assertTrue(output.contains("\"applied_winning_combinations\": {"));
        assertTrue(output.contains("\"applied_bonus_symbol\": \"10x\"")
                || output.contains("\"applied_bonus_symbol\": \"null\""));
        assertTrue(output.matches("(?s).*Final Reward: \\d{1,5}.*"));
    }

    @Test
    public void testGameFlow_WithMissBonus() {
        String configPath = this.getTestResourceFilePath("config_with_miss.json");
        String[] args = new String[]{"--config=" + configPath, "--betting-amount=100"};
        String output = this.captureOutput(() -> {
            Main.main(args);
        });
        System.out.println(output);
        assertTrue(output.contains("\"applied_bonus_symbol\": \"MISS\"")
                || output.contains("\"applied_bonus_symbol\": \"null\""));
        if (output.contains("\"applied_bonus_symbol\": \"MISS\"")) {
            assertTrue(output.contains("Final Reward: 0"));
        } else {
            assertFalse(output.contains("Final Reward: 0"));
        }

    }

    @Test
    public void testGameFlow_MultipleCombinations() {
        String configPath = this.getTestResourceFilePath("config_multiple_combinations.json");
        String[] args = new String[]{"--config=" + configPath, "--betting-amount=100"};
        String output = this.captureOutput(() -> {
            Main.main(args);
        });
        assertTrue(output.contains("\"applied_winning_combinations\": {"));
        assertTrue(output.contains("same_symbol_4_times") || output.contains("same_symbol_3_times"));
        assertTrue(output.contains("Final Reward: "));
        assertTrue(output.contains("\"applied_bonus_symbol\": \"null\"")
                || output.contains("\"applied_bonus_symbol\": \"10x\"")
                || output.contains("\"applied_bonus_symbol\": \"MISS\""));
    }

    private String captureOutput(Runnable task) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(baos));

        try {
            task.run();
        } finally {
            System.setOut(oldOut);
        }

        return baos.toString();
    }

    private String getTestResourceFilePath(String filename) {
        return Paths.get("src/test/resources", filename).toAbsolutePath().toString();
    }
}

