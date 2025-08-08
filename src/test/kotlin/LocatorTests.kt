import locator.get
import locator.inject
import locator.startLocator
import locator.stopLocator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import sample.domain.models.TestA
import sample.domain.models.TestB
import sample.domain.models.TestFactoryBody
import sample.domain.models.TestPerson

class LocatorTests {

    @Before
    fun before() {
        stopLocator()
    }

    @Test
    fun `Can resolve simple provider`() {
        startLocator {
            provider { TestFactoryBody() }
        }

        val testFactoryBody = get<TestFactoryBody>()
        assertNotNull(testFactoryBody)
    }

    @Test
    fun `Can resolve simple singleton`() {
        startLocator {
            singleton { TestB() }
        }

        val testB1 = get<TestB>()
        val b2 = get<TestB>()
        assertSame(testB1, b2)
    }

    @Test
    fun `Can resolve dependency chain`() {
        startLocator {
            singleton { TestB() }
            singleton { TestA(get()) }
        }

        val testA = get<TestA>()
        assertNotNull(testA)
        assertEquals(testA.talk(), "I am A and have a B")
    }

    @Test
    fun `Throws if missing bean`() {
        startLocator {
            singleton<TestPerson> { TestPerson("Siziksu", 28, get()) }
        }

        val lazyPerson = inject<TestPerson>()
        try {
            lazyPerson.value
            fail("Expected an Exception due to Bean not defined")
        } catch (e: Exception) {
            assertEquals("Bean sample.domain.models.TestBody not defined, Bean sample.domain.models.TestPerson not defined", e.message)
        }
    }

    @Test
    fun `Throws if bean not registered`() {
        startLocator { }
        try {
            get<TestB>()
            fail("Expected an Exception due to circular dependency")
        } catch (e: Exception) {
            assertEquals("Bean sample.domain.models.TestB not defined", e.message)
        }
    }

    @Test
    fun `Inject lazy works`() {
        startLocator {
            singleton { TestB() }
        }

        val lazyB: Lazy<TestB> = inject()
        assertFalse(lazyB.isInitialized())
        assertNotNull(lazyB.value)
        assertTrue(lazyB.isInitialized())
    }

    @Test
    fun `Multiple blocks register correctly`() {
        stopLocator()
        startLocator(
            { singleton { TestB() } },
            { singleton { TestA(get()) } }
        )

        val testA = get<TestA>()
        assertEquals(testA.talk(), "I am A and have a B")
    }
}
