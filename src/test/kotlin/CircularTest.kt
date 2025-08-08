import locator.get
import locator.inject
import locator.startLocator
import locator.stopLocator
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import sample.domain.models.TestCircularA
import sample.domain.models.TestCircularB

class CircularTest {

    @Before
    fun before() {
        stopLocator()

        startLocator {
            singleton<TestCircularA> { TestCircularA(get()) }
            singleton<TestCircularB> { TestCircularB(get()) }
        }
    }

    @Test
    fun `Throws on lazy circular dependency`() {
        startLocator {
            singleton<TestCircularA> { TestCircularA(get()) }
            singleton<TestCircularB> { TestCircularB(get()) }
        }
        val lazyB = inject<TestCircularB>()
        try {
            lazyB.value
            fail("Expected an Exception due to circular dependency")
        } catch (e: Exception) {
            assertEquals(
                "Circular dependency detected for sample.domain.models.TestCircularB, Bean sample.domain.models.TestCircularA not defined, Bean sample.domain.models.TestCircularB not defined",
                e.message
            )
        }
    }

    @Test
    fun `Circular detection for TestCircularA`() {
        try {
            get<TestCircularA>()
            fail("Expected an Exception due to circular dependency")
        } catch (e: Exception) {
            assertEquals(
                "Circular dependency detected for sample.domain.models.TestCircularA, Bean sample.domain.models.TestCircularB not defined, Bean sample.domain.models.TestCircularA not defined",
                e.message
            )
        }
    }

    @Test
    fun `Circular detection for TestCircularB`() {
        val lazyB = inject<TestCircularB>()
        try {
            lazyB.value
            fail("Expected an Exception due to circular dependency")
        } catch (e: Exception) {
            assertEquals(
                "Circular dependency detected for sample.domain.models.TestCircularB, Bean sample.domain.models.TestCircularA not defined, Bean sample.domain.models.TestCircularB not defined",
                e.message
            )
        }
    }
}
