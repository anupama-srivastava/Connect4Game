package com.connect4.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class GameSettings {
    private static final Logger logger = LoggerFactory.getLogger(GameSettings.class);
    private static final String SETTINGS_FILE = "connect4_settings.json";
    
    private static GameSettings instance;
    private final Preferences preferences;
    private final ObjectMapper objectMapper;
    
    // Audio settings
    private double masterVolume = 0.7;
    private double musicVolume = 0.5;
    private double sfxVolume = 0.8;
    private boolean muted = false;
    
    // Visual settings
    private String theme = "default";
    private boolean animationsEnabled = true;
    private boolean particleEffects = true;
    private int animationSpeed = 100; // milliseconds
    
    // Game settings
    private String defaultGameMode = "Human vs Human";
    private int defaultDifficulty = 2;
    private boolean autoSaveEnabled = true;
    private int autoSaveInterval = 30; // seconds
    
    // Accessibility settings
    private boolean colorBlindMode = false;
    private boolean highContrast = false;
    private boolean reducedMotion = false;
    private boolean screenReaderSupport = false;
    
    private GameSettings() {
        preferences = Preferences.userNodeForPackage(GameSettings.class);
        objectMapper = new ObjectMapper();
        loadSettings();
    }
    
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }
    
    public void saveSettings() {
        try {
            SettingsData data = new SettingsData(
                masterVolume, musicVolume, sfxVolume, muted,
                theme, animationsEnabled, particleEffects, animationSpeed,
                defaultGameMode, defaultDifficulty, autoSaveEnabled, autoSaveInterval,
                colorBlindMode, highContrast, reducedMotion, screenReaderSupport
            );
            
            objectMapper.writeValue(new File(SETTINGS_FILE), data);
            logger.info("Settings saved successfully");
            
        } catch (IOException e) {
            logger.error("Failed to save settings", e);
            saveToPreferences();
        }
    }
    
    private void loadSettings() {
        try {
            File settingsFile = new File(SETTINGS_FILE);
            if (settingsFile.exists()) {
                SettingsData data = objectMapper.readValue(settingsFile, SettingsData.class);
                applySettings(data);
                logger.info("Settings loaded from file");
            } else {
                loadFromPreferences();
            }
        } catch (IOException e) {
            logger.error("Failed to load settings from file, using defaults", e);
            loadFromPreferences();
        }
    }
    
    private void applySettings(SettingsData data) {
        this.masterVolume = data.masterVolume;
        this.musicVolume = data.musicVolume;
        this.sfxVolume = data.sfxVolume;
        this.muted = data.muted;
        this.theme = data.theme;
        this.animationsEnabled = data.animationsEnabled;
        this.particleEffects = data.particleEffects;
        this.animationSpeed = data.animationSpeed;
        this.defaultGameMode = data.defaultGameMode;
        this.defaultDifficulty = data.defaultDifficulty;
        this.autoSaveEnabled = data.autoSaveEnabled;
        this.autoSaveInterval = data.autoSaveInterval;
        this.colorBlindMode = data.colorBlindMode;
        this.highContrast = data.highContrast;
        this.reducedMotion = data.reducedMotion;
        this.screenReaderSupport = data.screenReaderSupport;
    }
    
    private void loadFromPreferences() {
        masterVolume = preferences.getDouble("masterVolume", 0.7);
        musicVolume = preferences.getDouble("musicVolume", 0.5);
        sfxVolume = preferences.getDouble("sfxVolume", 0.8);
        muted = preferences.getBoolean("muted", false);
        theme = preferences.get("theme", "default");
        animationsEnabled = preferences.getBoolean("animationsEnabled", true);
        particleEffects = preferences.getBoolean("particleEffects", true);
        animationSpeed = preferences.getInt("animationSpeed", 100);
        defaultGameMode = preferences.get("defaultGameMode", "Human vs Human");
        defaultDifficulty = preferences.getInt("defaultDifficulty", 2);
        autoSaveEnabled = preferences.getBoolean("autoSaveEnabled", true);
        autoSaveInterval = preferences.getInt("autoSaveInterval", 30);
        colorBlindMode = preferences.getBoolean("colorBlindMode", false);
        highContrast = preferences.getBoolean("highContrast", false);
        reducedMotion = preferences.getBoolean("reducedMotion", false);
        screenReaderSupport = preferences.getBoolean("screenReaderSupport", false);
    }
    
    private void saveToPreferences() {
        preferences.putDouble("masterVolume", masterVolume);
        preferences.putDouble("musicVolume", musicVolume);
        preferences.putDouble("sfxVolume", sfxVolume);
        preferences.putBoolean("muted", muted);
        preferences.put("theme", theme);
        preferences.putBoolean("animationsEnabled", animationsEnabled);
        preferences.putBoolean("particleEffects", particleEffects);
        preferences.putInt("animationSpeed", animationSpeed);
        preferences.put("defaultGameMode", defaultGameMode);
        preferences.putInt("defaultDifficulty", defaultDifficulty);
        preferences.putBoolean("autoSaveEnabled", autoSaveEnabled);
        preferences.putInt("autoSaveInterval", autoSaveInterval);
        preferences.putBoolean("colorBlindMode", colorBlindMode);
        preferences.putBoolean("highContrast", highContrast);
        preferences.putBoolean("reducedMotion", reducedMotion);
        preferences.putBoolean("screenReaderSupport", screenReaderSupport);
    }
    
    // Getters and setters
    public double getMasterVolume() { return masterVolume; }
    public void setMasterVolume(double masterVolume) { this.masterVolume = masterVolume; }
    
    public double getMusicVolume() { return musicVolume; }
    public void setMusicVolume(double musicVolume) { this.musicVolume = musicVolume; }
    
    public double getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(double sfxVolume) { this.sfxVolume = sfxVolume; }
    
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public boolean isAnimationsEnabled() { return animationsEnabled; }
    public void setAnimationsEnabled(boolean animationsEnabled) { this.animationsEnabled = animationsEnabled; }
    
    public boolean isParticleEffects() { return particleEffects; }
    public void setParticleEffects(boolean particleEffects) { this.particleEffects = particleEffects; }
    
    public int getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(int animationSpeed) { this.animationSpeed = animationSpeed; }
    
    public String getDefaultGameMode() { return defaultGameMode; }
    public void setDefaultGameMode(String defaultGameMode) { this.defaultGameMode = defaultGameMode; }
    
    public int getDefaultDifficulty() { return defaultDifficulty; }
    public void setDefaultDifficulty(int defaultDifficulty) { this.defaultDifficulty = defaultDifficulty; }
    
    public boolean isAutoSaveEnabled() { return autoSaveEnabled; }
    public void setAutoSaveEnabled(boolean autoSaveEnabled) { this.autoSaveEnabled = autoSaveEnabled; }
    
    public int getAutoSaveInterval() { return autoSaveInterval; }
    public void setAutoSaveInterval(int autoSaveInterval) { this.autoSaveInterval = autoSaveInterval; }
    
    public boolean isColorBlindMode() { return colorBlindMode; }
    public void setColorBlindMode(boolean colorBlindMode) { this.colorBlindMode = colorBlindMode; }
    
    public boolean isHighContrast() { return highContrast; }
    public void setHighContrast(boolean highContrast) { this.highContrast = highContrast; }
    
    public boolean isReducedMotion() { return reducedMotion; }
    public void setReducedMotion(boolean reducedMotion) { this.reducedMotion = reducedMotion; }
    
    public boolean isScreenReaderSupport() { return screenReaderSupport; }
    public void setScreenReaderSupport(boolean screenReaderSupport) { this.screenReaderSupport = screenReaderSupport; }
    
    private static class SettingsData {
        public double masterVolume;
        public double musicVolume;
        public double sfxVolume;
        public boolean muted;
        public String theme;
        public boolean animationsEnabled;
        public boolean particleEffects;
        public int animationSpeed;
        public String defaultGameMode;
        public int defaultDifficulty;
        public boolean autoSaveEnabled;
        public int autoSaveInterval;
        public boolean colorBlindMode;
        public boolean highContrast;
        public boolean reducedMotion;
        public boolean screenReaderSupport;
        
        public SettingsData() {}
        
        public SettingsData(double masterVolume, double musicVolume, double sfxVolume, boolean muted,
                          String theme, boolean animationsEnabled, boolean particleEffects, int animationSpeed,
                          String defaultGameMode, int defaultDifficulty, boolean autoSaveEnabled, int autoSaveInterval,
                          boolean colorBlindMode, boolean highContrast, boolean reducedMotion, boolean screenReaderSupport) {
            this.masterVolume = masterVolume;
            this.musicVolume = musicVolume;
            this.sfxVolume = sfxVolume;
            this.muted = muted;
            this.theme = theme;
            this.animationsEnabled = animationsEnabled;
            this.particleEffects = particleEffects;
            this.animationSpeed = animationSpeed;
            this.defaultGameMode = defaultGameMode;
            this.defaultDifficulty = defaultDifficulty;
            this.autoSaveEnabled = autoSaveEnabled;
            this.autoSaveInterval = autoSaveInterval;
            this.colorBlindMode = colorBlindMode;
            this.highContrast = highContrast;
            this.reducedMotion = reducedMotion;
            this.screenReaderSupport = screenReaderSupport;
        }
    }
}
