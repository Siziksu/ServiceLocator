import locator.get
import locator.startLocator
import locator.stopLocator
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sample.domain.models.TestBody
import sample.domain.models.TestPerson

class SingletonTest {

    @Before
    fun before() {
        stopLocator()
    }

    @Test
    fun `Same objects`() {
        startLocator {
            singleton { TestPerson("Siziksu", 28, get()) }
            singleton { TestBody(180, 70) }
        }

        val o1: TestPerson = get()
        val o2: TestPerson = get()

        assert(o1 === o2)
    }

    @Test
    fun `Same hash objects`() {
        startLocator {
            singleton { TestPerson("Siziksu", 28, get()) }
            singleton { TestBody(180, 70) }
        }

        val o1: TestPerson = get()
        val o2: TestPerson = get()

        assertEquals(System.identityHashCode(o1), System.identityHashCode(o2))
    }
}
