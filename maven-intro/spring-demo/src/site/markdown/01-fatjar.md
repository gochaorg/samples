fatjar
===============

pom.xml
---------

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>spring-fat-jar</id>
                    <phase>package</phase>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

log
--------

```
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ spring-demo ---
[INFO] Building jar: /home/user/code/samples/maven-intro/spring-demo/target/spring-demo-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.4.1:repackage (repackage) @ spring-demo ---
[INFO] Replacing main artifact with repackaged archive
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.4.1:repackage (spring-fat-jar) @ spring-demo ---
[INFO] Replacing main artifact with repackaged archive
```

ls -h
----------

```
-rw-rw-r--  1 user user  19M дек 29 16:17 spring-demo-0.0.1-SNAPSHOT.jar
-rw-rw-r--  1 user user  14K дек 29 16:17 spring-demo-0.0.1-SNAPSHOT.jar.original
```

unzip
----------

```
> unzip -l target/spring-demo-0.0.1-SNAPSHOT.jar 
Archive:  target/spring-demo-0.0.1-SNAPSHOT.jar
  Length      Date    Time    Name
---------  ---------- -----   ----
        0  2020-12-29 16:17   META-INF/
      472  2020-12-29 16:17   META-INF/MANIFEST.MF
        0  1980-02-01 00:00   org/
        0  1980-02-01 00:00   org/springframework/
        0  1980-02-01 00:00   org/springframework/boot/
        0  1980-02-01 00:00   org/springframework/boot/loader/
     5871  1980-02-01 00:00   org/springframework/boot/loader/ClassPathIndexFile.class
     6806  1980-02-01 00:00   org/springframework/boot/loader/ExecutableArchiveLauncher.class
     3966  1980-02-01 00:00   org/springframework/boot/loader/JarLauncher.class
     1483  1980-02-01 00:00   org/springframework/boot/loader/LaunchedURLClassLoader$DefinePackageCallType.class
     1535  1980-02-01 00:00   org/springframework/boot/loader/LaunchedURLClassLoader$UseFastConnectionExceptionsEnumeration.class
    11154  1980-02-01 00:00   org/springframework/boot/loader/LaunchedURLClassLoader.class
     5932  1980-02-01 00:00   org/springframework/boot/loader/Launcher.class
     1536  1980-02-01 00:00   org/springframework/boot/loader/MainMethodRunner.class
      266  1980-02-01 00:00   org/springframework/boot/loader/PropertiesLauncher$1.class
     1484  1980-02-01 00:00   org/springframework/boot/loader/PropertiesLauncher$ArchiveEntryFilter.class
     8128  1980-02-01 00:00   org/springframework/boot/loader/PropertiesLauncher$ClassPathArchives.class
     1953  1980-02-01 00:00   org/springframework/boot/loader/PropertiesLauncher$PrefixMatchingArchiveFilter.class
    18267  1980-02-01 00:00   org/springframework/boot/loader/PropertiesLauncher.class
     1750  1980-02-01 00:00   org/springframework/boot/loader/WarLauncher.class
        0  1980-02-01 00:00   org/springframework/boot/loader/archive/
      302  1980-02-01 00:00   org/springframework/boot/loader/archive/Archive$Entry.class
      511  1980-02-01 00:00   org/springframework/boot/loader/archive/Archive$EntryFilter.class
     4745  1980-02-01 00:00   org/springframework/boot/loader/archive/Archive.class
     6093  1980-02-01 00:00   org/springframework/boot/loader/archive/ExplodedArchive$AbstractIterator.class
     2180  1980-02-01 00:00   org/springframework/boot/loader/archive/ExplodedArchive$ArchiveIterator.class
     1857  1980-02-01 00:00   org/springframework/boot/loader/archive/ExplodedArchive$EntryIterator.class
     1269  1980-02-01 00:00   org/springframework/boot/loader/archive/ExplodedArchive$FileEntry.class
     2527  1980-02-01 00:00   org/springframework/boot/loader/archive/ExplodedArchive$SimpleJarFileArchive.class
     5346  1980-02-01 00:00   org/springframework/boot/loader/archive/ExplodedArchive.class
     2884  1980-02-01 00:00   org/springframework/boot/loader/archive/JarFileArchive$AbstractIterator.class
     1981  1980-02-01 00:00   org/springframework/boot/loader/archive/JarFileArchive$EntryIterator.class
     1081  1980-02-01 00:00   org/springframework/boot/loader/archive/JarFileArchive$JarFileEntry.class
     2528  1980-02-01 00:00   org/springframework/boot/loader/archive/JarFileArchive$NestedArchiveIterator.class
     7569  1980-02-01 00:00   org/springframework/boot/loader/archive/JarFileArchive.class
        0  1980-02-01 00:00   org/springframework/boot/loader/data/
      485  1980-02-01 00:00   org/springframework/boot/loader/data/RandomAccessData.class
      282  1980-02-01 00:00   org/springframework/boot/loader/data/RandomAccessDataFile$1.class
     2680  1980-02-01 00:00   org/springframework/boot/loader/data/RandomAccessDataFile$DataInputStream.class
     3259  1980-02-01 00:00   org/springframework/boot/loader/data/RandomAccessDataFile$FileAccess.class
     4015  1980-02-01 00:00   org/springframework/boot/loader/data/RandomAccessDataFile.class
        0  1980-02-01 00:00   org/springframework/boot/loader/jar/
     1438  1980-02-01 00:00   org/springframework/boot/loader/jar/AbstractJarFile$JarFileType.class
      878  1980-02-01 00:00   org/springframework/boot/loader/jar/AbstractJarFile.class
     4976  1980-02-01 00:00   org/springframework/boot/loader/jar/AsciiBytes.class
      616  1980-02-01 00:00   org/springframework/boot/loader/jar/Bytes.class
      295  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryEndRecord$1.class
     3401  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryEndRecord$Zip64End.class
     2004  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryEndRecord$Zip64Locator.class
     4682  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryEndRecord.class
     6223  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryFileHeader.class
     4620  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryParser.class
      540  1980-02-01 00:00   org/springframework/boot/loader/jar/CentralDirectoryVisitor.class
      345  1980-02-01 00:00   org/springframework/boot/loader/jar/FileHeader.class
    11634  1980-02-01 00:00   org/springframework/boot/loader/jar/Handler.class
     3885  1980-02-01 00:00   org/springframework/boot/loader/jar/JarEntry.class
     1458  1980-02-01 00:00   org/springframework/boot/loader/jar/JarEntryCertification.class
      299  1980-02-01 00:00   org/springframework/boot/loader/jar/JarEntryFilter.class
     2296  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFile$1.class
     1299  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFile$JarEntryEnumeration.class
    16096  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFile.class
     1368  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFileEntries$1.class
     2258  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFileEntries$EntryIterator.class
    16395  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFileEntries.class
     3390  1980-02-01 00:00   org/springframework/boot/loader/jar/JarFileWrapper.class
      702  1980-02-01 00:00   org/springframework/boot/loader/jar/JarURLConnection$1.class
     4302  1980-02-01 00:00   org/springframework/boot/loader/jar/JarURLConnection$JarEntryName.class
     9440  1980-02-01 00:00   org/springframework/boot/loader/jar/JarURLConnection.class
     3559  1980-02-01 00:00   org/springframework/boot/loader/jar/StringSequence.class
     1813  1980-02-01 00:00   org/springframework/boot/loader/jar/ZipInflaterInputStream.class
        0  1980-02-01 00:00   org/springframework/boot/loader/jarmode/
      293  1980-02-01 00:00   org/springframework/boot/loader/jarmode/JarMode.class
     2201  1980-02-01 00:00   org/springframework/boot/loader/jarmode/JarModeLauncher.class
     1292  1980-02-01 00:00   org/springframework/boot/loader/jarmode/TestJarMode.class
        0  1980-02-01 00:00   org/springframework/boot/loader/util/
     5174  1980-02-01 00:00   org/springframework/boot/loader/util/SystemPropertyUtils.class
        0  2020-12-29 16:17   BOOT-INF/
        0  2020-12-29 16:17   BOOT-INF/classes/
        0  2020-12-29 15:28   BOOT-INF/classes/static/
        0  2020-12-29 15:28   BOOT-INF/classes/org/
        0  2020-12-29 15:28   BOOT-INF/classes/org/example/
        0  2020-12-29 15:55   BOOT-INF/classes/org/example/springdemo/
        0  2020-12-29 16:17   META-INF/maven/
        0  2020-12-29 16:17   META-INF/maven/org.example/
        0  2020-12-29 16:17   META-INF/maven/org.example/spring-demo/
      314  2020-12-29 15:28   BOOT-INF/classes/application.yml
     1364  2020-12-29 15:28   BOOT-INF/classes/static/index.html
     7867  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/LinuxProcessesRest.class
     1059  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/LoginsConf.class
     1978  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/LinuxProcessesRest$1.class
     6988  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/BasicAuth.class
     2176  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/AuthenticationEntryPointImpl.class
      763  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/SpringDemoApplication.class
     1369  2020-12-29 16:17   BOOT-INF/classes/org/example/springdemo/Account.class
     2437  2020-12-29 16:17   META-INF/maven/org.example/spring-demo/pom.xml
       66  2020-12-29 16:17   META-INF/maven/org.example/spring-demo/pom.properties
        0  2020-12-29 16:17   BOOT-INF/lib/
  1302347  2020-12-11 07:01   BOOT-INF/lib/spring-boot-2.4.1.jar
  1537752  2020-12-11 07:00   BOOT-INF/lib/spring-boot-autoconfigure-2.4.1.jar
   290339  2017-03-31 20:20   BOOT-INF/lib/logback-classic-1.2.3.jar
   471901  2017-03-31 20:19   BOOT-INF/lib/logback-core-1.2.3.jar
    17461  2020-05-10 12:10   BOOT-INF/lib/log4j-to-slf4j-2.13.3.jar
   292301  2020-05-10 12:07   BOOT-INF/lib/log4j-api-2.13.3.jar
     4592  2019-12-16 22:00   BOOT-INF/lib/jul-to-slf4j-1.7.30.jar
    25058  2019-08-02 11:08   BOOT-INF/lib/jakarta.annotation-api-1.3.5.jar
   310104  2020-09-11 14:52   BOOT-INF/lib/snakeyaml-1.27.jar
  1421699  2020-10-01 22:38   BOOT-INF/lib/jackson-databind-2.11.3.jar
    68215  2020-10-01 22:20   BOOT-INF/lib/jackson-annotations-2.11.3.jar
   351495  2020-10-01 22:25   BOOT-INF/lib/jackson-core-2.11.3.jar
    34335  2020-10-02 00:25   BOOT-INF/lib/jackson-datatype-jdk8-2.11.3.jar
   111008  2020-10-02 00:25   BOOT-INF/lib/jackson-datatype-jsr310-2.11.3.jar
     9267  2020-10-02 00:25   BOOT-INF/lib/jackson-module-parameter-names-2.11.3.jar
  3409747  2020-12-03 11:38   BOOT-INF/lib/tomcat-embed-core-9.0.41.jar
   237826  2019-08-26 10:58   BOOT-INF/lib/jakarta.el-3.0.3.jar
   271820  2020-12-03 11:38   BOOT-INF/lib/tomcat-embed-websocket-9.0.41.jar
  1549576  2020-12-09 06:05   BOOT-INF/lib/spring-web-5.3.2.jar
   695691  2020-12-09 06:05   BOOT-INF/lib/spring-beans-5.3.2.jar
   996134  2020-12-09 06:06   BOOT-INF/lib/spring-webmvc-5.3.2.jar
  1241309  2020-12-09 06:05   BOOT-INF/lib/spring-context-5.3.2.jar
   282517  2020-12-09 06:05   BOOT-INF/lib/spring-expression-5.3.2.jar
   374283  2020-12-09 06:05   BOOT-INF/lib/spring-aop-5.3.2.jar
  1227511  2020-12-03 04:44   BOOT-INF/lib/spring-security-config-5.4.2.jar
   449471  2020-12-03 04:43   BOOT-INF/lib/spring-security-core-5.4.2.jar
   605271  2020-12-03 04:44   BOOT-INF/lib/spring-security-web-5.4.2.jar
    41472  2019-12-16 22:03   BOOT-INF/lib/slf4j-api-1.7.30.jar
  1465868  1980-02-01 00:00   BOOT-INF/lib/spring-core-5.3.2.jar
    23943  2020-12-09 06:04   BOOT-INF/lib/spring-jcl-5.3.2.jar
    28860  2020-11-02 16:11   BOOT-INF/lib/fs-1.1.jar
    57865  2020-11-02 16:11   BOOT-INF/lib/cbuffer-1.1-SNAPSHOT.jar
     9016  2020-11-02 16:11   BOOT-INF/lib/iofun-1.0.jar
   393127  2020-08-22 18:50   BOOT-INF/lib/ecolls-1.7.jar
    32913  1980-02-01 00:00   BOOT-INF/lib/spring-boot-jarmode-layertools-2.4.1.jar
     1053  2020-12-29 16:17   BOOT-INF/classpath.idx
     1770  2020-12-29 16:17   BOOT-INF/layers.idx
---------                     -------
 19912667                     134 files
```

run
----------------

```
> java -jar spring-demo-0.0.1-SNAPSHOT.jar 

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.1)

2020-12-29 16:21:10.544  INFO 15912 --- [           main] o.e.springdemo.SpringDemoApplication     : Starting SpringDemoApplication v0.0.1-SNAPSHOT using Java 11.0.7 on user-Modern-14-A10RB with PID 15912 (/home/user/code/samples/maven-intro/spring-demo/target/spring-demo-0.0.1-SNAPSHOT.jar started by user in /home/user/code/samples/maven-intro/spring-demo/target)
2020-12-29 16:21:10.546  INFO 15912 --- [           main] o.e.springdemo.SpringDemoApplication     : No active profile set, falling back to default profiles: default
2020-12-29 16:21:11.535  INFO 15912 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8088 (http)
2020-12-29 16:21:11.544  INFO 15912 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-12-29 16:21:11.544  INFO 15912 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.41]
2020-12-29 16:21:11.591  INFO 15912 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-12-29 16:21:11.591  INFO 15912 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 977 ms
Encoded password of 123=$2a$10$7jrLmxcTMYAALJQ4YXpWs.E9NhcwduhBGXEHgrVvogvzGKH7X6uKC
2020-12-29 16:21:12.113  INFO 15912 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@54a67a45, org.springframework.security.web.context.SecurityContextPersistenceFilter@681a8b4e, org.springframework.security.web.header.HeaderWriterFilter@61c9c3fd, org.springframework.security.web.authentication.logout.LogoutFilter@4c2cc639, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@e84a8e1, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@5c08c46a, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@4218500f, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@7d42c224, org.springframework.security.web.session.SessionManagementFilter@8c11eee, org.springframework.security.web.access.ExceptionTranslationFilter@1253e7cb, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@3bd418e4]
2020-12-29 16:21:12.203  INFO 15912 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-12-29 16:21:12.285  INFO 15912 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2020-12-29 16:21:12.384  INFO 15912 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8088 (http) with context path ''
2020-12-29 16:21:12.396  INFO 15912 --- [           main] o.e.springdemo.SpringDemoApplication     : Started SpringDemoApplication in 2.334 seconds (JVM running for 2.767)
```

