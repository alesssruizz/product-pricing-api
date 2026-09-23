.PONY: all build test

all:
	@./gradlew clean build --warning-mode all

test:
	@./gradlew test --warning-mode all

lint:
	@./gradlew spotlessCheck

fix-lint:
	@./gradlew spotlessApply