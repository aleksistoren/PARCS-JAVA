all: run

clean:
	rm -f out/Main.jar out/MonteCarlo.jar

out/Main.jar: out/parcs.jar src/Main.java src/Data.java
	@javac -cp out/parcs.jar src/Main.java src/Data.java
	@jar cf out/Main.jar -C src Main.class -C src Data.class
	@rm -f src/Main.class src/Data.class

out/MonteCarlo.jar: out/parcs.jar src/MonteCarlo.java src/Data.java
	@javac -cp out/parcs.jar src/MonteCarlo.java src/Data.java
	@jar cf out/MonteCarlo.jar -C src MonteCarlo.class -C src Data.class
	@rm -f src/MonteCarlo.class src/Data.class

build: out/Main.jar out/MonteCarlo.jar

run: out/Main.jar out/MonteCarlo.jar
	@cd out && java -cp 'parcs.jar:Main.jar' Main
