package com.cache.bloomfilter.service;
import com.cache.bloomfilter.entity.User;
import com.cache.bloomfilter.repository.UserRepository;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloomFilterService {
    private List<String> usernames;

    private BloomFilter<String> bloomFilter;
    private long totalUsers = 0;
    // Constructor
    @Autowired
    private UserRepository userRepository;
    public BloomFilterService() {
        this.bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(Charsets.UTF_8),
                50000,
                0.01);
        System.out.println("Bloom Filter created");
    }
    @PostConstruct
    void initializeFilter() {
        usernames = userRepository.findAllUserNames();
        totalUsers = usernames.size();
        System.out.println("Total users: " + usernames.size());
        System.out.println("Adding to the BloomFilter");
        for(String x  : usernames) {
            bloomFilter.put(x);
        }
    }
    public boolean mightExist(String x) {
        return bloomFilter.mightContain(x);
    }
    public void addUser(String username) {
        bloomFilter.put(username);
        totalUsers++;
    }

}
