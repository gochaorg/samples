package org.example.grfn;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOError;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class HtmlBuilder {
    public final HttpServletResponse response;
    protected HtmlBuilder( HtmlBuilder parent ){
        if( parent==null )throw new IllegalArgumentException( "parent==null" );
        this.response = parent.response;
    }
    public HtmlBuilder( HttpServletResponse response ){
        if( response==null )throw new IllegalArgumentException( "response==null" );
        this.response = response;
    }

    public HtmlBuilder asis(String txt){
        try{
            response.getWriter().print(txt!=null ? txt : "null");
        } catch( IOException e ){
            throw new IOError(e);
        }
        return this;
    }
    public HtmlBuilder text(String txt){
        if( txt!=null ){
            asis(HtmlUtils.htmlEscape(txt));
        }else{
            asis(null);
        }
        return this;
    }
    public void flush(){
        try{
            response.getWriter().flush();
        } catch( IOException e ){
            throw new IOError(e);
        }
    }

    private static String urlValueEncode(String value) {
        try{
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch( UnsupportedEncodingException e ){
            throw new RuntimeException(e);
        }
    }

    public HtmlBuilder br(){
        asis("<br/>");
        return this;
    }
    public HtmlBuilder a(String href, String text){
        asis("<a");
        if( href!=null ){
            asis(" href=\"");
            asis(href);
            asis("\">");
        }else{
            asis(">");
        }
        text(text);
        asis("</a>");
        return this;
    }
    public HtmlBuilder a( String href, Consumer<HtmlBuilder> out ){
        asis("<a");
        if( href!=null ){
            asis(" href=\"");
            asis(href);
            asis("\">");
        }else{
            asis(">");
        }
        if( out!=null ){
            out.accept(this);
        }
        asis("</a>");
        return this;
    }

    public static class FormBody extends HtmlBuilder {
        public FormBody( Form form ){
            super( form.htmlBuilder() );
        }

        public FormBody inputText(String name){
            if( name==null )throw new IllegalArgumentException( "name==null" );
            String nm = HtmlBuilder.urlValueEncode(name);
            asis("<input name=\""+nm+"\" type=\"text\" placeholder=\""+nm+"\">");
            return this;
        }

        public FormBody submit(){
            asis("<input type='submit'>");
            return this;
        }
        public FormBody submit(String value){
            if( value==null )throw new IllegalArgumentException( "value==null" );
            asis("<input type='submit' value=\""+HtmlBuilder.urlValueEncode(value)+"\">");
            return this;
        }
    }

    public class Form {
        protected HtmlBuilder htmlBuilder(){ return HtmlBuilder.this; }

        public final String action;
        public Form( String action ){
            this.action = action;
        }

        private String method="get";
        public Form get(){
            method = "get";
            return this;
        }
        public Form post(){
            method = "post";
            return this;
        }

        private String enc="application/x-www-form-urlencoded";
        public Form urlEncoded(){
            enc = "application/x-www-form-urlencoded";
            return this;
        }
        public Form multipartFormData(){
            enc = "multipart/form-data";
            return this;
        }

        private String target;
        public Form blank(){
            target = "_blank";
            return this;
        }

        public HtmlBuilder body( Consumer<FormBody> bodyBuilder ){
            if( bodyBuilder==null )throw new IllegalArgumentException( "bodyBuilder==null" );

            asis("<form action=");
            asis(action);
            asis(" method=\"").asis(method).asis("\" enctype=\"").asis(enc).asis("\"");
            if( target!=null )asis(" target=\""+target+"\"");
            asis(">\n");

            bodyBuilder.accept(new FormBody(this));

            asis("</form>\n");
            return HtmlBuilder.this;
        }
    }

    public Form form( String action ){
        if( action==null )throw new IllegalArgumentException( "action==null" );
        return new Form(action);
    }
}
