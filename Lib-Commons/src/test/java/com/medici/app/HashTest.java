package com.medici.app;

import java.util.*;

public class HashTest {

    public static int getFirstDuplicated(int[] nums) {

        if (nums.length > 100000)
            throw new RuntimeException();

        Set<Integer> seen = new HashSet<>();

        int result = 0;

        for (int num : nums) {
            if (!seen.add(num))
                return num;
        }

        return result;
    }

    public static int firstUniqueChar(String input) {

        int index = 0;

        Map<Character, Integer> counts = new LinkedHashMap<>();

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            Integer val = counts.getOrDefault(current, 0) + 1;
            counts.put(current, val);
        }

        for (Map.Entry<Character, Integer> count : counts.entrySet()) {
            if (count.getValue() == 1)
                return input.indexOf(count.getKey());
        }
        return index;
    }

    public static int[] twoSum(int[] nums, int target) {

        int calculation = 0;

        for (int i = 0; i < nums.length; i++) {
            int current = nums[i];

            if (current > target)
                continue;

            calculation = target - current;

            for (int j = 1 + i; j < nums.length; j++) {
                if (nums[j] == calculation) {
                    return new int[]{i, j};
                }
            }
        }

        return new int[0];
    }


    public static int[] twoSumMap(int[] nums, int target) {

        Map<Integer, Integer> indexes = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {

            int calculation = target - nums[i];

            if (indexes.containsKey(calculation)) {
                return new int[]{indexes.get(calculation), i};
            }

            indexes.put(nums[i], i);
        }

        return new int[0];
    }

    public static void main(String[] args) {

        int[] numx = {2, 8, 7, 9, 11, 15, 5, 6, 8, 3, 2};

        int[] rexultMap = twoSumMap(numx, 8);
        System.out.println(Arrays.toString(rexultMap));

        int[] rexult = twoSum(numx, 8);
        System.out.println(Arrays.toString(rexult));

        String name = "TIAGOPRATISMEDICE";
        int indexName = firstUniqueChar(name);
        System.out.println(indexName);


        int[] nums = {1, 2, 5, 10, 8, 6, 9, 69, 7, 87, 77, 33, 5, 2, 1};
        int result = getFirstDuplicated(nums);
        System.out.println(result);
    }
}
