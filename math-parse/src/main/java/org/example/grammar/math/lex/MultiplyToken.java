package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

public class MultiplyToken extends AbstractToken {
    public MultiplyToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        super(begin, end);
    }

    public static final TokenParser parser = ptr ->
        ptr.get().flatMap(c -> c == '*'
            ? Optional.of(new MultiplyToken(ptr, ptr.move(1)))
            : Optional.empty()
        );
}
