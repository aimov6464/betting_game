# 🎰 Betting Game

Welcome to the **Betting Game**, a scratch card game that generates a matrix (e.g., 3x3) filled with symbols based on probabilities. The game determines if the user wins or loses based on specific winning combinations. 

## ✨ Features
- **Randomly Generated Matrix**: Creates a matrix filled with symbols based on defined probabilities.
- **Winning Combinations**: Matches the matrix against predefined winning combinations to determine if you've won.
- **Bonus Symbols**: Includes special bonus symbols that can multiply your rewards or add extra bonuses.

## 🛠️ How to Run the Project

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/betting_game.git
   cd betting_game
2. **Build the Project**
   Ensure you have Maven installed, then run:
   ```maven
   mvn clean package
3. **Run the Game**
   Use the following command to run the game, specifying the configuration file and betting amount:
   ```bash
   java -jar target/Betting_game-1.0-SNAPSHOT.jar --config=path/to/config.json --betting-amount=10000
📝 Configuration File (config.json)
The configuration file defines the rules and probabilities for the game, including the symbols, their probabilities, and the winning combinations. Here’s an example of what your config.json might look like:
The configuration file located in repository /resource/config.json

💰 Betting Amount
The --betting-amount parameter specifies the amount of money the user is betting in this game session. The final reward is calculated based on this amount and the winning combinations.

Example:java -jar target/Betting_game-1.0-SNAPSHOT.jar --config=src/main/resources/config.json --betting-amount=10000

Good luck!
   

