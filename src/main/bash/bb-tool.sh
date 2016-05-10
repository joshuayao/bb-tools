#!/bin/bash

JAVA6_HOME_CANDIDATES='\
    /usr/lib/j2sdk1.6-sun \
    /usr/lib/jvm/java-6-sun \
    /usr/lib/jvm/java-1.6.0-sun-1.6.0.* \
    /usr/lib/jvm/java-1.6.0-sun-1.6.0.*/jre \
    /usr/lib/jvm/j2sdk1.6-oracle \
    /usr/lib/jvm/j2sdk1.6-oracle/jre \
    /usr/java/jdk1.6* \
    /usr/java/jre1.6*'

OPENJAVA6_HOME_CANDIDATES='\
    /usr/lib/jvm/java-1.6.0-openjdk \
    /usr/lib/jvm/java-1.6.0-openjdk-* \
    /usr/lib/jvm/jre-1.6.0-openjdk*'

JAVA7_HOME_CANDIDATES='\
    /usr/java/jdk1.7* \
    /usr/java/jre1.7* \
    /usr/lib/jvm/j2sdk1.7-oracle \
    /usr/lib/jvm/j2sdk1.7-oracle/jre \
    /usr/lib/jvm/java-7-oracle*'

OPENJAVA7_HOME_CANDIDATES='\
    /usr/lib/jvm/java-1.7.0-openjdk* \
    /usr/lib/jvm/java-7-openjdk*'

JAVA8_HOME_CANDIDATES='\
    /usr/java/jdk1.8* \
    /usr/java/jre1.8* \
    /usr/lib/jvm/j2sdk1.8-oracle \
    /usr/lib/jvm/j2sdk1.8-oracle/jre \
    /usr/lib/jvm/java-8-oracle*'

OPENJAVA8_HOME_CANDIDATES='\
    /usr/lib/jvm/java-1.8.0-openjdk* \
    /usr/lib/jvm/java-8-openjdk*'


MISCJAVA_HOME_CANDIDATES='\
    /Library/Java/Home \
    /usr/java/default \
    /usr/lib/jvm/default-java \
    /usr/lib/jvm/java-openjdk \
    /usr/lib/jvm/jre-openjdk'

case $JAVA_MAJOR in
  6) JAVA_HOME_CANDIDATES="$JAVA6_HOME_CANDIDATES"
     ;;
  7) JAVA_HOME_CANDIDATES="$JAVA7_HOME_CANDIDATES $OPENJAVA7_HOME_CANDIDATES"
     ;;
  8) JAVA_HOME_CANDIDATES="$JAVA8_HOME_CANDIDATES $OPENJAVA8_HOME_CANDIDATES"
          ;;
  *) JAVA_HOME_CANDIDATES="$JAVA7_HOME_CANDIDATES     \
                           $MISCJAVA_HOME_CANDIDATES  \
                           $OPENJAVA7_HOME_CANDIDATES \
                           $JAVA6_HOME_CANDIDATES     \
                           $OPENJAVA6_HOME_CANDIDATES"
     ;;
esac

# attempt to find java
if [ -z "$JAVA_HOME" ]; then
  for candidate_regex in $JAVA_HOME_CANDIDATES ; do
      for candidate in `ls -rd $candidate_regex 2>/dev/null`; do
        if [ -e $candidate/bin/java ]; then
          export JAVA_HOME=$candidate
          break 2
        fi
      done
  done
fi


if [[ @${JAVA_HOME}@ == @@ ]]
then
  echo "No JAVA_HOME defined!"
  exit 1
fi
export JAVA_CMD=${JAVA_HOME}/bin/java
export PATH=$JAVA_HOME/bin:$PATH

if [[ @${BB_TOOL_HOME}@ == @@ ]]
then
  export BB_TOOL_HOME=`pwd`
fi

JARS=$BB_TOOL_HOME/conf

for conf in ${CONF_DIRS[@]}; do
    JARS=$JARS:$conf
done

for dir in ${JAR_DIRS[@]}; do
     for jar in `ls $dir`
     do
       JARS="$JARS:$dir/$jar"
     done
done

for jar in `ls $BB_TOOL_HOME/lib`
do
  JARS="$JARS:${BB_TOOL_HOME}/lib/$jar"
done

#echo "JAVA_HOME = ${JAVA_HOME}"
#echo "classpath = $JARS"
#echo "java.library.path = $LD_LIBRARY_PATH"

#TEST_OPTS="-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8001"
$JAVA_CMD -Djava.library.path=${LD_LIBRARY_PATH} -cp "$JARS:$EXT_CP" com.intel.bb.tool.log.LogFetcher $@