package com.cache.bloomfilter.controller;


import com.cache.bloomfilter.dto.CheckResponse;
import com.cache.bloomfilter.dto.UserRequest;
import com.cache.bloomfilter.entity.User;
import com.cache.bloomfilter.repository.UserRepository;
import com.cache.bloomfilter.service.BloomFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BloomFilterService bloomFilterService;
    @GetMapping("/check-without-filter/{username}")
    public ResponseEntity<CheckResponse>checkWithoutFilter(@PathVariable  String username){
        long startTime = System.nanoTime();
        boolean ans = userRepository.existsByUserName(username);
        long duration = System.nanoTime() - startTime;
        double durationMs= duration/1000000;
        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setUsername(username);
        checkResponse.setExists(ans);
        checkResponse.setMethod("DB_DIRECT");
        checkResponse.setDurationMs(durationMs);
        checkResponse.setDatabaseQueried(true);
        return ResponseEntity.ok(checkResponse);
    }
    @GetMapping("/check-with-filter/{username}")
    public ResponseEntity<CheckResponse>checkWithFilter(@PathVariable String username){
        long startTime = System.nanoTime();
        boolean ans = bloomFilterService.mightExist(username);
        if(!ans){
            long duration = System.nanoTime() - startTime;
            double durationMs = duration/1000000;
            CheckResponse checkResponse = new CheckResponse();
            checkResponse.setUsername(username);
            checkResponse.setExists(ans);
            checkResponse.setMethod("BLOOM_FILTER");
            checkResponse.setDurationMs(durationMs);
            checkResponse.setDatabaseQueried(false);
            return ResponseEntity.ok(checkResponse);
        }
        else {

            boolean exists = userRepository.existsByUserName(username);
            long duration = System.nanoTime() - startTime;
            double durationMs = duration/1000000;
            CheckResponse checkResponse = new CheckResponse();
            checkResponse.setUsername(username);
            checkResponse.setExists(exists);
            checkResponse.setMethod("BLOOM_FILTER");
            checkResponse.setDurationMs(durationMs);
            checkResponse.setDatabaseQueried(true);
            return ResponseEntity.ok(checkResponse);
        }
    }
    @PostMapping("/add-username")
    public ResponseEntity<User>addUser(@RequestBody User user){
        User user1 = new User();
        user1.setUserId(user.getUserId());
        user1.setUserName(user.getUserName());
        userRepository.save(user1);
        bloomFilterService.addUser(user.getUserName());
        return ResponseEntity.ok(user1);

    }
}
