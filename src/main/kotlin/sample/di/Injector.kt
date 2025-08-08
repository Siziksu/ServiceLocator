package sample.di

import locator.get
import locator.inject
import locator.startLocator
import sample.data.repository.contract.StringRepositoryContract
import sample.domain.models.Person

internal fun initLocator() {
    startLocator(block1, block2, block3)

    val person: Person = get()
    with(person) {
        name = "John"
        age = 35
        body.height = 175
        body.weight = 80
    }

    val stringRepository: StringRepositoryContract by inject()
    stringRepository.set("name", "Siziksu")
}
