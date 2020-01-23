package xyz.cofe.test.chrfrm;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Main {
    private static int countArgs(@NonNull String[] args) {
        return args.length;
    }

    public static void main(@Nullable String[] args) {
        System.out.println(countArgs(args));
    }
//    public static void main(String[] args){
//        System.out.println("hello");
//    }
}
