package org.example.grammar.math;

import java.util.List;
import java.util.Optional;

public sealed interface Pointer<A,SELF> {
    Optional<? extends A> get();
    boolean isEOF();
    SELF move(int offset);

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
