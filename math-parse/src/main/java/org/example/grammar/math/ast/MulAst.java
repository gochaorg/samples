package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;
import org.example.grammar.math.sem.Num;

public class MulAst extends BinaryAst {
    public MulAst(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end, Ast left, Ast right) {
        super(begin, end, left, right);
    }

    @Override
    public Num eval() {
        return getLeft().eval().multiply( getRight().eval() );
    }
}
