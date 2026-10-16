package com.medici.app.model;

import java.util.Scanner;

public class MaxCardCount {

    public static void main(String[] args) {
        // Read input values
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] cards = new int[n];

        for (int i = 0; i < n; i++) {
            cards[i] = scanner.nextInt();
        }

        // Find the maximum number of cards that can be picked up
        int result = maxCardCount(n, cards);

        // Print the result
        System.out.println(result);
    }

    static int maxCardCount(int n, int[] card) {
        return dp(0, n, card, 0, 0);
    }

    static int dp(int i, int n, int[] card, int sum, int total) {
        if (i == n) {
            return total;
        } else if (sum + card[i] >= 1) {
            total += 1;
            sum += card[i];
            return dp(i + 1, n, card, sum , total);
        } else {
            return dp(i + 1, n, card, sum, total);
        }
    }
}
