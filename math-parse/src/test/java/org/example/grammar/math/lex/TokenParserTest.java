package org.example.grammar.math.lex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SimplifiableAssertion")
public class TokenParserTest {
    @Test
    public void tokens(){
        var tokens = MathTokenParser.tokens("1 12 12. 12.3 .3 ()+-*/");
        tokens.forEach(System.out::println);

        assertTrue(tokens.size()==16);
        assertTrue(tokens.get(0) instanceof NumToken);
        assertTrue(((NumToken)tokens.get(0)).value().value().map(v -> v==1.0).orElse(false));
        assertTrue(tokens.get(1) instanceof WhiteSpaceToken);

        assertTrue(tokens.get(2) instanceof NumToken);
        assertTrue(((NumToken)tokens.get(2)).value().value().map(v -> v==12.0).orElse(false));
        assertTrue(tokens.get(3) instanceof WhiteSpaceToken);

        assertTrue(tokens.get(4) instanceof NumToken);
        assertTrue(((NumToken)tokens.get(4)).value().value().map(v -> v==12.0).orElse(false));
        assertTrue(tokens.get(5) instanceof WhiteSpaceToken);

        assertTrue(tokens.get(6) instanceof NumToken);
        assertTrue(((NumToken)tokens.get(6)).value().value().map(v -> v==12.3).orElse(false));
        assertTrue(tokens.get(7) instanceof WhiteSpaceToken);

        assertTrue(tokens.get(8) instanceof NumToken);
        assertTrue(((NumToken)tokens.get(8)).value().value().map(v -> v==0.3).orElse(false));
        assertTrue(tokens.get(9) instanceof WhiteSpaceToken);

        assertTrue(tokens.get(10) instanceof OpenParenthesesToken);
        assertTrue(tokens.get(11) instanceof CloseParenthesesToken);
        assertTrue(tokens.get(12) instanceof PlusToken);
        assertTrue(tokens.get(13) instanceof MinusToken);
        assertTrue(tokens.get(14) instanceof MultiplyToken);
        assertTrue(tokens.get(15) instanceof DivisionToken);

    }
}
