package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

public class MinusToken extends AbstractToken {
    public MinusToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        super(begin, end);
    }

    public static final TokenParser parser = ptr ->
        ptr.get().flatMap(c -> c == '-'
            ? Optional.of(new MinusToken(ptr, ptr.move(1)))
            : Optional.empty()
        );
}