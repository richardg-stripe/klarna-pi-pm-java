install:
	mvn install

run_klarna:
	mvn compile && mvn exec:java -Dexec.mainClass="com.stripe.sample.Klarna"
