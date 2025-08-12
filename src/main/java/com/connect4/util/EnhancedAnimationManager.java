package com.connect4.util;

import javafx.animation.*;
import javafx.scene.effect.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

public class EnhancedAnimationManager {
    
    private Pane particleSystem;
    private Timeline particleTimeline;
    
    public void initializeParticleSystem(Pane container) {
        this.particleSystem = container;
    }
    
    public void animatePieceDrop(Circle circle, Color targetColor) {
        // Create falling animation
        TranslateTransition fall = new TranslateTransition(Duration.millis(500), circle);
        fall.setFromY(-300);
        fall.setToY(0);
        
        // Add bounce effect
        BounceInterpolator bounce = new BounceInterpolator();
        fall.setInterpolator(bounce);
        
        // Color transition
        FillTransition color = new FillTransition(Duration.millis(500), circle);
        color.setToValue(targetColor);
        
        ParallelTransition animation = new ParallelTransition(fall, color);
        animation.play();
    }
    
    public void animateWinningPositions(Circle[][] circles, List<int[]> winningPositions) {
        for (int[] pos : winningPositions) {
            Circle circle = circles[pos[0]][pos[1]];
            
            // Create glow effect
            Glow glow = new Glow();
            glow.setLevel(0.8);
            circle.setEffect(glow);
            
            // Pulse animation
            ScaleTransition pulse = new ScaleTransition(Duration.millis(500), circle);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.2);
            pulse.setToY(1.2);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(Animation.INDEFINITE);
            pulse.play();
        }
    }
    
    public void pulseEffect(Circle circle) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), circle);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }
    
    public Glow createGlowEffect() {
        Glow glow = new Glow();
        glow.setLevel(0.3);
        return glow;
    }
    
    public void createParticleEffect(double x, double y, Color color) {
        if (particleSystem == null) return;
        
        for (int i = 0; i < 10; i++) {
            Circle particle = new Circle(3);
            particle.setFill(color);
            particle.setCenterX(x);
            particle.setCenterY(y);
            
            // Random direction
            double angle = Math.random() * 2 * Math.PI;
            double distance = 50 + Math.random() * 50;
            
            TranslateTransition move = new TranslateTransition(Duration.millis(1000), particle);
            move.setToX(Math.cos(angle) * distance);
            move.setToY(Math.sin(angle) * distance);
            
            FadeTransition fade = new FadeTransition(Duration.millis(1000), particle);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            
            ParallelTransition animation = new ParallelTransition(move, fade);
            animation.setOnFinished(e -> particleSystem.getChildren().remove(particle));
            
            particleSystem.getChildren().add(particle);
            animation.play();
        }
    }
    
    public void createConfettiEffect(double x, double y) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE};
        
        for (int i = 0; i < 20; i++) {
            Circle confetti = new Circle(5);
            confetti.setFill(colors[i % colors.length]);
            confetti.setCenterX(x);
            confetti.setCenterY(y);
            
            // Random trajectory
            double angle = Math.random() * 2 * Math.PI;
            double distance = 100 + Math.random() * 100;
            
            TranslateTransition move = new TranslateTransition(Duration.millis(2000), confetti);
            move.setToX(Math.cos(angle) * distance);
            move.setToY(Math.sin(angle) * distance - 100); // Add gravity effect
            
            RotateTransition rotate = new RotateTransition(Duration.millis(2000), confetti);
            rotate.setByAngle(360);
            
            FadeTransition fade = new FadeTransition(Duration.millis(2000), confetti);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            
            ParallelTransition animation = new ParallelTransition(move, rotate, fade);
            animation.setOnFinished(e -> {
                if (particleSystem != null) {
                    particleSystem.getChildren().remove(confetti);
                }
            });
            
            if (particleSystem != null) {
                particleSystem.getChildren().add(confetti);
                animation.play();
            }
        }
    }
    
    public void animateBackground(Pane background) {
        // Create animated background
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(background.opacityProperty(), 0.5)),
            new KeyFrame(Duration.seconds(2), new KeyValue(background.opacityProperty(), 1.0)),
            new KeyFrame(Duration.seconds(4), new KeyValue(background.opacityProperty(), 0.5))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public void createRippleEffect(Circle circle) {
        Circle ripple = new Circle(circle.getRadius() * 2);
        ripple.setFill(Color.TRANSPARENT);
        ripple.setStroke(circle.getFill());
        ripple.setStrokeWidth(2);
        ripple.setCenterX(circle.getCenterX());
        ripple.setCenterY(circle.getCenterY());
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), ripple);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(2.0);
        scale.setToY(2.0);
        
        FadeTransition fade = new FadeTransition(Duration.millis(500), ripple);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        
        ParallelTransition animation = new ParallelTransition(scale, fade);
        animation.setOnFinished(e -> {
            if (particleSystem != null) {
                particleSystem.getChildren().remove(ripple);
            }
        });
        
        if (particleSystem != null) {
            particleSystem.getChildren().add(ripple);
            animation.play();
        }
    }
}
