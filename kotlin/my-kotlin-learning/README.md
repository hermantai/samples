# Run the basics.kt program

    javac Developer.java -d /tmp/javaBuildTmp && \
    kotlinc basics.kt Developer.java -include-runtime -d /tmp/basics.jar && \
    kotlin -cp /tmp/basics.jar:/tmp/javaBuildTmp BasicsKt

Or just use run-basics.sh.
