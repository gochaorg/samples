package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;
import org.example.grammar.math.sem.Num;

/**
 * Абстрактный узел синтаксиса - унарный минус
 */
public class UnaryMinusAst extends UnaryAst {
    public UnaryMinusAst(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end, Ast value) {
        super(begin, end, value);
    }

    @Override
    public Num eval() {
        return new Num(0).minus(getValue().eval());
    }
}