#!/bin/sh
# Use Image type: Ubuntu 12.04

cd /home/ubuntu

# Install Git
echo 'y' | sudo apt-get install git

# Download DBLP dataset
wget http://dblp.uni-trier.de/xml/dblp.dtd
wget http://dblp.uni-trier.de/xml/dblp.xml.gz
gzip -d dblp.xml.gz

INPUT=/home/ubuntu/dblp.xml
OUTPUT=/home/ubuntu/dblp-apache-rdf/output/dblp-full.rdf
# Retrieve program
git clone https://github.com/AlfredAria/dblp-apache-rdf.git

#
cd dblp-apache-rdf
mkdir -p target/classes/
mkdir -p output
javac -cp ./libs/*:./src/* -d target/classes/ src/conversion/*.java src/query/*.java
cd /home/ubuntu/dblp-apache-rdf/target/classes/
java -Xmx28g -cp ./:/home/ubuntu/dblp-apache-rdf/libs/* conversion.ConvertXMLToRDF $INPUT $OUTPUT
java -Xmx28g -cp ./:/home/ubuntu/dblp-apache-rdf/libs/* query.RDFQuery

