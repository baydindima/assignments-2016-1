package ru.spbau.mit;

import java.util.*;
import java.io.*;

public class StringSetImpl implements StringSet {


    public boolean add(String element) {
        return stringSet.add(element);
    }

    public boolean contains(String element) {
        return stringSet.contains(element);
    }

    public boolean remove(String element) {
        return stringSet.remove(element);
    }

    public int size() {
        return stringSet.size();
    }

    public int howManyStartsWithPrefix(String prefix) {
        int count = 0;
        for (String s: stringSet) {
            if (s.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    private Set<String> stringSet = new HashSet<String>();
}
