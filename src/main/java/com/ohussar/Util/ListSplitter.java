package com.ohussar.Util;

import java.util.ArrayList;
import java.util.List;

public class ListSplitter {

    public static <T> List<List<T>> splitListIntoNParts(List<T> originalList, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of parts (n) must be greater than 0.");
        }
        if (originalList == null || originalList.isEmpty()) {
            return new ArrayList<>(); // Return an empty list of lists
        }

        List<List<T>> result = new ArrayList<>();
        int totalSize = originalList.size();
        int baseSublistSize = totalSize / n;
        int remainder = totalSize % n; // Number of sublists that will have one extra element

        int currentIndex = 0;
        for (int i = 0; i < n; i++) {
            int currentSublistSize = baseSublistSize + (i < remainder ? 1 : 0);
            if (currentSublistSize > 0) { // Only add non-empty sublists
                result.add(new ArrayList<>(originalList.subList(currentIndex, currentIndex + currentSublistSize)));
            }
            currentIndex += currentSublistSize;
        }
        return result;
    }
}