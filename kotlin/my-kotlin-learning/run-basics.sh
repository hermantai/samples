set -e
#!/bin/bash
KOTLIN=$(which kotlin)
KOTLINC=$(which kotlinc)
JAVAC=$(which javac)
COROUTINES=kotlinx-coroutines-core-jvm-1.5.1.jar

echo $JAVAC $KOTLINC $KOTLIN

mkdir -p /tmp/javaBuildTmp
$KOTLINC -jvm-target 1.8 -cp $COROUTINES basics.kt *.java -include-runtime -d /tmp/basics.jar && \
echo finished kotlinc && \
$JAVAC *.java -d /tmp/javaBuildTmp -cp /tmp/basics.jar && \
echo finished javac && \
$KOTLIN -cp /tmp/basics.jar:/tmp/javaBuildTmp:$COROUTINES BasicsKt
