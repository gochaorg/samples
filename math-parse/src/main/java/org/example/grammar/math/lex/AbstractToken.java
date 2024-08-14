package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

public abstract class AbstractToken implements Token {
    private final Pointer.CharPointer begin;
    private final Pointer.CharPointer end;

    public AbstractToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        if( begin==null ) throw new IllegalArgumentException("begin==null");
        if( end==null ) throw new IllegalArgumentException("end==null");
        this.begin = begin;
        this.end = end;
    }

    @Override
    public Pointer.CharPointer begin() {
        return begin;
    }

    @Override
    public Pointer.CharPointer end() {
        return end;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
            "begin=" + begin +
            ", end=" + end +
            '}';
    }
}
