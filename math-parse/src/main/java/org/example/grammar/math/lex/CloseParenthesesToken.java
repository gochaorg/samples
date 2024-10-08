package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

/**
 * Лексема - Закрывающая круглая скобка
 */
public class CloseParenthesesToken extends AbstractToken {
    public CloseParenthesesToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        super(begin, end);
    }

    /**
     * Парсер лексемы
     */
    public static final TokenParser parser = ptr ->
        ptr.get().flatMap(c -> c == ')'
            ? Optional.of(new CloseParenthesesToken(ptr, ptr.move(1)))
            : Optional.empty()
        );
}
