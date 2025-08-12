# Connect4 Game

A fully-featured JavaFX-based Connect4 game with advanced AI, multiplayer support, and comprehensive statistics tracking.

## 🎮 Features

### Core Gameplay
- **Classic Connect4 Rules**: 6x7 grid with standard win conditions
- **Multiple Game Modes**:
  - Human vs Human (Local/LAN/Online)
  - Human vs AI (4 difficulty levels)
  - AI vs AI (Spectator mode)
  - LAN Multiplayer
  - Online Multiplayer with lobby system

### Advanced AI
- **Minimax Algorithm**: Advanced AI with alpha-beta pruning and heuristic evaluation
- **4 Difficulty Levels**: Easy, Medium, Hard, Expert
- **Adaptive AI**: AI adjusts strategy based on game state and opponent patterns
- **AI Personalities**: Different AI playing styles and strategies

### UI/UX
- **Multiple Themes**: Dark, Light, Neon, Classic, Ocean, Forest, Sunset
- **Customizable Board Colors**: Dynamic color schemes with real-time preview
- **Smooth Animations**: Enhanced piece drop animations with physics-based bounce effects
- **Hover Preview**: Visual feedback for piece placement with column highlighting
- **Responsive Design**: Professional styling with hover effects and transitions
- **Animated Backgrounds**: Particle effects and smooth transitions

### Statistics & Analytics
- **Comprehensive Game Tracking**: Every game recorded with detailed statistics
- **Player Performance Metrics**: Win/loss ratios, streaks, average moves per game
- **Heat Maps**: Visual representation of most used columns and patterns
- **Export Functionality**: Export statistics to CSV/JSON formats
- **Real-time Updates**: Live statistics display during gameplay
- **Historical Analysis**: Track performance over time

### Multiplayer Features
- **LAN Multiplayer**: Connect to other players on local network with automatic discovery
- **Online Multiplayer**: Lobby system for online games with matchmaking
- **Spectator Mode**: Watch ongoing games with chat support
- **Player Management**: Track connected players and their statistics
- **Game Replays**: Save and replay interesting matches

### Settings & Customization
- **Comprehensive Settings Panel**: All game settings in one centralized location
- **Keyboard Shortcuts**: Fully customizable keyboard controls
- **Accessibility Features**: Color blind mode, high contrast themes, screen reader support
- **Audio Controls**: Individual volume controls for sound effects and background music
- **Animation Controls**: Adjustable animation speeds and effects
- **Auto-save**: Automatic game state persistence

## 🚀 Quick Start

### Prerequisites
- **Java**: Java 17 or higher (OpenJDK recommended)
- **JavaFX**: JavaFX 19.0.2.1 or higher
- **Maven**: Apache Maven 3.8.1 or higher (for building from source)

### System Requirements
- **Minimum**: 4GB RAM, 1GB free disk space
- **Recommended**: 8GB RAM, SSD storage for better performance
- **OS**: Windows 10/11, macOS 10.15+, or Linux (Ubuntu 18.04+)

### Installation Options

#### Option 1: Pre-built JAR (Recommended)
1. Download the latest release from [Releases](https://github.com/anupama-srivastava/Connect4Game/releases)
2. Run: `java -jar Connect4Game-1.0.0.jar`

#### Option 2: Build from Source
```bash
# Clone the repository
git clone https://github.com/anupama-srivastava/Connect4Game.git
cd Connect4Game

# Build and run
mvn clean compile
mvn javafx:run
```

#### Option 3: IDE Setup
1. **IntelliJ IDEA**:
   - Import as Maven project
   - Ensure JavaFX SDK is configured in Project Structure
   - Run `com.connect4.Main` as the main class

2. **Eclipse**:
   - Import as existing Maven project
   - Configure JavaFX library in build path
   - Run `com.connect4.Main` as Java application

### Troubleshooting Common Issues

#### JavaFX Configuration Issues
```bash
# If you get JavaFX runtime errors, set VM options:
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.media
```

#### Maven Build Issues
```bash
# Clean and rebuild
mvn clean
mvn install

# Force update dependencies
mvn dependency:purge-local-repository
mvn install -U
```

#### IDE-Specific Issues
- **IntelliJ**: File → Project Structure → Libraries → Add JavaFX SDK
- **Eclipse**: Project Properties → Java Build Path → Add External JARs (JavaFX jars)

## 🎯 Game Controls

### Basic Controls
- **Mouse**: Click to place piece in selected column
- **Hover**: Preview piece placement with column highlighting
- **Right-click**: Cancel current move preview
- **Scroll**: Zoom in/out (when supported)

### Keyboard Shortcuts (Customizable)
- **Space**: Drop piece in highlighted column
- **Arrow Keys**: Navigate between columns
- **Enter**: Confirm move
- **Escape**: Cancel current action
- **Ctrl+N**: New game
- **Ctrl+S**: Save game
- **Ctrl+O**: Load game
- **Ctrl+Q**: Quit game

### Game Modes
- **Human vs Human**: Local two-player with turn indicators
- **Human vs AI**: Play against AI with 4 difficulty levels
- **AI vs AI**: Watch AI vs AI matches with speed controls
- **LAN Multiplayer**: Connect via IP address or network discovery
- **Online Multiplayer**: Join public lobbies or create private rooms

## 🛠️ Development

### Architecture
- **MVC Pattern**: Clean separation of concerns with Model-View-Controller
- **JavaFX**: Modern Java GUI framework with FXML
- **Maven**: Build automation and dependency management
- **Observer Pattern**: Event-driven architecture for game state updates

### Key Components
- **Main.java**: Application entry point and initialization
- **GameBoard.java**: Core game logic and board state management
- **GameController.java**: Standard game controller handling user interactions
- **EnhancedGameController.java**: Advanced features controller for Phase 2
- **MinimaxAI.java**: AI implementation with minimax algorithm and alpha-beta pruning
- **GameStateService.java**: Business logic for game state management
- **MultiplayerManager.java**: Network multiplayer functionality
- **GameStatistics.java**: Analytics and performance tracking

### Testing Strategy
- **Unit Tests**: JUnit 5 for game logic, AI algorithms, and model testing
- **UI Tests**: TestFX for interface testing and user interaction validation
- **Integration Tests**: End-to-end testing of complete game flows
- **Performance Tests**: Load testing for multiplayer features

### Development Setup
```bash
# Clone and setup
git clone https://github.com/anupama-srivastava/Connect4Game.git
cd Connect4Game

# Run tests
mvn test

# Generate test report
mvn surefire-report:report

# Package application
mvn package
```

## 📊 Statistics Tracking

The game comprehensively tracks:
- **Game Results**: Win/loss/draw with timestamps
- **Performance Metrics**: Win rates, streaks, average game duration
- **Move Analytics**: Average moves per game, column preferences
- **AI Performance**: AI difficulty effectiveness and learning patterns
- **Multiplayer Stats**: Network game performance and connection quality
- **Historical Data**: Performance trends over time

## 🎨 Customization Options

### Available Themes
- **Dark**: Modern dark theme with blue accents
- **Light**: Clean light theme with gray accents
- **Neon**: Vibrant neon colors with glowing effects
- **Classic**: Traditional Connect4 red and yellow
- **Ocean**: Blue ocean theme with wave animations
- **Forest**: Green nature theme with leaf patterns
- **Sunset**: Warm orange and purple sunset theme

### Customization Features
- **Board Colors**: Customizable board and piece colors
- **Animation Speed**: Adjustable from instant to slow motion
- **Sound Effects**: Individual volume controls for different effects
- **Keyboard Shortcuts**: Fully remappable controls
- **Window Size**: Resizable window with responsive design
- **Font Size**: Adjustable for accessibility

## 🤝 Contributing

I welcome contributions! Please see my [Contributing Guidelines](CONTRIBUTING.md) for details.

### Quick Contribution Steps
1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow Java coding standards (Google Java Style Guide)
- Write comprehensive tests for new features
- Update documentation for API changes
- Ensure all tests pass before submitting PR

## 📁 Project Structure

```
Connect4Game/
├── src/
│   ├── main/
│   │   ├── java/com/connect4/
│   │   │   ├── Main.java                 # Application entry point
│   │   │   ├── ai/
│   │   │   │   └── MinimaxAI.java        # AI implementation
│   │   │   ├── audio/
│   │   │   │   └── AudioManager.java     # Sound management
│   │   │   ├── controller/
│   │   │   │   ├── GameController.java   # Standard controller
│   │   │   │   └── EnhancedGameController.java # Advanced controller
│   │   │   ├── model/
│   │   │   │   ├── GameBoard.java        # Core game logic
│   │   │   │   └── GameState.java        # Game state management
│   │   │   ├── multiplayer/
│   │   │   │   └── MultiplayerManager.java # Network multiplayer
│   │   │   ├── persistence/
│   │   │   │   └── CloudSaveManager.java # Save/load functionality
│   │   │   ├── service/
│   │   │   │   └── GameStateService.java # Business logic
│   │   │   ├── settings/
│   │   │   │   ├── GameSettings.java     # Basic settings
│   │   │   │   └── EnhancedSettings.java # Advanced settings
│   │   │   ├── statistics/
│   │   │   │   └── GameStatistics.java   # Analytics
│   │   │   ├── themes/
│   │   │   │   ├── ThemeManager.java     # Basic themes
│   │   │   │   └── EnhancedThemeManager.java # Advanced themes
│   │   │   └── util/
│   │   │       ├── AnimationManager.java   # Basic animations
│   │   │       └── EnhancedAnimationManager.java # Advanced animations
│   │   └── resources/
│   │       ├── css/styles.css              # Application styling
│   │       └── fxml/
│   │           ├── GameView.fxml           # Standard UI
│   │           └── EnhancedGameView.fxml   # Enhanced UI
│   └── test/
│       └── java/com/connect4/            # Test suite
├── pom.xml                                 # Maven configuration
├── README.md                               # This file
├── .gitignore                             # Git ignore rules
├── PHASE2_README.md                        # Phase 2 documentation
└── TESTING_SUMMARY.md                     # Testing documentation
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### MIT License

```
MIT License

Copyright (c) 2024 Connect4Game Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 Acknowledgments

- **JavaFX Community** for the excellent GUI framework
- **JUnit and TestFX teams** for comprehensive testing tools
- **Contributors** who helped improve the game
- **Connect4 enthusiasts** for feedback and suggestions

---

**Happy Gaming!** 🎮 Connect4 awaits your strategic moves!
