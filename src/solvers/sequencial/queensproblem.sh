#!/bin/bash

# Nome do arquivo Java (sem extensão .java)
JAVA_CLASS="QueensProblem"

# Diretório onde o arquivo Java está localizado
SOURCE_DIR="solvers/sequencial/"

# Navega para o diretório onde está o arquivo Java
cd $SOURCE_DIR

# Compila a classe Java
javac ${JAVA_CLASS}.java

for ((i=1; i<=NUM_ITERATIONS; i++))
do
    echo "Execução $i" >> $OUTPUT_FILE
    java $JAVA_CLASS >> $OUTPUT_FILE 2>&1
done
