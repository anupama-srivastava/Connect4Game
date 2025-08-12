package com.connect4.settings;

import com.connect4.themes.EnhancedThemeManager;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnhancedSettings {
    
    public enum BoardSize {
        SMALL(6, 7, "6x7"),
        MEDIUM(8, 9, "8x9"),
        LARGE(10, 11, "10x11");
        
        private final int rows;
        private final int cols;
        private final String displayName;
        
        BoardSize(int rows, int cols, String displayName) {
            this.rows = rows;
            this.cols = cols;
            this.displayName = displayName;
        }
        
        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public String getDisplayName() { return displayName; }
        
        @Override
        public String toString() { return displayName; }
    }
    
    public enum AnimationSpeed {
        SLOW(1000, "Slow"),
        NORMAL(500, "Normal"),
        FAST(250, "Fast"),
        INSTANT(0, "Instant");
        
        private final int duration;
        private final String displayName;
        
        AnimationSpeed(int duration, String displayName) {
            this.duration = duration;
            this.displayName = displayName;
        }
        
        public int getDuration() { return duration; }
        public String getDisplayName() { return displayName; }
        
        @Override
        public String toString() { return displayName; }
    }
    
    private final StringProperty playerName = new SimpleStringProperty("Player");
    private final ObjectProperty<EnhancedThemeManager.Theme> currentTheme = 
        new SimpleObjectProperty<>(EnhancedThemeManager.Theme.DARK);
    private final ObjectProperty<BoardSize> boardSize = new SimpleObjectProperty<>(BoardSize.SMALL);
    private final BooleanProperty soundEnabled = new SimpleBooleanProperty(true);
    private final DoubleProperty soundVolume = new SimpleDoubleProperty(0.7);
    private final BooleanProperty musicEnabled = new SimpleBooleanProperty(true);
    private final DoubleProperty musicVolume = new SimpleDoubleProperty(0.5);
    private final BooleanProperty animationsEnabled = new SimpleBooleanProperty(true);
    private final ObjectProperty<AnimationSpeed> animationSpeed = new SimpleObjectProperty<>(AnimationSpeed.NORMAL);
    private final BooleanProperty colorBlindMode = new SimpleBooleanProperty(false);
    private final BooleanProperty highContrastMode = new SimpleBooleanProperty(false);
    private final BooleanProperty keyboardShortcutsEnabled = new SimpleBooleanProperty(true);
    private final ObservableList<String> customKeyboardShortcuts = FXCollections.observableArrayList();
    
    private static EnhancedSettings instance;
    
    private EnhancedSettings() {
        // Initialize default settings
    }
    
    public static EnhancedSettings getInstance() {
        if (instance == null) {
            instance = new EnhancedSettings();
        }
        return instance;
    }
    
    // Property getters
    public StringProperty playerNameProperty() { return playerName; }
    public ObjectProperty<EnhancedThemeManager.Theme> currentThemeProperty() { return currentTheme; }
    public ObjectProperty<BoardSize> boardSizeProperty() { return boardSize; }
    public BooleanProperty soundEnabledProperty() { return soundEnabled; }
    public DoubleProperty soundVolumeProperty() { return soundVolume; }
    public BooleanProperty musicEnabledProperty() { return musicEnabled; }
    public DoubleProperty musicVolumeProperty() { return musicVolume; }
    public BooleanProperty animationsEnabledProperty() { return animationsEnabled; }
    public ObjectProperty<AnimationSpeed> animationSpeedProperty() { return animationSpeed; }
    public BooleanProperty colorBlindModeProperty() { return colorBlindMode; }
    public BooleanProperty highContrastModeProperty() { return highContrastMode; }
    public BooleanProperty keyboardShortcutsEnabledProperty() { return keyboardShortcutsEnabled; }
    public ObservableList<String> getCustomKeyboardShortcuts() { return customKeyboardShortcuts; }
    
    // Convenience getters
    public String getPlayerName() { return playerName.get(); }
    public EnhancedThemeManager.Theme getCurrentTheme() { return currentTheme.get(); }
    public BoardSize getBoardSize() { return boardSize.get(); }
    public boolean isSoundEnabled() { return soundEnabled.get(); }
    public double getSoundVolume() { return soundVolume.get(); }
    public boolean isMusicEnabled() { return musicEnabled.get(); }
    public double getMusicVolume() { return musicVolume.get(); }
    public boolean isAnimationsEnabled() { return animationsEnabled.get(); }
    public AnimationSpeed getAnimationSpeed() { return animationSpeed.get(); }
    public boolean isColorBlindMode() { return colorBlindMode.get(); }
    public boolean isHighContrastMode() { return highContrastMode.get(); }
    public boolean isKeyboardShortcutsEnabled() { return keyboardShortcutsEnabled.get(); }
    
    // Setters
    public void setPlayerName(String name) { playerName.set(name); }
    public void setCurrentTheme(EnhancedThemeManager.Theme theme) { currentTheme.set(theme); }
    public void setBoardSize(BoardSize size) { boardSize.set(size); }
    public void setSoundEnabled(boolean enabled) { soundEnabled.set(enabled); }
    public void setSoundVolume(double volume) { soundVolume.set(Math.max(0, Math.min(1, volume))); }
    public void setMusicEnabled(boolean enabled) { musicEnabled.set(enabled); }
    public void setMusicVolume(double volume) { musicVolume.set(Math.max(0, Math.min(1, volume))); }
    public void setAnimationsEnabled(boolean enabled) { animationsEnabled.set(enabled); }
    public void setAnimationSpeed(AnimationSpeed speed) { animationSpeed.set(speed); }
    public void setColorBlindMode(boolean enabled) { colorBlindMode.set(enabled); }
    public void setHighContrastMode(boolean enabled) { highContrastMode.set(enabled); }
    public void setKeyboardShortcutsEnabled(boolean enabled) { keyboardShortcutsEnabled.set(enabled); }
    
    public void resetToDefaults() {
        playerName.set("Player");
        currentTheme.set(EnhancedThemeManager.Theme.DARK);
        boardSize.set(BoardSize.SMALL);
        soundEnabled.set(true);
        soundVolume.set(0.7);
        musicEnabled.set(true);
        musicVolume.set(0.5);
        animationsEnabled.set(true);
        animationSpeed.set(AnimationSpeed.NORMALNext, I will create the enhanced settings panel to support customization and accessibility features:

<create_file>
<path>Connect4Game/src/main/java/com/connect4/settings/EnhancedSettings.java</path>
<content>
package com.connect4.settings;

import com.connect4.themes.EnhancedThemeManager;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnhancedSettings {
    
    public enum BoardSize {
        SMALL(6, 7, "6x7"),
        MEDIUM(8, 9, "8x9"),
        LARGE(10, 11, "10x11");
        
        private final int rows;
        private final int cols;
        private final String label;
        
        BoardSize(int rows, int cols, String label) {
            this.rows = rows;
            this.cols = cols;
            this.label = label;
        }
        
        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public String getLabel() { return label; }
        
        @Override
        public String toString() {
            return label;
        }
    }
    
    private final ObjectProperty<EnhancedThemeManager.Theme> currentTheme = new SimpleObjectProperty<>(EnhancedThemeManager.Theme.DARK);
    private final ObjectProperty<BoardSize> boardSize = new SimpleObjectProperty<>(BoardSize.SMALL);
    private final BooleanProperty colorBlindMode = new SimpleBooleanProperty(false);
    private final BooleanProperty highContrastMode = new SimpleBooleanProperty(false);
    private final DoubleProperty animationSpeed = new SimpleDoubleProperty(1.0);
    private final BooleanProperty soundEnabled = new SimpleBooleanProperty(true);
    private final DoubleProperty volume = new SimpleDoubleProperty(0.5);
    
    public EnhancedSettings() {
        // Default settings
    }
    
    public ObjectProperty<EnhancedThemeManager.Theme> currentThemeProperty() {
        return currentTheme;
    }
    
    public BoardSize getBoardSize() {
        return boardSize.get();
    }
    
    public ObjectProperty<BoardSize> boardSizeProperty() {
        return boardSize;
    }
    
    public BooleanProperty colorBlindModeProperty() {
        return colorBlindMode;
    }
    
    public BooleanProperty highContrastModeProperty() {
        return highContrastMode;
    }
    
    public DoubleProperty animationSpeedProperty() {
        return animationSpeed;
    }
    
    public BooleanProperty soundEnabledProperty() {
        return soundEnabled;
    }
    
    public DoubleProperty volumeProperty() {
        return volume;
    }
    
    public void setCurrentTheme(EnhancedThemeManager.Theme theme) {
        currentTheme.set(theme);
    }
    
    public EnhancedThemeManager.Theme getCurrentTheme() {
        return currentTheme.get();
    }
    
    public void setBoardSize(BoardSize size) {
        boardSize.set(size);
    }
    
    public void setColorBlindMode(boolean enabled) {
        colorBlindMode.set(enabled);
    }
    
    public void setHighContrastMode(boolean enabled) {
        highContrastMode.set(enabled);
    }
    
    public void setAnimationSpeed(double speed) {
        animationSpeed.set(speed);
    }
    
    public void setSoundEnabled(boolean enabled) {
        soundEnabled.set(enabled);
    }
    
    public void setVolume(double vol) {
        volume.set(vol);
    }
}
