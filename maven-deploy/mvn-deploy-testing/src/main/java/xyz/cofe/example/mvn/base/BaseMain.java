package xyz.cofe.example.mvn.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Выводит на экран информацию о сборке
 */
public class BaseMain {
    // Здесь храниться инф о сборке
    private static Properties buildInfo;
    static {
        // Чтение инф о сборке из ресурсов
        buildInfo = new Properties();
        URL url = BaseMain.class.getResource("version.properties");
        if( url!=null ) {
            try {
                try(InputStream strm = url.openStream()){
                    buildInfo.load(strm);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Время сборки
     */
    public static final String buildTime;
    static { buildTime = buildInfo.getProperty("buildTime","?"); }

    /**
     * maven - группа
     */
    public static final String group;
    static { group = buildInfo.getProperty("group","?"); }

    /**
     * maven - артефакт
     */
    public static final String artifact;
    static { artifact = buildInfo.getProperty("artifact","?"); }

    /**
     * maven - версия
     */
    public static final String version;
    static { version = buildInfo.getProperty("version","?"); }

    public static void main(String[] args){
        System.out.println("hello," +
                " group="+group+
                " artifact="+artifact+
                " version="+version+
                " buildTime="+buildTime);
    }
}
