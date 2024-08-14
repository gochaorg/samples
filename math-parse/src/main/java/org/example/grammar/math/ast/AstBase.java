package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;

public abstract class AstBase implements Ast {
    private final Pointer.ListPointer<Token> begin;
    private final Pointer.ListPointer<Token> end;

    public AstBase(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end) {
        if( begin==null ) throw new IllegalArgumentException("begin==null");
        if( end==null ) throw new IllegalArgumentException("end==null");
        this.begin = begin;
        this.end = end;
    }

    @Override
    public Pointer.ListPointer<Token> begin() {
        return begin;
    }

    @Override
    public Pointer.ListPointer<Token> end() {
        return end;
    }
}
