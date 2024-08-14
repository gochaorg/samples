package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;

public interface Ast {
    Pointer.ListPointer<Token> begin();
    Pointer.ListPointer<Token> end();
}
