package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;

import static org.example.grammar.math.Indent.indent;
import static org.example.grammar.math.Indent.nonBlankLines;

/**
 * Абстрактный узел синтаксиса - унарного оператора
 */
public abstract class UnaryAst extends AstBase {
    private final Ast value;

    /**
     * Конструктор
     * @param begin Указатель на начало в исходном тексте
     * @param end Указатель на конец в исходном тексте
     * @param value операнд
     */
    public UnaryAst(Pointer.ListPointer<Token> begin, Pointer.ListPointer<Token> end, Ast value) {
        super(begin, end);
        if (value == null) throw new IllegalArgumentException("value==null");
        this.value = value;
    }

    /**
     * Операнд
     * @return операнд
     */
    public Ast getValue() {
        return value;
    }

    @Override
    public String toString() {
        return
            nonBlankLines(
                this.getClass().getSimpleName() + "\n" +
                    "  value:\n" +
                    indent("    ", value.toString()) + "\n"
            );
    }
}
