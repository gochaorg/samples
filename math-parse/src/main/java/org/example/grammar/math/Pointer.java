package org.example.grammar.math;

import java.util.List;
import java.util.Optional;

/**
 * Указатель на исходник
 * @param <A> Тип данных в исходнике
 * @param <SELF> "Собственный" тип
 */
public sealed interface Pointer<A,SELF> {
    /**
     * Возвращает данные исходника
     * @return данные
     */
    Optional<? extends A> get();

    /**
     * Указатель вышел за пределы исходника
     * @return true - указатель вышел за пределы, например исходник не содержит данных
     */
    boolean isEOF();

    /**
     * Создание нового указателя
     * @param offset смещение
     * @return новый указатель
     */
    SELF move(int offset);

    /**
     * Указатель на символы исходного текста
     * @param source исходный текст
     * @param offset смещение в тексте то начала
     */
    public record CharPointer(String source, int offset) implements Pointer<Character, CharPointer> {
        public CharPointer {
            if( source==null ) throw new IllegalArgumentException("source==null");
        }

        @Override
        public Optional<Character> get() {
            if( offset<0 || offset>=source.length() )return Optional.empty();
            return Optional.of(source.charAt(offset));
        }

        @Override
        public boolean isEOF() {
            return offset >= source.length() || offset < 0;
        }

        @Override
        public CharPointer move(int offset) {
            return new CharPointer(source, this.offset + offset);
        }

        @Override
        public String toString() {
            return "CharPointer{" +
                "offset=" + offset +
                '}';
        }
    }

    /**
     * Указатель на лексемы в исходном списке
     * @param source исходный список
     * @param offset смещение
     * @param <A> тип лексемы
     */
    public record ListPointer<A>(List<A> source, int offset) implements Pointer<A, ListPointer<A>> {
        public ListPointer {
            if( source==null ) throw new IllegalArgumentException("source==null");
        }

        @Override
        public Optional<? extends A> get() {
            if( offset<0 || offset>=source.size() )return Optional.empty();
            return Optional.of(source.get(offset));
        }

        @Override
        public boolean isEOF() {
            return offset >= source.size() || offset < 0;
        }

        @Override
        public ListPointer<A> move(int offset) {
            return new ListPointer<>(source, this.offset + offset);
        }

        @Override
        public String toString() {
            return "ListPointer{" +
                "offset=" + offset +
                '}';
        }
    }
}
