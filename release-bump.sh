#!/usr/bin/env bash

update() {
  sed -i bak -e s/$1/$2-SNAPSHOT/g pom.xml
  git commit -m "bumped up to $2-SNAPSHOT"
  git push
}

if [ "$#" -ne 2 ]; then
    echo "2 parameters needed: old version, new version"
fi

update $1 $2

for name in `ls -d */`
do
  echo "Working on $name"
  cd $name
  update $1 $2
  cd ..
  echo "---------------"
done