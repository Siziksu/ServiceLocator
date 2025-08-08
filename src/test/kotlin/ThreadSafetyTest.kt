import locator.get
import locator.inject
import locator.startLocator
import locator.stopLocator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import sample.domain.models.TestB
import sample.domain.models.TestExpensiveService
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

class ThreadSafetyTest {

    @Before
    fun before() {
        TestExpensiveService.instanceCounter.set(0)
        stopLocator()
    }

    @Test
    fun `Concurrent access to same singleton is safe`() {
        startLocator {
            singleton { TestB() }
        }

        val results = mutableSetOf<TestB>()
        val threads = List(10) {
            Thread {
                results.add(get<TestB>())
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        assertEquals(1, results.size)
    }

    @Test
    fun `Inject lazy is safe across threads`() {
        startLocator {
            singleton { TestExpensiveService() }
        }

        val instances = ConcurrentLinkedQueue<TestExpensiveService>()
        val lazyService: Lazy<TestExpensiveService> = inject()
        val latch = CountDownLatch(1)

        val threads = List(50) {
            Thread {
                latch.await()
                instances += lazyService.value
            }
        }

        threads.forEach { it.start() }
        latch.countDown()
        threads.forEach { it.join() }

        val first = instances.first()
        instances.forEach {
            assertSame(first, it)
        }

        assertEquals(1, TestExpensiveService.instanceCounter.get())
    }

    @Test
    fun `Concurrent access to different types does not interfere`() {
        class S1
        class S2

        val resolvedS1 = AtomicReference<S1>()
        val resolvedS2 = AtomicReference<S2>()

        startLocator {
            singleton { S1() }
            singleton { S2() }
        }

        val latch = CountDownLatch(1)

        val t1 = Thread {
            latch.await()
            resolvedS1.set(get<S1>())
        }

        val t2 = Thread {
            latch.await()
            resolvedS2.set(get<S2>())
        }

        t1.start()
        t2.start()
        latch.countDown()
        t1.join()
        t2.join()

        assertNotNull(resolvedS1.get())
        assertNotNull(resolvedS2.get())
    }

    @Test
    fun `Multiple threads get same singleton instance`() {
        startLocator {
            singleton { TestExpensiveService() }
        }

        val instances = ConcurrentLinkedQueue<TestExpensiveService>()
        val latch = CountDownLatch(1)
        val threads = List(50) {
            Thread {
                latch.await()
                instances += get<TestExpensiveService>()
            }
        }

        threads.forEach { it.start() }
        latch.countDown()
        threads.forEach { it.join() }

        val first = instances.first()
        instances.forEach {
            assertSame(first, it)
        }

        assertEquals(1, TestExpensiveService.instanceCounter.get())
    }
}
