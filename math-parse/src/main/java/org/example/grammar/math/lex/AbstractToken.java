package org.example.grammar.math.lex;

import org.example.grammar.math.Pointer;

/**
 * Абстрактная лексема
 */
public abstract class AbstractToken implements Token {
    /**
     * Указатель на начало в исходном тексте
     */
    private final Pointer.CharPointer begin;

    /**
     * Указатель на конец в исходном тексте
     */
    private final Pointer.CharPointer end;

    /**
     * Конструктор лексемы
     * @param begin Указатель на начало в исходном тексте
     * @param end Указатель на конец в исходном тексте
     */
    public AbstractToken(Pointer.CharPointer begin, Pointer.CharPointer end) {
        if( begin==null ) throw new IllegalArgumentException("begin==null");
        if( end==null ) throw new IllegalArgumentException("end==null");
        this.begin = begin;
        this.end = end;
    }

    /**
     * Возвращает указатель на начало в исходном тексте
     * @return начало в исходном тексте
     */
    @Override
    public Pointer.CharPointer begin() {
        return begin;
    }

    /**
     * Возвращает указатель на конец в исходном тексте
     * @return конец в исходном тексте
     */
    @Override
    public Pointer.CharPointer end() {
        return end;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
            "begin=" + begin +
            ", end=" + end +
            '}';
    }
}
