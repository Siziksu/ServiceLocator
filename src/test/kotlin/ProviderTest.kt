import locator.get
import locator.startLocator
import locator.stopLocator
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import sample.domain.models.TestBody
import sample.domain.models.TestFactoryPerson

class ProviderTest {

    @Before
    fun before() {
        stopLocator()
    }

    @Test
    fun `Different objects`() {
        startLocator {
            singleton { TestBody(180, 70) }
            provider { TestFactoryPerson(get()) }
        }

        val o1: TestFactoryPerson = get()
        val o2: TestFactoryPerson = get()

        assert(o1 !== o2)
    }

    @Test
    fun `Different hash objects`() {
        startLocator {
            singleton { TestBody(180, 70) }
            provider { TestFactoryPerson(get()) }
        }

        val o1: TestFactoryPerson = get()
        val o2: TestFactoryPerson = get()

        assertNotEquals(System.identityHashCode(o1), System.identityHashCode(o2))
    }
}
