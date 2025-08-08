package sample.di

import locator.Locator
import sample.app.Presenter
import sample.app.PresenterContract
import sample.data.repository.AnotherRepository
import sample.data.repository.StringRepository
import sample.data.repository.contract.AnotherRepositoryContract
import sample.data.repository.contract.StringRepositoryContract
import sample.domain.UseCase
import sample.domain.contract.UseCaseContract
import sample.domain.models.Body
import sample.domain.models.Person

val block1: Locator.() -> Unit = {
    singleton<PresenterContract> { Presenter(get()) }
}
val block2: Locator.() -> Unit = {
    singleton<UseCaseContract> { UseCase(get(), get()) }
    singleton<AnotherRepositoryContract> { AnotherRepository() }
}
val block3: Locator.() -> Unit = {
    singleton { Person("Siziksu", 28, get()) }
    singleton<StringRepositoryContract> { StringRepository() }
    singleton { Body(180, 70) }
}
