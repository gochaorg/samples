package org.example.junit;

public interface T {
    /**
     * Стабильные баги
     */
    final static String Stable = "Stable";

    /**
     * Не стабильные баги, вероятность бага Б= 10%, тестировать по 10 или более раз
     */
    final static String UnStable = "UnStable";

    /**
     * Тест не требует предварительной настройки
     */
    final static String Atomic = "Atomic";

    /**
     * Тестирование MSSQL
     */
    final static String MSSQL = "MSSQL";
}
