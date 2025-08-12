# Connect4 Game

A fully-featured JavaFX-based Connect4 game with advanced AI, multiplayer support, and comprehensive statistics tracking.

## 🎮 Features

### Core Gameplay
- **Classic Connect4 Rules**: 6x7 grid with standard win conditions
- **Multiple Game Modes**:
  - Human vs Human
  - Human vs AI (4 difficulty levels)
  - AI vs AI
  - LAN Multiplayer
  - Online Multiplayer

### Advanced AI
- **Minimax Algorithm**: Advanced AI with alpha-beta pruning
- **4 Difficulty Levels**: Easy, Medium, Hard, Expert
- **Adaptive AI**: AI adjusts strategy based on game state

### UI/UX
- **Multiple Themes**: Dark, Light, Neon, Classic, Ocean, Forest
- **Customizable Board Colors**: Dynamic color schemes
- **Smooth Animations**: Enhanced piece drop animations with bounce effects
- **Hover Preview**: Visual feedback for piece placement
- **Responsive Design**: Professional styling with hover effects

### Statistics & Analytics
- **Comprehensive Game Tracking**: Every game recorded with detailed statistics
- **Player Performance Metrics**: Win/loss ratios, streaks, average moves
- **Heat Maps**: Visual representation of most used columns
- **Export Functionality**: Export statistics to CSV/JSON formats

### Multiplayer Features
- **LAN Multiplayer**: Connect to other players on local network
- **Online Multiplayer**: Lobby system for online games
- **Spectator Mode**: Watch ongoing games
- **Player Management**: Track connected players and their stats

### Settings & Customization
- **Comprehensive Settings Panel**: All game settings in one place
- **Keyboard Shortcuts**: Customizable keyboard controls
- **Accessibility Features**: Color blind mode, high contrast themes
- **Audio Controls**: Volume controls for sound effects and music

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- JavaFX 19.0.2.1 or higher

### Running the Game
1. Clone the repository
2. Import the project into your favorite Java IDE (IntelliJ IDEA, Eclipse, etc.)
3. Ensure JavaFX is properly configured
4. Run `com.connect4.Main` as the main class

### Build from Source
```bash
mvn clean compile
mvn javafx:run
```

## 🎯 Game Controls

### Basic Controls
- **Mouse Click**: Place piece in selected column
- **Hover**: Preview piece placement
- **Keyboard**: Customizable shortcuts (see Settings)

### Game Modes
- **Human vs Human**: Local two-player mode
- **Human vs AI**: Play against AI with 4 difficulty levels
- **AI vs AI**: Watch AI vs AI matches
- **LAN Multiplayer**: Connect to local network games
- **Online Multiplayer**: Join online lobbies

## 🛠️ Development

### Architecture
- **MVC Pattern**: Clean separation of concerns
- **JavaFX**: Modern Java GUI framework
- **Maven**: Build automation and dependency management

### Key Components
- `GameBoard.java`: Core game logic and board management
- `GameController.java`: Standard game controller
- `EnhancedGameController.java`: Advanced features controller
- `MinimaxAI.java`: AI implementation with minimax algorithm

### Testing
- **Unit Tests**: JUnit 5 for game logic and AI
- **UI Tests**: TestFX for interface testing
- **Integration Tests**: End-to-end testing

## 📊 Statistics

The game tracks:
- Win/loss ratios
- Streaks and performance trends
- Average moves per game
- Column usage patterns
- Game duration statistics

## 🎨 Customization

### Themes Available
- **Dark**: Modern dark theme
- **Light**: Clean light theme
- **Neon**: Vibrant neon colors
- **Classic**: Traditional Connect4 colors
- **Ocean**: Blue ocean theme
- **Forest**: Green nature theme

### Settings
- **Animation Speed**: Adjustable animation timing
- **Sound Effects**: Volume controls for audio
- **Keyboard Shortcuts**: Customizable key bindings
- **Accessibility**: Color blind and high contrast modes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new features
5. Submit a pull request

## 📄 License
This project is open source and available under the MIT License.
