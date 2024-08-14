package org.example.grammar.math;

import java.util.Arrays;

/**
 * Функции форматирования текста
 */
public class Indent {
    /**
     * Добавляет отступ в начале каждой строки
     * @param indent отступ
     * @param text текст
     * @return текст с отступами
     */
    public static String indent(String indent, String text) {
        if (indent == null) throw new IllegalArgumentException("indent==null");
        if (text == null) throw new IllegalArgumentException("text==null");
        return Arrays.stream(text.split("\r?\n")).map(line -> indent + line).reduce("", (sum, it) -> sum.isEmpty() ? it : sum + "\n" + it);
    }

    /**
     * Удаляет пустые строки
     * @param text текст
     * @return текст бех пустых строк
     */
    public static String nonBlankLines(String text) {
        if (text == null) throw new IllegalArgumentException("text==null");
        return Arrays.stream(text.split("\r?\n")).filter(line -> !line.isBlank()).reduce("", (sum, it) -> sum.isEmpty() ? it : sum + "\n" + it);
    }
}
