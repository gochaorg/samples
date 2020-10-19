#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/bellsoft-java8-full-amd64
JAVA=$JAVA_HOME/bin/java
MVN_REPO=/home/user/code/jfrog-artifactory/usr1/repo
MVN_SETTINGS=/home/user/code/jfrog-artifactory/usr1/settings/settings.xml

mvn -s $MVN_SETTINGS -Dmaven.repo.local=$MVN_REPO clean deploy