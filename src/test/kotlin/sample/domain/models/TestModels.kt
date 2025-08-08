package sample.domain.models

import java.util.concurrent.atomic.AtomicInteger

class TestCircularA(private val b: TestCircularB) {

    fun who() = "A"
    fun talk() = "I have a ${b.who()}"
}

class TestCircularB(private val a: TestCircularA) {

    fun who() = "B"
    fun talk() = "I have a ${a.who()}"
}

class TestA(private val b: TestB) {

    fun talk() = "I am A and have a ${b.who()}"
}

class TestB {

    fun who() = "B"
}

class TestExpensiveService {

    init {
        Thread.sleep(10)
        instanceCounter.incrementAndGet()
    }

    companion object {

        val instanceCounter = AtomicInteger(0)
    }
}

data class TestPerson(var name: String, var age: Int, val body: TestBody)

data class TestBody(var height: Int, var weight: Int)

data class TestFactoryPerson(val body: TestBody)

class TestFactoryBody
