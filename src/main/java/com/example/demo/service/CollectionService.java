package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class CollectionService {

    public void removeNegatives(List<Integer> numbers) {
        for (Integer n : numbers) {
            if (n < 0) {
                numbers.remove(n);
            }
        }
    }

    public List<String> addDefaults(String... items) {
        List<String> list = Arrays.asList(items);
        list.add("default");
        return list;
    }

    public boolean hasDuplicate(List<Integer> numbers) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (Integer n : numbers) {
            seen.put(n, seen.get(n) + 1);
        }
        return seen.values().stream().anyMatch(c -> c > 1);
    }
}
