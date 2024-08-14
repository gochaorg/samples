package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.sem.Num;

import java.util.Optional;

public class NumToken extends AbstractToken {
    public NumToken(Pointer.CharPointer begin, Pointer.CharPointer end, Num value) {
        super(begin, end);
        if (value == null) throw new IllegalArgumentException("num==null");
        this.value = value;
    }

    private final Num value;

    public Num value() {return value;}

    private static Optional<Integer> digitOf(Pointer.CharPointer ptr) {
        return ptr.get().flatMap(c -> switch (c) {
            case '0' -> Optional.of(0);
            case '1' -> Optional.of(1);
            case '2' -> Optional.of(2);
            case '3' -> Optional.of(3);
            case '4' -> Optional.of(4);
            case '5' -> Optional.of(5);
            case '6' -> Optional.of(6);
            case '7' -> Optional.of(7);
            case '8' -> Optional.of(8);
            case '9' -> Optional.of(9);
            default -> Optional.empty();
        });
    }

    private record IntPart(String value, Pointer.CharPointer begin, Pointer.CharPointer end) {}

    private static Optional<IntPart> intNumParse(Pointer.CharPointer ptr) {
        var digit0 = digitOf(ptr);
        if (digit0.isEmpty()) return Optional.empty();

        var sb = new StringBuilder();
        sb.append(digit0.get().toString());

        var begin = ptr;
        ptr = ptr.move(1);

        while (true) {
            var digit1 = digitOf(ptr);
            if (digit1.isEmpty()) break;

            sb.append(digit1.get().toString());
            ptr = ptr.move(1);
        }

        return Optional.of(new IntPart(sb.toString(), begin, ptr));
    }

    public static final TokenParser parser = ptr -> {
        if (ptr.get().map(c -> c == '.').orElse(false)) {
            return intNumParse(ptr.move(1))
                .map(p -> new NumToken(
                    ptr,
                    p.end(),
                    new Num(Double.parseDouble("0."+p.value()))));
        }

        var intPartOpt = intNumParse(ptr);
        if( intPartOpt.isEmpty() )return Optional.empty();

        var intPart = intPartOpt.get();

        if (intPart.end().get().map(c -> c == '.').orElse(false)) {
            Optional<Token> res = intNumParse(intPart.end().move(1))
                .map(p -> new NumToken(
                    ptr,
                    p.end(),
                    new Num(Double.parseDouble(
                        intPart.value()+"."+
                        p.value()
                    )))
                );

            if(res.isPresent()) return res;

            return Optional.of(
                new NumToken(ptr, intPart.end().move(1),
                    new Num( Double.parseDouble(intPart.value()) ))
            );
        }

        return Optional.of(
            new NumToken(ptr, intPart.end(), new Num(
                Double.parseDouble(intPart.value())
            ))
        );
    };

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
            "begin=" + begin() +
            ", end=" + end() +
            ", value="+value()+
            '}';
    }
}
