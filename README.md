# tabd
Java app
## Instalation with maven the ojdbc6 driver
```
cd java_application

mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=ojdbc6.jar -DgeneratePom=true
```

Previously, we must install the maven dependencies with netbeans to run the project in debug mode.

Practice with Cassandra

Practice with Neo4j
