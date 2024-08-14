package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.Optional;

/**
 * Лексема пробельных символов
 */
public class WhiteSpaceToken extends AbstractToken {
    public WhiteSpaceToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        super(begin, end);
    }

    /**
     * Парсер лексемы
     */
    public static final TokenParser parser = ptr ->
    {
        var ptrBegin = ptr;

        // Проверка первого пробела
        var next = ptr.get().flatMap(c -> c == ' '
            ? Optional.of(ptrBegin.move(1))
            : Optional.empty()
        );

        if( next.isEmpty() )return Optional.empty();

        // Получаем указатель на следующий возможный пробел
        ptr = next.get();

        // Проверка очередного пробела
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
