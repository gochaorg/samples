package org.example.grammar.math.ast;

import org.example.grammar.math.Indent;
import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;

import static org.example.grammar.math.Indent.indent;
import static org.example.grammar.math.Indent.nonBlankLines;

public abstract class BinaryAst extends AstBase {
    private final Ast left;
    private final Ast right;

    public BinaryAst(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end, Ast left, Ast right) {
        super(begin, end);
        if (left == null) throw new IllegalArgumentException("left==null");
        if (right == null) throw new IllegalArgumentException("right==null");
        this.left = left;
        this.right = right;
    }

    public Ast getLeft() {
        return left;
    }

    public Ast getRight() {
        return right;
    }

    @Override
    public String toString() {
        return
            nonBlankLines(
                this.getClass().getSimpleName() + "\n" +
                    "  left:\n" +
                    indent("    ", left.toString()) + "\n" +
                    "  right:\n" +
                    indent("    ", right.toString()) + "\n"
            )
            ;
    }
}
