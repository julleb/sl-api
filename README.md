# sl-api
:)
## Build & Run
	#Pre
	export JAVA_HOME=<pathToJava17>
	export PATH=$JAVA_HOME/bin:$PATH

	#Build
	./gradlew b #builds the jar

	#Run
	java -DAPI_KEY=<INSERT_API_KEY_HERE> -jar build/libs/sl-api-1.0-SNAPSHOT.jar