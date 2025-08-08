import locator.Locator
import locator.inject
import locator.startLocator
import locator.stopLocator
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sample.app.Presenter
import sample.app.PresenterContract
import sample.data.repository.AnotherRepository
import sample.data.repository.StringRepository
import sample.data.repository.contract.AnotherRepositoryContract
import sample.data.repository.contract.StringRepositoryContract
import sample.domain.UseCaseMock
import sample.domain.contract.UseCaseContract
import sample.domain.models.TestBody
import sample.domain.models.TestPerson

class StringRepositoryTest {

    private val testBlock1: Locator.() -> Unit = {
        singleton<PresenterContract> { Presenter(get()) }
    }
    private val testBlock2: Locator.() -> Unit = {
        singleton<UseCaseContract> { UseCaseMock() }
        singleton<AnotherRepositoryContract> { AnotherRepository() }
    }
    private val testBlock3: Locator.() -> Unit = {
        singleton { TestPerson("Siziksu", 28, get()) }
        singleton<StringRepositoryContract> { StringRepository() }
        singleton { TestBody(180, 70) }
    }

    @Before
    fun before() {
        stopLocator()
    }

    @Test
    fun `Correct mock use case response without blocks`() {
        startLocator {
            singleton<PresenterContract> { Presenter(get()) }
            singleton<UseCaseContract> { UseCaseMock() }
            singleton<AnotherRepositoryContract> { AnotherRepository() }
            singleton<StringRepositoryContract> { StringRepository() }
        }

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        val useCase: UseCaseContract by inject()
        assertEquals("Raquel", useCase.start())
    }

    @Test
    fun `Correct mock use case response through presenter call without blocks`() {
        startLocator {
            singleton<PresenterContract> { Presenter(get()) }
            singleton<UseCaseContract> { UseCaseMock() }
            singleton<AnotherRepositoryContract> { AnotherRepository() }
            singleton<StringRepositoryContract> { StringRepository() }
        }

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        val presenter: PresenterContract by inject()
        assertEquals("Raquel", presenter.start())
    }

    @Test
    fun `Correct value for StringRepository without blocks`() {
        startLocator {
            singleton<PresenterContract> { Presenter(get()) }
            singleton<UseCaseContract> { UseCaseMock() }
            singleton<AnotherRepositoryContract> { AnotherRepository() }
            singleton<StringRepositoryContract> { StringRepository() }
        }

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        assertEquals("Siziksu", stringRepository.get("name"))
    }

    @Test
    fun `Correct value from AnotherRepository direct call without blocks`() {
        startLocator {
            singleton<PresenterContract> { Presenter(get()) }
            singleton<UseCaseContract> { UseCaseMock() }
            singleton<AnotherRepositoryContract> { AnotherRepository() }
            singleton<StringRepositoryContract> { StringRepository() }
        }

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        val anotherRepository: AnotherRepositoryContract by inject()
        assertEquals("Another repository", anotherRepository.function())
    }

    @Test
    fun `Correct mock use case response`() {
        startLocator(testBlock1, testBlock2, testBlock3)

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        val useCase: UseCaseContract by inject()
        assertEquals("Raquel", useCase.start())
    }

    @Test
    fun `Correct mock use case response through presenter call`() {
        startLocator(testBlock1, testBlock2, testBlock3)

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        val presenter: PresenterContract by inject()
        assertEquals("Raquel", presenter.start())
    }

    @Test
    fun `Correct value for StringRepository`() {
        startLocator(testBlock1, testBlock2, testBlock3)

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        assertEquals("Siziksu", stringRepository.get("name"))
    }

    @Test
    fun `Correct value from AnotherRepository direct call`() {
        startLocator(testBlock1, testBlock2, testBlock3)

        val stringRepository: StringRepositoryContract by inject()
        stringRepository.set("name", "Siziksu")

        val anotherRepository: AnotherRepositoryContract by inject()
        assertEquals("Another repository", anotherRepository.function())
    }
}
