package com.connect4.util;

import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationManager {
    private static final Logger logger = LoggerFactory.getLogger(AnimationManager.class);
    
    public static void animatePieceDrop(Node piece, double targetY, double duration) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(duration), piece);
        transition.setToY(targetY);
        transition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        transition.play();
    }
    
    public static void animateColumnHover(Node column, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), column);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }
    
    public static void animateWinHighlight(Node highlight, double duration) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), highlight);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(3);
        fade.setAutoReverse(true);
        fade.play();
    }
    
    public static void animateButtonHover(Node button, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(150), button);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }
}
