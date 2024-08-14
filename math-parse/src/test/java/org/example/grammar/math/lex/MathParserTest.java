package org.example.grammar.math.lex;

import org.example.grammar.math.ast.MathParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathParserTest {
    @Test
    public void singleNum(){
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
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
        var astOpt = MathParser.parse(
            MathTokenParser.tokenPointerOf("1+(2+3)*4")
        );

        System.out.println(astOpt);
        assertTrue(astOpt.isPresent());

        var ast = astOpt.get();
        System.out.println(ast.eval());
        assertTrue(ast.eval().value().map(v -> v==21.0).orElse(false));
    }
}
