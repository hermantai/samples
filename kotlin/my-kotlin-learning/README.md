# Run the basics.kt program

    kotlinc basics.kt *.java -include-runtime -d /tmp/basics.jar && \
    javac *.java -d /tmp/javaBuildTmp && \
    kotlin -cp /tmp/basics.jar:/tmp/javaBuildTmp BasicsKt

Or just use run-basics.sh.
