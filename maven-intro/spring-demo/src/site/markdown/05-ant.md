ant
======================

pom.xml
----------------

```xml
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>1.7</version>
    <executions>
        <execution>
            <phase>package</phase>
            <configuration>
                <tasks>
                    <tar basedir="target/${project.artifactId}-${project.version}-dist/${project.artifactId}-${project.version}" destfile="target/${project.artifactId}-${project.version}.tar" />
                    <gzip src="target/${project.artifactId}-${project.version}.tar" destfile="target/${project.artifactId}-${project.version}.tar.gz"  />
                </tasks>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

log
--------------------

```
[INFO] --- maven-antrun-plugin:1.7:run (default) @ spring-demo ---
[WARNING] Parameter tasks is deprecated, use target instead
[INFO] Executing tasks

main:
      [tar] Building tar: /home/user/code/samples/maven-intro/spring-demo/target/spring-demo-0.0.1-SNAPSHOT.tar
     [gzip] Building: /home/user/code/samples/maven-intro/spring-demo/target/spring-demo-0.0.1-SNAPSHOT.tar.gz
[INFO] Executed tasks
```

