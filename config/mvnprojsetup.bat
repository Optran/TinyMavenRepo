@echo off
mvn --settings "E:\maven\mavenrepo\settings.xml" archetype:generate -DgroupId=com.ashutoshwad.tools -DartifactId=test-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
