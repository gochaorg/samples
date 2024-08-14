package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;
import org.example.grammar.math.sem.Num;

public class ParenthesesAst extends UnaryAst {
    public ParenthesesAst(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end, Ast value) {
        super(begin, end, value);
    }

    @Override
    public Num eval() {
        return getValue().eval();
    }
}
