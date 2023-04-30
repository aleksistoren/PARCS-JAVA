all: run

clean:
	rm -f out/Cipher.jar out/StringConverter.jar

out/Cipher.jar: out/parcs.jar src/Cipher.java src/CipherScheme.java src/Data.java
	@javac -cp out/parcs.jar src/Cipher.java src/CipherScheme.java src/Data.java
	@jar cf out/Cipher.jar -C src Cipher.class -C src CipherScheme.class -C src Data.class
	@rm -f src/Cipher.class src/CipherScheme.class src/Data.class

out/StringConverter.jar: out/parcs.jar src/StringConverter.java src/CipherScheme.java src/Data.java
	@javac -cp out/parcs.jar src/StringConverter.java src/CipherScheme.java src/Data.java
	@jar cf out/StringConverter.jar -C src StringConverter.class -C src CipherScheme.class -C src Data.class
	@rm -f src/StringConverter.class src/CipherScheme.class src/Data.class

build: out/Cipher.jar out/StringConverter.jar

run: out/Cipher.jar out/StringConverter.jar
	@cd out && java -cp 'parcs.jar:Cipher.jar' Cipher