appasembler
=====================

pom.xml
-----------

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>appassembler-maven-plugin</artifactId>
    <version>1.10</version>
    <executions>
        <execution>
            <phase>package</phase>
            <id>app-binary</id>
            <goals>
                <goal>assemble</goal>
            </goals>
            <configuration>
                <programs>
                    <program>
                        <mainClass>org.example.springdemo.SpringDemoApplication</mainClass>
                        <id>springdemo</id>
                    </program>
                </programs>
                <repositoryLayout>flat</repositoryLayout>
                <useWildcardClassPath>true</useWildcardClassPath>
                <repositoryName>jars</repositoryName>
            </configuration>
        </execution>
    </executions>
</plugin>
```

build log
----------------------

```
[INFO] --- appassembler-maven-plugin:1.10:assemble (app-binary) @ spring-demo ---
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-starter-web/2.4.1/spring-boot-starter-web-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-starter-web-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-starter-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-autoconfigure-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-starter-logging-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/logback-classic-1.2.3.jar
[INFO] Installing artifact /home/user/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/logback-core-1.2.3.jar
[INFO] Installing artifact /home/user/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/log4j-to-slf4j-2.13.3.jar
[INFO] Installing artifact /home/user/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/log4j-api-2.13.3.jar
[INFO] Installing artifact /home/user/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jul-to-slf4j-1.7.30.jar
[INFO] Installing artifact /home/user/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jakarta.annotation-api-1.3.5.jar
[INFO] Installing artifact /home/user/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/snakeyaml-1.27.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-starter-json/2.4.1/spring-boot-starter-json-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-starter-json-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.11.3/jackson-databind-2.11.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jackson-databind-2.11.3.jar
[INFO] Installing artifact /home/user/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.11.3/jackson-annotations-2.11.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jackson-annotations-2.11.3.jar
[INFO] Installing artifact /home/user/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.11.3/jackson-core-2.11.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jackson-core-2.11.3.jar
[INFO] Installing artifact /home/user/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.11.3/jackson-datatype-jdk8-2.11.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jackson-datatype-jdk8-2.11.3.jar
[INFO] Installing artifact /home/user/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.11.3/jackson-datatype-jsr310-2.11.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jackson-datatype-jsr310-2.11.3.jar
[INFO] Installing artifact /home/user/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.11.3/jackson-module-parameter-names-2.11.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jackson-module-parameter-names-2.11.3.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/2.4.1/spring-boot-starter-tomcat-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-starter-tomcat-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/9.0.41/tomcat-embed-core-9.0.41.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/tomcat-embed-core-9.0.41.jar
[INFO] Installing artifact /home/user/.m2/repository/org/glassfish/jakarta.el/3.0.3/jakarta.el-3.0.3.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/jakarta.el-3.0.3.jar
[INFO] Installing artifact /home/user/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/9.0.41/tomcat-embed-websocket-9.0.41.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/tomcat-embed-websocket-9.0.41.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-web/5.3.2/spring-web-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-web-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-beans-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-webmvc/5.3.2/spring-webmvc-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-webmvc-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-context-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-expression-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/boot/spring-boot-starter-security/2.4.1/spring-boot-starter-security-2.4.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-boot-starter-security-2.4.1.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-aop-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/security/spring-security-config/5.4.2/spring-security-config-5.4.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-security-config-5.4.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/security/spring-security-core/5.4.2/spring-security-core-5.4.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-security-core-5.4.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/security/spring-security-web/5.4.2/spring-security-web-5.4.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-security-web-5.4.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/slf4j-api-1.7.30.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-core-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-jcl-5.3.2.jar
[INFO] Installing artifact /home/user/.m2/repository/xyz/cofe/fs/1.1/fs-1.1.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/fs-1.1.jar
[INFO] Installing artifact /home/user/.m2/repository/xyz/cofe/cbuffer/1.1-SNAPSHOT/cbuffer-1.1-SNAPSHOT.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/cbuffer-1.1-SNAPSHOT.jar
[INFO] Installing artifact /home/user/.m2/repository/xyz/cofe/iofun/1.0/iofun-1.0.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/iofun-1.0.jar
[INFO] Installing artifact /home/user/.m2/repository/xyz/cofe/ecolls/1.7/ecolls-1.7.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/ecolls-1.7.jar
[INFO] Installing artifact /home/user/code/samples/maven-intro/spring-demo/target/spring-demo-0.0.1-SNAPSHOT.jar to /home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-demo-0.0.1-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.323 s
[INFO] Finished at: 2020-12-29T16:49:20+05:00
[INFO] ------------------------------------------------------------------------

Process finished with exit code 0
```

run
-----------------

```
user@user-Modern-14-A10RB:16:49:27:~/code/samples/maven-intro/spring-demo/target/appassembler:
> ll
итого 16
drwxrwxr-x  4 user user 4096 дек 29 16:49 ./
drwxrwxr-x 10 user user 4096 дек 29 16:49 ../
drwxrwxr-x  2 user user 4096 дек 29 16:49 bin/
drwxrwxr-x  2 user user 4096 дек 29 16:49 jars/
user@user-Modern-14-A10RB:16:49:28:~/code/samples/maven-intro/spring-demo/target/appassembler:
> ./bin/springdemo 

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.1)

2020-12-29 16:49:44.457  INFO 17826 --- [           main] o.e.springdemo.SpringDemoApplication     : Starting SpringDemoApplication v0.0.1-SNAPSHOT using Java 11.0.7 on user-Modern-14-A10RB with PID 17826 (/home/user/code/samples/maven-intro/spring-demo/target/appassembler/jars/spring-demo-0.0.1-SNAPSHOT.jar started by user in /home/user/code/samples/maven-intro/spring-demo/target/appassembler)
2020-12-29 16:49:44.459  INFO 17826 --- [           main] o.e.springdemo.SpringDemoApplication     : No active profile set, falling back to default profiles: default
2020-12-29 16:49:45.275  INFO 17826 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8088 (http)
2020-12-29 16:49:45.283  INFO 17826 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-12-29 16:49:45.283  INFO 17826 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.41]
2020-12-29 16:49:45.336  INFO 17826 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-12-29 16:49:45.336  INFO 17826 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 824 ms
Encoded password of 123=$2a$10$sKFcwNv/191ksFJNOxcK5er8JP09Xab02Q8AddOawdqpik0bYJXUa
2020-12-29 16:49:45.758  INFO 17826 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@623ebac7, org.springframework.security.web.context.SecurityContextPersistenceFilter@68b9834c, org.springframework.security.web.header.HeaderWriterFilter@333c8791, org.springframework.security.web.authentication.logout.LogoutFilter@127a7272, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@7f5538a1, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@671d1157, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@75d982d3, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@7c8c70d6, org.springframework.security.web.session.SessionManagementFilter@17fa1336, org.springframework.security.web.access.ExceptionTranslationFilter@2f4c2cd4, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@46a145ba]
2020-12-29 16:49:45.831  INFO 17826 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-12-29 16:49:45.894  INFO 17826 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2020-12-29 16:49:45.982  INFO 17826 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8088 (http) with context path ''
2020-12-29 16:49:45.992  INFO 17826 --- [           main] o.e.springdemo.SpringDemoApplication     : Started SpringDemoApplication in 1.897 seconds (JVM running for 2.144)
2020-12-29 16:49:54.979  INFO 17826 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutorus
```

ll bin
------------

```
> ll bin/
итого 16
drwxrwxr-x 2 user user 4096 дек 29 16:49 ./
drwxrwxr-x 4 user user 4096 дек 29 16:49 ../
-rwxr-xr-x 1 user user 3692 дек 29 16:49 springdemo*
-rw-rw-r-- 1 user user 3163 дек 29 16:49 springdemo.bat
```

bin/springdemo
--------------------

```shell
> cat bin/springdemo
#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2001-2006 The Apache Software Foundation.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# ----------------------------------------------------------------------------
#
#   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
#   reserved.


# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR/.." >/dev/null; pwd`

# Reset the REPO variable. If you need to influence this use the environment setup file.
REPO=


# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
		   if [ -z "$JAVA_HOME" ]; then
		      if [ -x "/usr/libexec/java_home" ]; then
			      JAVA_HOME=`/usr/libexec/java_home`
			  else
			      JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
			  fi
           fi       
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

if [ -z "$REPO" ]
then
  REPO="$BASEDIR"/jars
fi

CLASSPATH="$BASEDIR"/etc:"$REPO"/*

ENDORSED_DIR=
if [ -n "$ENDORSED_DIR" ] ; then
  CLASSPATH=$BASEDIR/$ENDORSED_DIR/*:$CLASSPATH
fi

if [ -n "$CLASSPATH_PREFIX" ] ; then
  CLASSPATH=$CLASSPATH_PREFIX:$CLASSPATH
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] && HOME=`cygpath --path --windows "$HOME"`
  [ -n "$BASEDIR" ] && BASEDIR=`cygpath --path --windows "$BASEDIR"`
  [ -n "$REPO" ] && REPO=`cygpath --path --windows "$REPO"`
fi

exec "$JAVACMD" $JAVA_OPTS  \
  -classpath "$CLASSPATH" \
  -Dapp.name="springdemo" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dapp.home="$BASEDIR" \
  -Dbasedir="$BASEDIR" \
  org.example.springdemo.SpringDemoApplication \
  "$@"
```

bin/springdemo.bat
------------------------

```
> cat bin/springdemo.bat 
@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\jars

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\*

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="springdemo" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" org.example.springdemo.SpringDemoApplication %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
```