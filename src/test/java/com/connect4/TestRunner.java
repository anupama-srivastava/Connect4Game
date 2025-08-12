package com.connect4;

import com.connect4.model.GameBoardTest;
import com.connect4.ai.MinimaxAITest;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("=== Connect4Game Testing Framework ===");
        System.out.println("Running comprehensive test suite...");
        
        // Create test launcher
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        // Create test request
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                selectClass(GameBoardTest.class),
                selectClass(MinimaxAITest.class)
            )
            .build();
        
        // Execute tests
        launcher.execute(request, listener);
        
        // Print results
        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println("\n=== Test Results ===");
        System.out.println("Total tests: " + summary.getTestsStartedCount());
        System.out.println("Passed: " + summary.getTestsSucceededCount());
        System.out.println("Failed: " + summary.getTestsFailedCount());
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\nFailed tests:");
            summary.getFailures().forEach(failure -> 
                System.out.println(" - " + failure.getTestIdentifier().getDisplayName() + ": " + failure.getException().getMessage())
            );
        }
        
        System.out.println("\n=== Testing Complete ===");
    }
}
