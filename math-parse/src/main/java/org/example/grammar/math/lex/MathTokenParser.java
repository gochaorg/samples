package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

import java.util.ArrayList;
import java.util.List;

public class MathTokenParser {
    public static final TokenParser parser = ptr ->
        WhiteSpaceToken.parser.parseToken(ptr)
            .or(() -> NumToken.parser.parseToken(ptr))
            .or(() -> CloseParenthesesToken.parser.parseToken(ptr))
            .or(() -> OpenParenthesesToken.parser.parseToken(ptr))
            .or(() -> PlusToken.parser.parseToken(ptr))
            .or(() -> MinusToken.parser.parseToken(ptr))
            .or(() -> MultiplyToken.parser.parseToken(ptr))
            .or(() -> DivisionToken.parser.parseToken(ptr))
        ;

    public static List<Token> tokens(String source){
        if( source==null ) throw new IllegalArgumentException("source==null");

        var tokens = new ArrayList<Token>();
        var ptr = new Pointer.CharPointer(source,0);
        while (!ptr.isEOF()){
            var tokenOpt = parser.parseToken(ptr);
            if( tokenOpt.isEmpty() ){
                throw new Error("unparsed token at "+ptr.offset()+": "+ptr.source().substring(ptr.offset()));
            }

            var token = tokenOpt.get();
            tokens.add(token);

            ptr = token.end();
        }

        return tokens;
    }

    public static Pointer.ListPointer<Token> tokenPointerOf(String source){
        var tokens = tokens(source);
        return new Pointer.ListPointer<>(tokens, 0);
    }
}
