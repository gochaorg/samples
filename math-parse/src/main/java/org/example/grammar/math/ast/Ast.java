package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.Token;
import org.example.grammar.math.sem.Num;

/**
 * Узел абстрактного синтаксического дерева
 */
public interface Ast {
    /**
     * Указатель на начало в исходном тексте
     * @return начало узла
     */
    Pointer.ListPointer<Token> begin();

    /**
     * Указатель на конец в исходном тексте
     * @return конец узла
     */
    Pointer.ListPointer<Token> end();

    /**
     * Интерпретация значения
     * @return значение
     */
    Num eval();
}
