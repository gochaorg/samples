package org.sample.sql_serialize;

import java.io.IOException;
import java.util.function.Supplier;

public class PrefixAppendable implements Appendable {
    public final Supplier<String> prefix;
    public final Appendable out;
    public PrefixAppendable(Supplier<String> prefix, Appendable out){
        if( prefix==null )throw new IllegalArgumentException( "prefix==null" );
        if( out==null )throw new IllegalArgumentException( "out==null" );
        this.prefix = prefix;
        this.out = out;
    }

    @Override
    public synchronized Appendable append(CharSequence csq) throws IOException {
        if( csq==null )throw new IllegalArgumentException( "csq==null" );
        append(csq,0,csq.length());
        return this;
    }

    private boolean firstCall = true;
    private boolean expect_n = false;
    private String expt_keep;

    @Override
    public synchronized Appendable append(CharSequence csq, int start, int end) throws IOException {
        if( csq==null )throw new IllegalArgumentException( "csq==null" );
        if( start<0 )throw new IllegalArgumentException( "start<0" );
        if( end<0 )throw new IllegalArgumentException( "end<0" );
        if( start>end )throw new IllegalArgumentException( "start>end" );
        if( (end-start)<1 )return this;
        if( firstCall ){
            String str = prefix.get();
            if( str!=null )out.append(str);
            firstCall = false;
        }

        StringBuilder sb = new StringBuilder();
        int skip = 0;
        for( int i=start; i<end; i++ ){
            if( skip>0 ){
                skip--;
                continue;
            }
            char c0 = csq.charAt(i);
            char c1 = i<(end-1) ? csq.charAt(i+1) : (char)0;
            if( c0=='\r' && c1=='\n' ){
                if( expect_n ){
                    if( expt_keep!=null ){
                        sb.append(expt_keep);
                        expt_keep = null;
                    }

                    String str = prefix.get();
                    if( str!=null )sb.append(str);
                    expect_n = false;
                }
                skip=1;
                sb.append("\r\n");

                String str = prefix.get();
                if( str!=null )sb.append(str);
            }else if( c0=='\n' || c0=='\r' ){
                if( expect_n && c0!='\n' ){
                    if( expt_keep!=null ){
                        sb.append(expt_keep);
                        expt_keep = null;
                    }

                    String str = prefix.get();
                    if( str!=null )sb.append(str);
                    expect_n = false;
                }
                sb.append(c0);

                String str = prefix.get();
                if( str!=null )sb.append(str);
            }else {
                sb.append(c0);
            }
        }
        out.append(sb.toString());

        return this;
    }

    @Override
    public synchronized Appendable append(char c) throws IOException {
        if( firstCall ){
            String str = prefix.get();
            if( str!=null )out.append(str);
            firstCall = false;
        }

        if( c=='\r' ){
            expt_keep = "\r";
            expect_n = true;
            return this;
        }

        if( c=='\n' ){
            String str = prefix.get();
            if( str!=null )out.append(str);
        }
        out.append(c);

        return this;
    }
}
