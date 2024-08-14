package org.example.grammar.math.lex;

import org.example.grammar.math.ast.MathParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathParserTest {
    @Test
    public void singleNum(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("1")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==1.0).orElse(false));
    }

    @Test
    public void sumNum(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("1 + 2")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==3.0).orElse(false));
    }

    @Test
    public void minusNum(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("1 - 2")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==-1.0).orElse(false));
    }

    @Test
    public void divNum(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("2 / 2")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==1.0).orElse(false));
    }

    @Test
    public void mulNum(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("2 * 2")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==4.0).orElse(false));
    }

    @Test
    public void opPrio(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("2 + 2 * 2")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==6.0).orElse(false));
    }

    @Test
    public void assocOrder(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("1 + 2 + 3 + 4 + 5")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
//        assertTrue(ast.eval().value().map(v -> v==6.0).orElse(false));
    }


    @Test
    public void parethneses(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("1+(2+3)*4")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==21.0).orElse(false));
    }

    @Test
    public void unary1(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("-1")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==-1.0).orElse(false));
    }

    @Test
    public void unary2(){
        var astOpt = new MathParser().parse(
            MathTokenParser.tokenPointerOf("1--1")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isEmpty());
    }
}
