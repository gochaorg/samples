package org.example.grammar.math.ast;

import org.example.grammar.math.Pointer;
import org.example.grammar.math.lex.CloseParenthesesToken;
import org.example.grammar.math.lex.DivisionToken;
import org.example.grammar.math.lex.MinusToken;
import org.example.grammar.math.lex.MultiplyToken;
import org.example.grammar.math.lex.NumToken;
import org.example.grammar.math.lex.OpenParenthesesToken;
import org.example.grammar.math.lex.PlusToken;
import org.example.grammar.math.lex.Token;

import java.util.Optional;
import java.util.function.Function;

/**
 * Парсер математических выражений
 */
public class MathParser {
    //region fullyParsed : boolean = true - Проверять что входная последовательность токенов разобрана полностью
    private boolean fullyParsed = true;

    /**
     * Проверять что входная последовательность токенов разобрана полностью
     * @return true - есть проверка
     */
    public boolean isFullyParsed() {
        return fullyParsed;
    }

    /**
     * Проверять что входная последовательность токенов разобрана полностью
     * @param fullyParsed true - есть проверка
     */
    public void setFullyParsed(boolean fullyParsed) {
        this.fullyParsed = fullyParsed;
    }
    //endregion

    private boolean unaryHighPriority = false;

    public boolean isUnaryHighPriority() {
        return unaryHighPriority;
    }

    public void setUnaryHighPriority(boolean unaryHighPriority) {
        this.unaryHighPriority = unaryHighPriority;
        if( unaryHighPriority ){
            mulNestedParser = ptr -> parseUnary(ptr, this::parseAtom);
            parseNestedParser = this::parseSum;
        }else{
            mulNestedParser = this::parseAtom;
            parseNestedParser = ptr -> parseUnary(ptr, this::parseSum);
        }
    }

    private Function<Pointer.ListPointer<Token>,Optional<Ast>> mulNestedParser = this::parseAtom;
    private Function<Pointer.ListPointer<Token>,Optional<Ast>> parseNestedParser = ptr -> parseUnary(ptr, this::parseSum);


    /**
     * Уровень вложенности вызова {@link #parse(Pointer.ListPointer)}
      */
    private int parseLevel = 0;

    /**
     * Парсинг выражения
     * @param ptr указатель на лексемы
     * @return выражение
     */
    public Optional<Ast> parse(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");
        Optional<Ast> result = null;
        try {
            parseLevel++;
            result = parseUnary(ptr, this::parseSum);
        } finally {
            parseLevel--;
        }
        if( parseLevel==0 && result.isPresent() && fullyParsed && !result.get().end().isEOF() ){
            return Optional.empty();
        }
        return result;
    }

    private Optional<Ast> parseSum(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");

        Optional<Ast> expOpt = parseMul(ptr, mulNestedParser);
        if (expOpt.isEmpty()) return Optional.empty();

        while (true) {
            var exp = expOpt.get();
            var opPlus = exp.end().get().flatMap(t -> t instanceof PlusToken ? Optional.of(t) : Optional.empty());
            var opMinus = exp.end().get().flatMap(t -> t instanceof MinusToken ? Optional.of(t) : Optional.empty());

            if (!(opPlus.isPresent() || opMinus.isPresent())) break;

            var rightOpt = parseMul(exp.end().move(1), mulNestedParser);
            if (rightOpt.isEmpty()) break;
            var right = rightOpt.get();

            if (opPlus.isPresent()) {
                expOpt = Optional.of(new AddAst(exp.begin(), right.end(), exp, right));
            }else{
                expOpt = Optional.of(new SubAst(exp.begin(), right.end(), exp, right));
            }
        }

        return expOpt;
    }

    private Optional<Ast> parseMul(Pointer.ListPointer<Token> ptr, Function<Pointer.ListPointer<Token>,Optional<Ast>> nestedParser) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");

        Optional<Ast> expOpt = nestedParser.apply(ptr);
        if (expOpt.isEmpty()) return Optional.empty();

        while (true) {
            var exp = expOpt.get();
            var opMul = exp.end().get().flatMap(t -> t instanceof MultiplyToken ? Optional.of(t) : Optional.empty());
            var opDiv = exp.end().get().flatMap(t -> t instanceof DivisionToken ? Optional.of(t) : Optional.empty());

            if (!(opMul.isPresent() || opDiv.isPresent())) break;

            var rightOpt = nestedParser.apply(exp.end().move(1));
            if (rightOpt.isEmpty()) break;
            var right = rightOpt.get();

            if (opMul.isPresent()) {
                expOpt = Optional.of(new MulAst(exp.begin(), right.end(), exp, right));
            }else {
                expOpt = Optional.of(new DivAst(exp.begin(), right.end(), exp, right));
            }
        }

        return expOpt;
    }

    private Optional<Ast> parseUnary(Pointer.ListPointer<Token> ptr, Function<Pointer.ListPointer<Token>,Optional<Ast>> nestedParser) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");

        var opPlus = ptr.get().flatMap(t -> t instanceof PlusToken ? Optional.of(t) : Optional.empty());
        var opMinus = ptr.get().flatMap(t -> t instanceof MinusToken ? Optional.of(t) : Optional.empty());

        var aPtr =
            opPlus.isPresent() || opMinus.isPresent()
                ? ptr.move(1)
                : ptr;

        var atom = nestedParser.apply(aPtr);

        if (atom.isPresent()) {
            if (opPlus.isPresent()) {
                return Optional.of(new UnaryPlusAst(ptr, atom.get().end(), atom.get()));
            } else if (opMinus.isPresent()) {
                return Optional.of(new UnaryMinusAst(ptr, atom.get().end(), atom.get()));
            }
        }

        return atom;
    }

    private Optional<Ast> parseAtom(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");
        var tokOpt = ptr.get();

        // Случай number
        Optional<Ast> numAst =
            tokOpt.flatMap(t -> t instanceof NumToken nt
                ? Optional.of(new NumAst(ptr, ptr.move(1), nt))
                : Optional.empty()
            );
        if (numAst.isPresent()) return numAst;

        // случай скобок
        var opeParentheses = tokOpt.flatMap(t -> t instanceof OpenParenthesesToken
            ? Optional.of(t)
            : Optional.empty());

        if (opeParentheses.isPresent()) {
            var innerExpressionOpt = parse(ptr.move(1));

            if (innerExpressionOpt.isPresent()) {
                var innExp = innerExpressionOpt.get();
                var closeParent = innExp.end().get().flatMap(
                    t -> t instanceof CloseParenthesesToken ? Optional.of(t) : Optional.empty());
                if (closeParent.isPresent()) {
                    return Optional.of(
                        new ParenthesesAst(
                            ptr, innExp.end().move(1),
                            innExp
                        )
                    );
                }
            }
        }

        return Optional.empty();
    }
}
