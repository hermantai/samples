#!/bin/bash
KOTLIN=$(which kotlin)
KOTLINC=$(which kotlinc)
JAVAC=$(which javac)

echo $JAVAC $KOTLINC $KOTLIN

$JAVAC Developer.java -d /tmp/javaBuildTmp && \
$KOTLINC basics.kt Developer.java -include-runtime -d /tmp/basics.jar && \
$KOTLIN -cp /tmp/basics.jar:/tmp/javaBuildTmp BasicsKt
