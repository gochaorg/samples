package org.example.grammar.math.ast;

import org.example.grammar.math.Indent;
import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.NumToken;
import org.example.grammar.math.lex.Token;
import org.example.grammar.math.sem.Num;

/**
 * Абстрактный узел синтаксиса - число
 */
public class NumAst extends AstBase {
    private final NumToken numToken;

    public NumAst(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end, NumToken numToken) {
        super(begin, end);
        if( numToken==null ) throw new IllegalArgumentException("numToken==null");
        this.numToken = numToken;
    }

    public NumToken getNumToken() {
        return numToken;
    }

    @Override
    public String toString() {
        return "NumAst\n" +
            "  value:\n" +
            Indent.indent("    ",getNumToken().toString());
    }

    @Override
    public Num eval() {
        return numToken.value();
    }
}
