package org.example.ha.tezt

import groovy.transform.TypeChecked
import xyz.cofe.text.EndLine
import xyz.cofe.text.EndLineReWriter

import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer
import java.util.function.Function

class LineInterceptor extends EndLineReWriter {
    LineInterceptor(Writer writer) {
        super(writer)
    }

    LineInterceptor(Writer writer, String endl) {
        super(writer, endl)
    }

    LineInterceptor(Writer writer, EndLine endl) {
        super(writer, endl)
    }

    {
        super.@lineConvertor = lineListenerConvertor;
    }

    protected final List<Function<String,String>> interceptors = new CopyOnWriteArrayList<>()

    public LineInterceptor listen( Consumer<String> ls ){
        if( ls==null )throw new IllegalArgumentException("ls==null");
        Function<String,String> f = { line ->
            ls.accept(line)
            line
        } as Function<String,String>
        interceptors.add( f )
        return this
    }

    public LineInterceptor intercept( Function<String,String> ls ){
        if( ls==null )throw new IllegalArgumentException("ls==null");
        interceptors.add(ls)
        return this
    }

    @TypeChecked
    protected String fire( String line ){
        String[] line1 = new String[]{ line }
        interceptors.each {fn ->
            line1[0] = fn.apply(line1[0])
        }
        return line1[0];
    }

    protected final Function<String, String> lineListenerConvertor = (String line) -> {
        String str = line;
        Function<String, String> orgnConvertor = originalConvertor
        if( orgnConvertor!=null ){
            str = orgnConvertor.apply(line)
        }
        return fire(str);
    }

    protected volatile Function<String, String> originalConvertor;

    @Override
    synchronized void setLineConvertor(Function<String, String> newLineConv) {
        originalConvertor = newLineConv;
        super.setLineConvertor(newLineConv)
    }

    @Override
    synchronized Function<String, String> getLineConvertor() {
        return originalConvertor;
    }
}
