package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

/**
 * Лексема
 */
public interface Token {
    /**
     * Указатель на начало в исходном тексте
     * @return начало лексемы
     */
    Pointer.CharPointer begin();

    /**
     * Указатель на конец в исходном тексте
     * @return конец лексемы
     */
    Pointer.CharPointer end();
}
