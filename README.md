cines
=====

A Java EE Web application (with Hibernate + Spring + Bootstrap + jQuery) to manage and consult a cinema billboard (select favorite cinema, create billboard, buy tickets, etc..).

=====

Pasos para ejecutar la aplicación:

1. Arrancar mysql:
	$ mysqld

2. Sobre el proyecto "cines":
	$ mvn install
	$ mvn sql:execute

3. Copiar $HOME/workspace/cines/target/cines.war a $TOMCAT/webapps

4. Ir al directorio de Tomcat (se requiere ser superusuario):
	$ cd \$TOMCAT

5. Ejecutar el script que arranca Tomcat:
	$ sh ./bin/startup.sh

6. Abrir un navegador e ir a la dirección http://localhost:8080/cines

7. Para realizar las pruebas contra la interfaz Web ejecutar el siguiente comando sobre el directorio del proyecto cines pruebasInterfaz. La base de datos debe haber sido inicializada previamente de la manera mostrada anteriormente.
	$ mvn test