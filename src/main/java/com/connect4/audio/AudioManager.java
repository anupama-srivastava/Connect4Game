package com.connect4.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static final Logger logger = LoggerFactory.getLogger(AudioManager.class);
    
    private static AudioManager instance;
    private final Map<String, AudioClip> soundEffects = new HashMap<>();
    private MediaPlayer backgroundMusicPlayer;
    private double masterVolume = 0.7;
    private double musicVolume = 0.5;
    private double sfxVolume = 0.8;
    private boolean muted = false;
    
    private AudioManager() {
        initializeSounds();
    }
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    private void initializeSounds() {
        try {
            // Sound effects
            loadSoundEffect("piece_drop", "/sounds/piece_drop.wav");
            loadSoundEffect("win", "/sounds/win.wav");
            loadSoundEffect("draw", "/sounds/draw.wav");
            loadSoundEffect("button_click", "/sounds/button_click.wav");
            loadSoundEffect("hover", "/sounds/hover.wav");
            loadSoundEffect("invalid_move", "/sounds/invalid_move.wav");
            
            // Background music
            loadBackgroundMusic("/sounds/background_music.mp3");
            
        } catch (Exception e) {
            logger.warn("Some audio files not found, continuing without sound: {}", e.getMessage());
        }
    }
    
    private void loadSoundEffect(String name, String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toString());
                soundEffects.put(name, clip);
            }
        } catch (Exception e) {
            logger.warn("Failed to load sound effect: {}", name);
        }
    }
    
    private void loadBackgroundMusic(String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource != null) {
                Media media = new Media(resource.toString());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusicPlayer.setVolume(musicVolume * masterVolume);
            }
        } catch (Exception e) {
            logger.warn("Failed to load background music");
        }
    }
    
    public void playSoundEffect(String effectName) {
        if (muted || !soundEffects.containsKey(effectName)) return;
        
        AudioClip clip = soundEffects.get(effectName);
        clip.setVolume(sfxVolume * masterVolume);
        clip.play();
    }
    
    public void playBackgroundMusic() {
        if (muted || backgroundMusicPlayer == null) return;
        backgroundMusicPlayer.play();
    }
    
    public void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }
    
    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
        updateVolumes();
    }
    
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(musicVolume * masterVolume);
        }
    }
    
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }
    
    private void updateVolumes() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(musicVolume * masterVolume);
        }
    }
    
    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            pauseBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
    }
    
    public boolean isMuted() {
        return muted;
    }
    
    public double getMasterVolume() { return masterVolume; }
    public double getMusicVolume() { return musicVolume; }
    public double getSfxVolume() { return sfxVolume; }
}
