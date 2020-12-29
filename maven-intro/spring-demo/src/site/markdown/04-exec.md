maven-exec
==================

pom.xml
-------------

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.0.0</version>
    <executions>
        <execution>
            <phase>prepare-package</phase>
            <id>generate-git-id</id>
            <goals>
                <goal>exec</goal>
            </goals>
            <configuration>
                <executable>/usr/bin/bash</executable>
                <arguments>
                    <argument>${project.basedir}/src/assembly/build-step.sh</argument>
                    <argument>${project.basedir}/target</argument>
                </arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

src/assembly/build-step.sh
---------------------------

```shell
#!/bin/bash

echo "running build step"

if [ -d $1 ] ;
then
  git status 1>${1}/git-status
fi
```

