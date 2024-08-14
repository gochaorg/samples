package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

/**
 * Лексема - Оператор деление
 */
public class DivisionToken extends AbstractToken {
    public DivisionToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        super(begin, end);
    }

    /**
     * Парсер лексемы
     */
    public static final TokenParser parser = ptr ->
        ptr.get().flatMap(c -> c == '/'
            ? Optional.of(new DivisionToken(ptr, ptr.move(1)))
            : Optional.empty()
        );
}
