package com.connect4.themes;

import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import java.util.HashMap;
import java.util.Map;

public class EnhancedThemeManager {
    
    public enum Theme {
        DARK("Dark", "#2c3e50", "#34495e", "#3498db", "#e74c3c", "#f39c12"),
        LIGHT("Light", "#ecf0f1", "#bdc3c7", "#2980b9", "#c0392b", "#d35400"),
        NEON("Neon", "#0a0a0a", "#1a1a1a", "#00ff00", "#ff0080", "#00ffff"),
        CLASSIC("Classic", "#2c3e50", "#34495e", "#3498db", "#e74c3c", "#f39c12"),
        OCEAN("Ocean", "#1e3a8a", "#3b82f6", "#06b6d4", "#fbbf24", "#f59e0b"),
        FOREST("Forest", "#14532d", "#22c55e", "#84cc16", "#fbbf24", "#f59e0b");
        
        private final String name;
        private final String backgroundColor;
        private final String secondaryColor;
        private final String accentColor;
        private final String player1Color;
        private final String player2Color;
        
        Theme(String name, String backgroundColor, String secondaryColor, 
              String accentColor, String player1Color, String player2Color) {
            this.name = name;
            this.backgroundColor = backgroundColor;
            this.secondaryColor = secondaryColor;
            this.accentColor = accentColor;
            this.player1Color = player1Color;
            this.player2Color = player2Color;
        }
        
        public String getName() { return name; }
        public String getBackgroundColor() { return backgroundColor; }
        public String getSecondaryColor() { return secondaryColor; }
        public String getAccentColor() { return accentColor; }
        public String getPlayer1Color() { return player1Color; }
        public String getPlayer2Color() { return player2Color; }
    }
    
    private static Theme currentTheme = Theme.DARK;
    private static final Map<String, Color> customColors = new HashMap<>();
    
    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }
    
    public static Theme getCurrentTheme() {
        return currentTheme;
    }
    
    public static Background createBackground() {
        return new Background(new BackgroundFill(
            Color.web(currentTheme.getBackgroundColor()),
            CornerRadii.EMPTY,
            Insets.EMPTY
        ));
    }
    
    public static Color getPlayerColor(int player) {
        return player == 1 ? Color.web(currentTheme.getPlayer1Color()) : Color.web(currentTheme.getPlayer2Color());
    }
    
    public static void setCustomColor(String key, Color color) {
        customColors.put(key, color);
    }
    
    public static Color getCustomColor(String key) {
        return customColors.getOrDefault(key, Color.web(currentTheme.getAccentColor()));
    }
}
