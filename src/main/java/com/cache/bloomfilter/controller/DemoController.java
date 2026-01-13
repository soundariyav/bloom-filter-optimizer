package com.cache.bloomfilter.controller;
import com.cache.bloomfilter.dto.BulkTestResult;
import com.cache.bloomfilter.dto.CheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoController {
    @Autowired
    private UserController userController;

    /**
     * Run bulk test to compare performance with and without Bloom Filter
     */
    @PostMapping("/bulk-test")
    public ResponseEntity<BulkTestResult> runBulkTest(@RequestParam(defaultValue = "1000") int count) {

        System.out.println("Starting bulk test with " + count + " queries");

        // Generate test usernames (mix of existing and non-existing)
        List<String> testUsernames = generateTestUsernames(count);

        int withFilterDbQueries = 0;
        int withoutFilterDbQueries = count; // Always queries DB
        int falsePositives = 0;

        double withFilterTotalTime = 0;
        double withoutFilterTotalTime = 0;

        // Test each username
        for (int i = 0; i < testUsernames.size(); i++) {
            String username = testUsernames.get(i);

            // Progress indicator
            if (i % 100 == 0) {
                System.out.println("Progress: " + i + "/" + count);
            }

            // Test WITH filter
            CheckResponse withFilter = userController.checkWithFilter(username).getBody();
            if (withFilter != null) {
                if (withFilter.isDatabaseQueried()) {
                    withFilterDbQueries++;

                    // Check for false positive (filter said "might exist" but doesn't)
                    if (!withFilter.isExists()) {
                        falsePositives++;
                    }
                }
                withFilterTotalTime += withFilter.getDurationMs();
            }

            // Test WITHOUT filter
            CheckResponse withoutFilter = userController.checkWithoutFilter(username).getBody();
            if (withoutFilter != null) {
                withoutFilterTotalTime += withoutFilter.getDurationMs();
            }
        }

        // Calculate percentages
        double queriesSavedPercentage = 0;
        double timeSavedPercentage = 0;

        if (withoutFilterDbQueries > 0) {
            queriesSavedPercentage = ((double)(withoutFilterDbQueries - withFilterDbQueries) / withoutFilterDbQueries) * 100;
        }

        if (withoutFilterTotalTime > 0) {
            timeSavedPercentage = ((withoutFilterTotalTime - withFilterTotalTime) / withoutFilterTotalTime) * 100;
        }

        // Create result
        BulkTestResult result = new BulkTestResult();
        result.setTotalQueries(count);
        result.setWithFilterDbQueries(withFilterDbQueries);
        result.setWithoutFilterDbQueries(withoutFilterDbQueries);
        result.setWithFilterTotalTimeMs(withFilterTotalTime);
        result.setWithoutFilterTotalTimeMs(withoutFilterTotalTime);
        result.setQueriesSavedPercentage(queriesSavedPercentage);
        result.setTimeSavedPercentage(timeSavedPercentage);
        result.setFalsePositives(falsePositives);

        // Print results
        System.out.println("========================================");
        System.out.println("Bulk Test Results:");
        System.out.println("   Total queries: " + result.getTotalQueries());
        System.out.println("   DB queries WITH filter: " + result.getWithFilterDbQueries());
        System.out.println("   DB queries WITHOUT filter: " + result.getWithoutFilterDbQueries());
        System.out.println("   Queries saved: " + String.format("%.2f%%", result.getQueriesSavedPercentage()));
        System.out.println("   Time WITH filter: " + String.format("%.2f ms", result.getWithFilterTotalTimeMs()));
        System.out.println("   Time WITHOUT filter: " + String.format("%.2f ms", result.getWithoutFilterTotalTimeMs()));
        System.out.println("   Time saved: " + String.format("%.2f%%", result.getTimeSavedPercentage()));
        System.out.println("   False positives: " + falsePositives + " (" +
                String.format("%.2f%%", (falsePositives * 100.0) / count) + ")");
        System.out.println("========================================");

        return ResponseEntity.ok(result);
    }

    /**
     * Generate mix of existing and non-existing usernames for testing
     */
    private List<String> generateTestUsernames(int count) {
        List<String> usernames = new ArrayList<>();
        Random random = new Random();
        String[] prefixes = {"user_", "test_", "admin_", "guest_"};

        for (int i = 0; i < count; i++) {
            if (random.nextDouble() < 0.5) {
                // 50% existing users
                String prefix = prefixes[random.nextInt(prefixes.length)];

                // Generate ID based on prefix ranges
                int maxId = switch (prefix) {
                    case "user_" -> 30000;
                    case "test_" -> 10000;
                    case "admin_" -> 5000;
                    case "guest_" -> 5000;
                    default -> 10000;
                };

                int id = random.nextInt(maxId) + 1;
                usernames.add(prefix + id);
            } else {
                // 50% non-existing users
                usernames.add("nonexistent_" + UUID.randomUUID().toString().substring(0, 8));
            }
        }

        return usernames;
    }
}
