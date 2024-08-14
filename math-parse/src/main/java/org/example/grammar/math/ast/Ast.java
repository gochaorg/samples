package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;
import org.example.grammar.math.sem.Num;

public interface Ast {
    Pointer.ListPointer<Token> begin();
    Pointer.ListPointer<Token> end();
    Num eval();
}
