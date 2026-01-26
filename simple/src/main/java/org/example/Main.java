package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //sout
        System.out.println("Привіт козаки і козачки :)");
        // int, short, float, double, String, boolean, char
        Scanner scanner = new Scanner(System.in);
        System.out.println("Скільки Вам років:");
        String text = scanner.next();
        System.out.println("Вам "+ text + " років");

    }
}