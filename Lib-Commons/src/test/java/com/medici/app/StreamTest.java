package com.medici.app;

import java.util.*;
import java.util.stream.Stream;

public class StreamTest {


    private static List<Integer> odd(List<Integer> numbers) {
        List<Integer> result = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .toList();

        return result;
    }


    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        List<Integer> result = odd(numbers);
        result.forEach(res ->
                System.out.println(res.toString())
        );

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        List<Integer> nummers = numbers.stream().map(n -> n * 5).toList();
        nummers.forEach(n -> System.out.println(n.toString()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        numbers.stream().sorted(Comparator.reverseOrder()).toList().forEach(n -> System.out.println(n.toString()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Optional<Integer> numerAny = numbers.stream().filter(n -> n > 5).findAny();
        numerAny.ifPresent(n -> System.out.print(n.toString()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Integer resultex = numbers.stream().reduce(1000, (t, p) -> t + p);
        System.out.print(resultex.toString());

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        result.stream().filter(res -> res < 100).toList().forEach(r -> System.out.println(r.toString()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Stream<Integer> inStream = Stream.iterate(5, t -> t + 5).limit(50);
        inStream.forEach(t -> System.out.println(t.toString()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Stream<Integer> outStream = Stream.iterate(5, n -> n <= 250, n -> n + 5);
        outStream.forEach(n -> System.out.println(n.toString()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Deque<Integer> stack = new ArrayDeque<>();
        stack.addFirst(10);
        stack.addFirst(5);
        stack.push(25);
        stack.push(125);
        stack.push(525);
        stack.push(235);
        stack.push(245);
        stack.push(725);
        stack.push(1025);
        stack.push(800);
        stack.push(400);
        stack.push(200);
        stack.addLast(50);
        stack.stream().forEach(n -> printer(n));

        printer(stack.peekFirst());
        printer(stack.peekLast());

        stack.add(75);
        stack.push(100);
        printer(stack.getFirst());
        printer(stack.getLast());

        printer(stack.peekFirst());
        printer(stack.peekLast());
        stack.removeFirst();    // removes 100
        stack.removeLast();     // removes 200

        stack.pollFirst();      // removes from front
        stack.pollLast();       // removes from back
        stack.pop();            // same as removeFirst()


// =========================
// SEARCH
// =========================

        System.out.println(stack.contains(10));
        System.out.println(stack.contains(999));

// =========================
// SIZE / EMPTY
// =========================

        printer(stack.size());
        System.out.println(stack.isEmpty());

// =========================
// ITERATION
// =========================

// First → Last
        for (Integer n : stack) {
            printer(n);
        }

// Last → First
        Iterator<Integer> iterator = stack.descendingIterator();

        while (iterator.hasNext()) {
            printer(iterator.next());
        }


// =========================
// STREAM
// =========================

        stack.stream().forEach(System.out::println);

// =========================
// REMOVE SPECIFIC VALUE
// =========================

        stack.removeFirstOccurrence(10);
        stack.removeLastOccurrence(10);

// =========================
// CLEAR
// =========================

        stack.clear();
        System.out.println(stack.isEmpty());

        PriorityQueue<Integer> queue = new PriorityQueue<>();

        queue.add(50);
        queue.add(10);
        queue.add(30);
        queue.add(20);

        printer(queue.poll());

        queue.add(200);
        queue.add(220);
        queue.add(240);
        queue.add(280);
        queue.add(285);
        queue.add(400);
        queue.add(422);

        printer(queue.peek());

        System.out.println(queue);

        queue.removeIf(n -> n < 100);
        printer(queue.peek());
        printer(queue.poll());
        printer(queue.size());

        StringBuilder text = new StringBuilder("Tiago Pratis Medice");
        text.repeat(' ', 2);
        text.repeat("Tiago Pratis Medice ", 2);
        System.out.println(text);

        System.out.println(text.reverse().toString());
    }

    public static void printer(Integer nummer) {
        System.out.println("\n ------- \n");
        System.out.println(nummer);
    }

    public static String reverseWord(String word) {
        return new StringBuilder(word).reverse().toString();
    }
}
