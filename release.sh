#!/usr/bin/env bash

update() {
  sed -i bak -e s/$1-SNAPSHOT/$1/g pom.xml
  git commit -m "release $1"
  git tag -a v$1 -m "$1"
  git push --force --tags
}

if [ "$#" -ne 1 ]; then
    echo "1 parameter needed: SNAPSHOT version (without -SNAPSHOT)"
    exit
fi

update $1

for name in `ls -d */`
do
  echo "Working on $name"
  cd $name
  update $1
  cd ..
  echo "---------------"
done

# mvn clean deploy -Prelease -DskipTests