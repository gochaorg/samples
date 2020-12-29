assembly
==================

pom.xml
------------

1. use [appassembler](02-appasembler.md)
2. use maven-site-plugin for docs
3. use maven-javadoc-plugin for api docs
4. use maven-source-plugin for zip of sources
5. use maven-assembly-plugin for generate distr


maven-site-plugin
-------------------

```
> tree
.
├── assembly
│   └── src.xml
├── main
│   ├── java
│   │   └── org
│   │       └── example
│   │           └── springdemo
│   └── resources
│       ├── application.yml
│       ├── static
│       │   └── index.html
│       └── templates
├── site
│   ├── markdown
│   │   ├── 01-fatjar.md
│   │   ├── 02-appasembler.md
│   │   ├── 03-assembly.md
│   │   ├── auth.md
│   │   └── rest.md
│   └── site.xml
└── test
```

### pom.xml

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-site-plugin</artifactId>
    <version>3.9.1</version>
    <dependencies>
        <!-- add optional Markdown processor -->
        <dependency>
            <groupId>org.apache.maven.doxia</groupId>
            <artifactId>doxia-module-markdown</artifactId>
            <version>1.9.1</version>
        </dependency>
    </dependencies>
    <configuration>
        <inputEncoding>UTF-8</inputEncoding>
        <outputEncoding>UTF-8</outputEncoding>
        <generateReports>false</generateReports>
    </configuration>
    <executions>
        <execution>
            <id>gen-docs</id>
            <phase>package</phase>
            <goals>
                <goal>site</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### target

* target/
    * site/
        * rest.html
        * auth.html
        * 01-fatjar.html
        * 02-appasembler.html
        * css/
        * fonts/
        * ...

maven-javadoc-plugin
---------------------

### pom.xml
    
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.2.0</version>
    <configuration>
        <javadocExecutable>${java.home}/bin/javadoc</javadocExecutable>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>javadoc</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
</plugin>
```

### target

* target/
    * site/
        * apidocs/

maven-source-plugin
--------------------

### pom.xml

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-source-plugin</artifactId>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>jar</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### target

* target/
    * spring-demo-0.0.1-SNAPSHOT-sources.jar

maven-assembly-plugin
-----------------------

### pom.xml

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.3.0</version>
    <configuration>
        <descriptors>
            <descriptor>src/assembly/src.xml</descriptor>
        </descriptors>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>single</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
</plugin>
```

### src/assembly/src.xml

```xml
<assembly>
    <id>dist</id>
    <formats>
        <format>dir</format>
    </formats>
    <fileSets>
        <fileSet>
            <directory>${basedir}/target/appassembler</directory>
            <includes>
                <include>**/*</include>
            </includes>
            <outputDirectory>/</outputDirectory>
        </fileSet>
        <fileSet>
            <directory>${basedir}/target/site</directory>
            <includes>
                <include>**/*</include>
            </includes>
            <outputDirectory>/doc</outputDirectory>
        </fileSet>
        <fileSet>
            <directory>${basedir}/target</directory>
            <includes>
                <include>*-sources.jar</include>
            </includes>
            <outputDirectory>/src</outputDirectory>
        </fileSet>
    </fileSets>
</assembly>
```

### target

* target/
    * spring-demo-0.0.1-SNAPSHOT-dist/
        * spring-demo-0.0.1-SNAPSHOT/
            * bin/
              * springdemo*
              * springdemo.bat
            * doc/
              * 01-fatjar.html
              * 02-appasembler.html
              * 03-assembly.html
              * apidocs/
              * auth.html
              * css/
              * fonts/
              * images/
              * img/
              * js/
              * rest.html
            * jars/
              * spring-demo-0.0.1-SNAPSHOT.jar
              * spring-expression-5.3.2.jar
              * spring-jcl-5.3.2.jar
              * spring-security-config-5.4.2.jar
              * spring-security-core-5.4.2.jar
              * spring-security-web-5.4.2.jar
              * spring-web-5.3.2.jar
              * spring-webmvc-5.3.2.jar
              * tomcat-embed-core-9.0.41.jar
              * tomcat-embed-websocket-9.0.41.jar
              * ...
            * src/
              * spring-demo-0.0.1-SNAPSHOT-sources.jar
