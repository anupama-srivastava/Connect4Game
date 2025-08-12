package com.connect4.themes;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ThemeManager {
    private static final Logger logger = LoggerFactory.getLogger(ThemeManager.class);
    
    private static ThemeManager instance;
    private final Map<String, Theme> themes = new HashMap<>();
    private String currentTheme = "default";
    
    private ThemeManager() {
        initializeThemes();
    }
    
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }
    
    private void initializeThemes() {
        // Default Theme
        Theme defaultTheme = new Theme("default", "Default", 
            Color.web("#2c3e50"), // Background
            Color.web("#34495e"), // Secondary
            Color.web("#e74c3c"), // Player 1
            Color.web("#f39c12"), // Player 2
            Color.WHITE,          // Text
            Color.web("#3498db"), // Accent
            Color.web("#27ae60")  // Success
        );
        
        // Dark Theme
        Theme darkTheme = new Theme("dark", "Dark Mode",
            Color.web("#1a1a1a"), // Background
            Color.web("#2d2d2d"), // Secondary
            Color.web("#ff6b6b"), // Player 1
            Color.web("#ffd93d"), // Player 2
            Color.web("#f0f0f0"), // Text
            Color.web("#4ecdc4"), // Accent
            Color.web("#95e1d3")  // Success
        );
        
        // Neon Theme
        Theme neonTheme = new Theme("neon", "Neon",
            Color.web("#0a0a0a"), // Background
            Color.web("#1a1a2e"), // Secondary
            Color.web("#ff00ff"), // Player 1
            Color.web("#00ffff"), // Player 2
            Color.web("#ffffff"), // Text
            Color.web("#ff006e"), // Accent
            Color.web("#00ff00")  // Success
        );
        
        // Classic Theme
        Theme classicTheme = new Theme("classic", "Classic",
            Color.web("#2c3e50"), // Background
            Color.web("#34495e"), // Secondary
            Color.web("#e74c3c"), // Player 1
            Color.web("#f39c12"), // Player 2
            Color.WHITE,          // Text
            Color.web("#3498db"), // Accent
            Color.web("#27ae60")  // Success
        );
        
        themes.put("default", defaultTheme);
        themes.put("dark", darkTheme);
        themes.put("neon", neonTheme);
        themes.put("classic", classicTheme);
    }
    
    public void applyTheme(String themeName) {
        if (themes.containsKey(themeName)) {
            currentTheme = themeName;
            logger.info("Applied theme: {}", themeName);
        }
    }
    
    public Theme getCurrentTheme() {
        return themes.get(currentTheme);
    }
    
    public Theme getTheme(String themeName) {
        return themes.get(themeName);
    }
    
    public Map<String, Theme> getAllThemes() {
        return new HashMap<>(themes);
    }
    
    public static class Theme {
        private final String id;
        private final String name;
        private final Color backgroundColor;
        private final Color secondaryColor;
        private final Color player1Color;
        private final Color player2Color;
        private final Color textColor;
        private final Color accentColor;
        private final Color successColor;
        
        public Theme(String id, String name, Color backgroundColor, Color secondaryColor,
                    Color player1Color, Color player2Color, Color textColor,
                    Color accentColor, Color successColor) {
            this.id = id;
            this.name = name;
            this.backgroundColor = backgroundColor;
            this.secondaryColor = secondaryColor;
            this.player1Color = player1Color;
            this.player2Color = player2Color;
            this.textColor = textColor;
            this.accentColor = accentColor;
            this.successColor = successColor;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public Color getBackgroundColor() { return backgroundColor; }
        public Color getSecondaryColor() { return secondaryColor; }
        public Color getPlayer1Color() { return player1Color; }
        public Color getPlayer2Color() { return player2Color; }
        public Color getTextColor() { return textColor; }
        public Color getAccentColor() { return accentColor; }
        public Color getSuccessColor() { return successColor; }
    }
}
