:: mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>

::call mvn install:install-file -Dfile=jacob-1.20\jacob.jar -DgroupId=xyz.cofe -DartifactId=jacob -Dversion=1.20 -Dpackaging=jar
::call mvn install:install-file -Dfile=jacob-1.20\jacob-1.20-x64.dll -DgroupId=xyz.cofe -DartifactId=jacob-x64 -Dversion=1.20 -Dpackaging=dll
::call mvn install:install-file -Dfile=jacob-1.20\jacob-1.20-x86.dll -DgroupId=xyz.cofe -DartifactId=jacob-x86 -Dversion=1.20 -Dpackaging=dll

:: mvn deploy:deploy-file -DgroupId=<group-id> \
::   -DartifactId=<artifact-id> \
::   -Dversion=<version> \
::   -Dpackaging=<type-of-packaging> \
::   -Dfile=<path-to-file> \
::   -DrepositoryId=<id-to-map-on-server-section-of-settings.xml> \
::   -Durl=<url-of-the-repository-to-deploy>

call mvn deploy:deploy-file -DgroupId=net.sf.jacob-project -DartifactId=jacob -Dversion=1.20 -Dpackaging=jar -Dfile=jacob-1.20\jacob.jar -DrepositoryId=nexus-snapshotServerId -Durl=http://10.1.18.36:8081/repository/assets