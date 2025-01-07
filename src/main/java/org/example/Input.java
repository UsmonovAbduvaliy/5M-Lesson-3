package org.example;

import java.util.Scanner;

public interface Input {
    static int num(String msg){
            System.out.print(msg);
            return new Scanner( System.in ).nextInt();
        }
        static String str(String msg){
            System.out.print(msg);
            return new Scanner( System.in ).nextLine();
        }
}
