IsometricGame

Compile this project with 

mvn integration-test -Pdesktop 

To run the tests: 

Depending on your system, after running the build, locate the snapshot and the GDX jar in the .m2 file downloaded by Maven. 

Manually add these to the classpath as shown in an example below: 

javac -d . -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/home/dev1/GitProjects/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar Test.java

java -ea -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/home/dev1/GitProjects/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar com.isometricgame.core.Test
