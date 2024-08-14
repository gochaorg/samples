package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

public class WhiteSpaceToken extends AbstractToken {
    public WhiteSpaceToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        super(begin, end);
    }

    public static final TokenParser parser = ptr ->
    {
        var ptrBegin = ptr;

        var next = ptr.get().flatMap(c -> c == ' '
            ? Optional.of(ptrBegin.move(1))
            : Optional.empty()
        );

        if( next.isEmpty() )return Optional.empty();
        ptr = next.get();

        while (true){
            var fptr2 = ptr;
            var next2 = ptr.get().flatMap(c -> c == ' '
                ? Optional.of(fptr2.move(1))
                : Optional.empty()
            );

            if( next2.isEmpty() )break;
            ptr = next2.get();
        }

        return Optional.of(
            new WhiteSpaceToken(ptrBegin, ptr)
        );
    };
}
