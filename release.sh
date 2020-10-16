#!/usr/bin/env bash

#
# Copyright (c) 2014-2020 Aurélien Broszniowski
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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

for name in `find . -name pom.xml -exec dirname {} \;`
do
  echo "Working on $name"
  cd $name
  update $1
  cd -
  echo "---------------"
done

# mvn clean deploy -Prelease -DskipTests