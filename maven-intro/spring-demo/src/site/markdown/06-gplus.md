gplus
=============

pom.xml
---------------

```xml
<plugin>
    <groupId>org.codehaus.gmavenplus</groupId>
    <artifactId>gmavenplus-plugin</artifactId>
    <version>1.12.0</version>
    <executions>
        <execution>
            <id>execute</id>
            <goals>
                <goal>execute</goal>
            </goals>
            <phase>generate-sources</phase>
        </execution>
    </executions>
    <configuration>
        <properties>
            <property>
                <name>someProp</name>
<!--							<value>${someProp}</value>-->
                <value>${project.artifactId}</value>
            </property>
        </properties>
        <scripts>
            <script><![CDATA[
            @Grapes([
              @Grab(group='org.apache.commons', module='commons-lang3', version='3.3.2')
            ])
            import org.apache.commons.lang3.SystemUtils
            log.info("The settings are " + session.settings)
            log.info("This session's goals are " + session.goals)
            log.info("The local repository is " + session.localRepository)
            log.info("The reactor projects are " + session.sortedProjects)
            log.info("The plugin artifacts are " + pluginArtifacts)
            log.info("The mojo execution is " + mojoExecution)
            log.info("The plugin descriptor is " + mojoExecution.mojoDescriptor)
            log.info("someProp is " + someProp)
            log.info("projectProp is " + project.properties['projectProp'])
            log.info("Using Java " + SystemUtils.JAVA_VERSION)
            assert ant.project.baseDir == project.basedir
            // the first reference is not filtered by Maven, the second reference is
            assert "$project.name" == "${project.name}"
          ]]></script>
<!--						<script>file:///${project.basedir}/src/main/resources/groovyScripts/someScript.groovy</script>-->
            <script>${project.basedir}/src/assembly/get-gitcommit.groovy</script>
        </scripts>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-all</artifactId>
            <!-- any version of Groovy \>= 1.5.0 should work here -->
            <version>3.0.6</version>
            <type>pom</type>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.eclipse.jgit</groupId>
            <artifactId>org.eclipse.jgit</artifactId>
            <version>5.10.0.202012080955-r</version>
        </dependency>
    </dependencies>
</plugin>
```

log
-------------------

```
[INFO] --- gmavenplus-plugin:1.12.0:execute (execute) @ spring-demo ---
[INFO] Using plugin classloader, includes GMavenPlus and project classpath.
[INFO] Using Groovy 3.0.6 to perform execute.
[INFO] The settings are org.apache.maven.execution.SettingsAdapter@7eae55
[INFO] This session's goals are [package]
[INFO] The local repository is       id: local
      url: file:///home/user/.m2/repository/
   layout: default
snapshots: [enabled => true, update => always]
 releases: [enabled => true, update => always]

[INFO] The reactor projects are [MavenProject: org.example:spring-demo:0.0.1-SNAPSHOT @ /home/user/code/samples/maven-intro/spring-demo/pom.xml]
[INFO] The plugin artifacts are [org.codehaus.gmavenplus:gmavenplus-plugin:maven-plugin:1.12.0:, org.codehaus.groovy:groovy-all:pom:3.0.6:runtime, org.codehaus.groovy:groovy:jar:3.0.6:runtime, org.codehaus.groovy:groovy-ant:jar:3.0.6:runtime, org.apache.ant:ant-junit:jar:1.10.8:runtime, org.apache.ant:ant-antlr:jar:1.10.8:runtime, org.codehaus.groovy:groovy-astbuilder:jar:3.0.6:runtime, org.codehaus.groovy:groovy-cli-picocli:jar:3.0.6:runtime, info.picocli:picocli:jar:4.5.1:runtime, org.codehaus.groovy:groovy-console:jar:3.0.6:runtime, org.codehaus.groovy:groovy-datetime:jar:3.0.6:runtime, org.codehaus.groovy:groovy-docgenerator:jar:3.0.6:runtime, com.thoughtworks.qdox:qdox:jar:1.12.1:runtime, org.codehaus.groovy:groovy-groovydoc:jar:3.0.6:runtime, com.github.javaparser:javaparser-core:jar:3.16.1:runtime, org.codehaus.groovy:groovy-groovysh:jar:3.0.6:runtime, org.codehaus.groovy:groovy-jmx:jar:3.0.6:runtime, org.codehaus.groovy:groovy-json:jar:3.0.6:runtime, org.codehaus.groovy:groovy-jsr223:jar:3.0.6:runtime, org.codehaus.groovy:groovy-macro:jar:3.0.6:runtime, org.codehaus.groovy:groovy-nio:jar:3.0.6:runtime, org.codehaus.groovy:groovy-servlet:jar:3.0.6:runtime, org.codehaus.groovy:groovy-sql:jar:3.0.6:runtime, org.codehaus.groovy:groovy-swing:jar:3.0.6:runtime, org.codehaus.groovy:groovy-templates:jar:3.0.6:runtime, org.codehaus.groovy:groovy-test:jar:3.0.6:runtime, junit:junit:jar:4.13:runtime, org.hamcrest:hamcrest-core:jar:1.3:runtime, org.codehaus.groovy:groovy-test-junit5:jar:3.0.6:runtime, org.junit.jupiter:junit-jupiter-api:jar:5.7.0:runtime, org.opentest4j:opentest4j:jar:1.2.0:runtime, org.junit.platform:junit-platform-launcher:jar:1.7.0:runtime, org.junit.platform:junit-platform-engine:jar:1.7.0:runtime, org.junit.platform:junit-platform-commons:jar:1.7.0:runtime, org.junit.jupiter:junit-jupiter-engine:jar:5.7.0:runtime, org.codehaus.groovy:groovy-testng:jar:3.0.6:runtime, org.testng:testng:jar:7.3.0:runtime, com.beust:jcommander:jar:1.78:runtime, org.codehaus.groovy:groovy-xml:jar:3.0.6:runtime, org.eclipse.jgit:org.eclipse.jgit:jar:5.10.0.202012080955-r:runtime, com.googlecode.javaewah:JavaEWAH:jar:1.1.7:runtime, org.slf4j:slf4j-api:jar:1.7.30:runtime, org.apache.maven:maven-plugin-api:jar:3.0:compile, org.apache.maven:maven-artifact:jar:3.0:compile, org.sonatype.sisu:sisu-inject-plexus:jar:1.4.2:compile, org.sonatype.sisu:sisu-inject-bean:jar:1.4.2:compile, org.sonatype.sisu:sisu-guice:jar:noaop:2.1.7:compile, org.apache.maven:maven-model:jar:3.0:compile, org.codehaus.plexus:plexus-utils:jar:2.0.4:compile, org.apache.maven:maven-core:jar:3.0:compile, org.apache.maven:maven-settings:jar:3.0:compile, org.apache.maven:maven-settings-builder:jar:3.0:compile, org.apache.maven:maven-repository-metadata:jar:3.0:compile, org.apache.maven:maven-model-builder:jar:3.0:compile, org.apache.maven:maven-aether-provider:jar:3.0:runtime, org.sonatype.aether:aether-impl:jar:1.7:compile, org.sonatype.aether:aether-spi:jar:1.7:compile, org.sonatype.aether:aether-api:jar:1.7:compile, org.sonatype.aether:aether-util:jar:1.7:compile, org.codehaus.plexus:plexus-interpolation:jar:1.14:compile, org.codehaus.plexus:plexus-classworlds:jar:2.2.3:compile, org.codehaus.plexus:plexus-component-annotations:jar:1.5.5:compile, org.sonatype.plexus:plexus-sec-dispatcher:jar:1.3:compile, org.sonatype.plexus:plexus-cipher:jar:1.4:compile, org.apache.maven.shared:file-management:jar:3.0.0:compile, org.apache.maven.shared:maven-shared-io:jar:3.0.0:compile, org.apache.maven:maven-compat:jar:3.0:compile, org.apache.maven.wagon:wagon-provider-api:jar:2.10:compile, org.apache.maven.shared:maven-shared-utils:jar:3.0.0:compile, com.google.code.findbugs:jsr305:jar:2.0.1:compile, org.apache.maven:maven-archiver:jar:3.3.0:compile, commons-io:commons-io:jar:2.5:compile, org.codehaus.plexus:plexus-archiver:jar:3.6.0:compile, org.codehaus.plexus:plexus-io:jar:3.0.1:compile, org.apache.commons:commons-compress:jar:1.16.1:compile, org.objenesis:objenesis:jar:2.6:compile, org.iq80.snappy:snappy:jar:0.4:compile, org.tukaani:xz:jar:1.8:runtime, org.fusesource.jansi:jansi:jar:1.17.1:runtime, jline:jline:jar:2.14.6:runtime, org.apache.ant:ant:jar:1.10.8:runtime, org.apache.ant:ant-launcher:jar:1.10.8:runtime, org.apache.ivy:ivy:jar:2.5.0:runtime]
[INFO] The mojo execution is org.codehaus.gmavenplus:gmavenplus-plugin:1.12.0:execute {execution: execute}
[INFO] The plugin descriptor is org.apache.maven.plugin.descriptor.MojoDescriptor [role: 'org.apache.maven.plugin.Mojo', hint: 'org.codehaus.gmavenplus:gmavenplus-plugin:1.12.0:execute', realm: ClassRealm[plugin>org.codehaus.gmavenplus:gmavenplus-plugin:1.12.0, parent: jdk.internal.loader.ClassLoaders$AppClassLoader@5bc2b487]]
[INFO] someProp is spring-demo
[INFO] projectProp is null
[INFO] Using Java 14.0.2
[INFO] Running Groovy script from /home/user/code/samples/maven-intro/spring-demo/src/assembly/get-gitcommit.groovy.
branch refs/heads/master
commit 87cbb2068efb095c14d2d73659133a611529f7ba
```

src/assembly/get-gitcommit.groovy
----------------------------------

```groovy
import org.eclipse.jgit.lib.*

try {

    // find .git dir
    File dir = new File(project.basedir.toString()).getAbsoluteFile().getCanonicalFile()
    File gitdir = null
    while (true) {
        if (dir.name == '.git') {
            gitdir = dir
            break
        }
        File tdir = new File(dir, '.git')
        if (tdir.exists() && tdir.isDirectory()) {
            gitdir = tdir
            break
        }
        if (dir.getParentFile() == null) break
        dir = dir.getParentFile()
    }

    if (gitdir == null) {
        println "git dir not found"
        return;
    }

    // read git info
    def repo = new RepositoryBuilder().setGitDir(gitdir).build()

    // write commit id, if branch found
    if (repo.fullBranch) {
        println "branch $repo.fullBranch"
        def ref = repo.findRef(repo.fullBranch)
        if (ref != null) {
            def cmtId = new StringWriter()
            ref.objectId.copyTo(cmtId)
            println "commit $cmtId"

            def commitTxt = new File(project.basedir, 'target/git-commit.txt')
            commitTxt.setText("""|branch $repo.fullBranch
                             |commit $cmtId""".stripMargin())
        }
    }

} catch ( Throwable err ){
    println err
}
```