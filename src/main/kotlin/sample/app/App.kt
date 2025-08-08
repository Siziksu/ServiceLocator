package sample.app

import locator.get
import locator.inject
import sample.domain.models.Person

class App {

    private val presenter: PresenterContract by inject()
    private val person: Person by lazy { get() }

    fun start() {
        println(presenter.start())
        println(person)
    }
}
