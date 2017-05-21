Пример использования
===================

Создание проекта Maven/Java
---------------------------

Выполнить в терминале

	user@user-VirtualBox:~/code$ mvn archetype:generate \
	-DgroupId=cofe.xyz \
	-DartifactId=sample-app \
	-DarchetypeArtifactId=maven-archetype-quickstart \
	-DinteractiveMode=false

Добавить зависимость в проект
-----------------------------

В файле *sample-app/pom.xml* вставить текст

	<project>
		..
		<dependencies>
			..
			<dependency>
				<groupId>xyz.cofe</groupId>
				<artifactId>template</artifactId>
				<version>0.1</version>
			</dependency>


Добавить код Java
-----------------

Открыть файл *sample-app/src/main/java/cofe/xyz/App.java*

И отредактировать его примерно так

	package cofe.xyz;

	import xyz.cofe.text.template.BasicTemplate;
	import java.util.*;

	/**
	 * Hello world!
	 */
	public class App 
	{
	    public static void main( String[] args )
	    {
	    	LinkedHashMap vars = new LinkedHashMap();
	    	vars.put( "v1", "sample 1" );
	    	vars.put( "v2", 123 );

		BasicTemplate.template( 
			"teplate test ${a}\n" +
			"  vars: ${v.v1}\n" +
			"        ${v.v2}"
	    	)
	    		.bind( "a", 1 )
	    		.bind( "v", vars )
	    		.println();
	    }
	}
	
Скомпилировать код
------------------

В терминале выполнить

	user@user-VirtualBox:~/code/sample-app$ mvn compile
	
Выполнить код
-------------

Отредактировать *pom.xml*, добавить опцию

	<project>
		..
		<build>
			<plugins>
			..
	
				<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>exec-maven-plugin</artifactId>
				<version>1.2.1</version>
				<executions>
					<execution>
						<goals>
							<goal>java</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<mainClass>cofe.xyz.App</mainClass>
					<arguments>
						<argument>foo</argument>
						<argument>bar</argument>
					</arguments>
				</configuration>
				</plugin>

В терминале выполнить

	user@user-VirtualBox:~/code/sample-app$ mvn exec:java