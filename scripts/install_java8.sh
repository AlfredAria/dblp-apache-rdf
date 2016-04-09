#!/bin/sh
# Reference
# http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html

echo 'y' | sudo add-apt-repository ppa:webupd8team/java
echo 'y' | sudo apt-get update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
echo 'y' | sudo apt-get install oracle-java8-installer
echo 'y' | sudo apt-get install oracle-java8-set-default

