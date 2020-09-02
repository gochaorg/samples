Тестирование The Checker Framework
===================================

О The Checker Framework
-------------------------

[The Checker Framework](https://checkerframework.org/) - статический анализатор кода, 
путем расщирения системы типов JAVA через аннотации.


Ссылки
* [Исходники на github](https://github.com/typetools/checker-framework)
* [Презентация на русском 1](https://ppt-online.org/296694) 
* [Презентация на русском 2](https://2014.jokerconf.com/presentations/chashnikov.pdf) 
* [Примеры для версии java 11](https://github.com/typetools/checker-framework/tree/master/docs/examples/MavenExampleJDK11)
+ [Лекция 45 мин](https://www.lektorium.tv/lecture/14537) 

Тесты
-----------

Тесты проводятся на **java версии 8**


Настройка
----------------

### enviroment
Настроить переменные окружения

    export JAVA_HOME=/usr/lib/jvm/bellsoft-java8-full-amd64
    export PATH=/usr/lib/jvm/bellsoft-java8-full-amd64/bin/:$PATH
    
### maven

Указать свойства

```xml
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>

        <!-- These properties will be set by the Maven Dependency plugin -->
        <annotatedJdk>${org.checkerframework:jdk8:jar}</annotatedJdk>
        <errorProneJavac>${com.google.errorprone:javac:jar}</errorProneJavac>
    </properties>
```

Зависимости
```xml
<dependencies>
    ... existing <dependency> items ...
    <!-- Annotations from the Checker Framework: nullness, interning, locking, ... -->
    <dependency>
        <groupId>org.checkerframework</groupId>
        <artifactId>checker-qual</artifactId>
        <version>3.6.0</version>
    </dependency>
    <!-- If using JDK 8, add the following additional dependency. -->
    <dependency>
        <groupId>com.google.errorprone</groupId>
        <artifactId>javac</artifactId>
        <version>9+181-r4173-1</version>
    </dependency>
</dependencies>
```
