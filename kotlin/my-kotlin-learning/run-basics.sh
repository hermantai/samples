#!/bin/bash
KOTLIN=$(which kotlin)
KOTLINC=$(which kotlinc)
JAVAC=$(which javac)

echo $JAVAC $KOTLINC $KOTLIN

$KOTLINC basics.kt *.java -include-runtime -d /tmp/basics.jar && \
$JAVAC *.java -d /tmp/javaBuildTmp -cp /tmp/basics.jar && \
$KOTLIN -cp /tmp/basics.jar:/tmp/javaBuildTmp BasicsKt
