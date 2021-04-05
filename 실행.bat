cd ./bin/
java -cp ../jars/*; simpleIR.main.kuir -c ../html/
java -cp ../jars/*; simpleIR.main.kuir -k ./result/collection.xml
java -cp ../jars/*; simpleIR.main.kuir -i ./result/index.xml
pause