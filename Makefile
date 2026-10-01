.PHONY: all test lint fix-lint run

all:
	@./gradlew clean build --warning-mode all

test:
	@./gradlew test --warning-mode all

lint:
	@./gradlew checkstyleMain checkstyleTest spotbugsMain spotbugsTest

fix-lint:
	@./gradlew spotlessApply --info

run:
	@./gradlew bootRun
