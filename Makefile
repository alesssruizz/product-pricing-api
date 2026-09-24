.PONY: build test

build:
	@./gradlew spotlessApply clean build --warning-mode all

test:
	@./gradlew test --warning-mode all

lint:
	@./gradlew spotlessCheck

fix-lint:
	@./gradlew spotlessApply
