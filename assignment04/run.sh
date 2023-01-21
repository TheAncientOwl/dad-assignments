echo ">> Compilation started..."
rm bin -r 2> /dev/null
rm sources.txt 2> /dev/null

find -name "*.java" > sources.txt
javac @sources.txt -d bin

echo ">> Run Server"
java -cp bin eu.ase.httpserver.HTTPMultiServer 25565
