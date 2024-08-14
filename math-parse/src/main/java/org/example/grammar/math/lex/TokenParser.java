package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

/**
 * Парсер лексемы
 */
public interface TokenParser {
    Optional<Token> parseToken(Pointer.CharPointer ptr);
}
