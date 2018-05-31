package com.dabek.jakub.bpptree;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        BppTree<Integer> tree = new BppTree<>(Arrays.asList(1, 10, 20, 30, 40));
        System.out.println(tree);
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            tree.add(value);
            System.out.println(tree);
        }
    }
}
