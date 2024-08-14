package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

public interface Token {
    Pointer.CharPointer begin();
    Pointer.CharPointer end();
}
