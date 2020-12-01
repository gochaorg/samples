package xyz.cofe.jacob

import com.jacob.com.LibraryLoader
import xyz.cofe.io.fn.IOFun

class JacobDll {
    private static volatile boolean inited = false;
    private static final String x64dllName = 'jacob-1.20-x64.dll'
    private static final String x86dllName = 'jacob-1.20-x86.dll'

    synchronized static void init(){
        if( inited )return;
        String tmpdirPath = System.getProperty("java.io.tmpdir");
        if( tmpdirPath==null ){
            throw new Error("can't read sys property: java.io.tmpdir")
        }

        File tmpdir = new File(tmpdirPath)
        if( !tmpdir.isDirectory() ){
            throw new Error("java.io.tmpdir(=$tmpdir) is not directory")
        }

        File x64dll = new File(tmpdir,x64dllName)
        if( !x64dll.exists() ){
            String res = '/jacob-dll/'+x64dllName
            URL x64dllUrl = JacobDll.getResource(res)
            if( x64dllUrl==null ){
                throw new Error("resource $res not found")
            }
            x64dllUrl.withInputStream {inStrm ->
                x64dll.withOutputStream {outStrm ->
                    IOFun.copy(inStrm,outStrm)
                }
            }
            println("copied $res to $x64dll")
        }

        File x86dll = new File(tmpdir,x86dllName)
        if( !x86dll.exists() ){
            String res = '/jacob-dll/'+x86dllName
            URL x86dllUrl = JacobDll.getResource(res)
            if( x86dllUrl==null ){
                throw new Error("resource $res not found")
            }
            x86dllUrl.withInputStream {inStrm ->
                x86dll.withOutputStream {outStrm ->
                    IOFun.copy(inStrm,outStrm)
                }
            }
            println("copied $res to $x86dll")
        }

        String arch = System.getProperty("os.arch")
        if( arch==null ){
            throw new Error("can't read sys property: os.arch")
        }

        boolean isX64 = arch.contains("64")
        if( isX64 ){
            System.setProperty(LibraryLoader.JACOB_DLL_PATH,
                    x64dll.toString())
        }else{
            System.setProperty(LibraryLoader.JACOB_DLL_PATH,
                    x86dll.toString())
        }

        inited = true
    }
}