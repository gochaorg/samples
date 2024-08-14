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

public class MathParser {
    public static Optional<Ast> parse(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");
        return parseSum(ptr);
    }

    public static Optional<Ast> parseSum(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");

        Optional<Ast> expOpt = parseMul(ptr);
        if (expOpt.isEmpty()) return Optional.empty();

        while (true) {
            var exp = expOpt.get();
            var opPlus = exp.end().get().flatMap(t -> t instanceof PlusToken ? Optional.of(t) : Optional.empty());
            var opMinus = exp.end().get().flatMap(t -> t instanceof MinusToken ? Optional.of(t) : Optional.empty());

            if (!(opPlus.isPresent() || opMinus.isPresent())) break;

            var rightOpt = parseMul(exp.end().move(1));
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

    public static Optional<Ast> parseMul(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");

        Optional<Ast> expOpt = parseUnary(ptr);
        if (expOpt.isEmpty()) return Optional.empty();

        while (true) {
            var exp = expOpt.get();
            var opMul = exp.end().get().flatMap(t -> t instanceof MultiplyToken ? Optional.of(t) : Optional.empty());
            var opDiv = exp.end().get().flatMap(t -> t instanceof DivisionToken ? Optional.of(t) : Optional.empty());

            if (!(opMul.isPresent() || opDiv.isPresent())) break;

            var rightOpt = parseUnary(exp.end().move(1));
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

    public static Optional<Ast> parseUnary(Pointer.ListPointer<Token> ptr) {
        if (ptr == null) throw new IllegalArgumentException("ptr==null");

        var opPlus = ptr.get().flatMap(t -> t instanceof PlusToken ? Optional.of(t) : Optional.empty());
        var opMinus = ptr.get().flatMap(t -> t instanceof MinusToken ? Optional.of(t) : Optional.empty());

        var aPtr =
            opPlus.isPresent() || opMinus.isPresent()
                ? ptr.move(1)
                : ptr;

        var atom = parseAtom(aPtr);

        if (atom.isPresent()) {
            if (opPlus.isPresent()) {
                return Optional.of(new UnaryPlusAst(ptr, atom.get().end(), atom.get()));
            } else if (opMinus.isPresent()) {
                return Optional.of(new UnaryMinusAst(ptr, atom.get().end(), atom.get()));
            }
        }

        return atom;
    }

    public static Optional<Ast> parseAtom(Pointer.ListPointer<Token> ptr) {
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
