# Service Locator

## Tests

`build.gradle.kts`

```groovy
tasks.test {
    useJUnit()
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
```

Command

```bash
.\gradlew clean test
```

## Examples

```kotlin
startLocator {
    singleton<PresenterContract> { Presenter(get()) }
    singleton<UseCaseContract> { UseCaseMock() }
    singleton<AnotherRepositoryContract> { AnotherRepository() }
    singleton<StringRepositoryContract> { StringRepository() }
}
```

```kotlin
val block: Locator.() -> Unit = {
    singleton<PresenterContract> { Presenter(get()) }
    singleton<UseCaseContract> { UseCase(get(), get()) }
    singleton<AnotherRepositoryContract> { AnotherRepository() }
    singleton { Person("Siziksu", 28, get()) }
    singleton<StringRepositoryContract> { StringRepository() }
    singleton { Body(180, 70) }
}

startLocator(block)
```

```kotlin
import locator.inject

class App {

    private val presenter: PresenterContract by inject()

    fun start() {
        println(presenter.start())
    }
}
```

```kotlin
import locator.get
import sample.domain.models.Person

class App {

    private val person: Person by lazy { get() }

    fun start() {
        println(person)
    }
}
```

```kotlin
import locator.get
import sample.domain.models.Person

class App {

    fun start() {
        val person: Person = get()
        println(person)
    }
}
```
