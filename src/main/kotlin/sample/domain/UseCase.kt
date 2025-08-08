package sample.domain

import sample.data.repository.contract.AnotherRepositoryContract
import sample.data.repository.contract.StringRepositoryContract
import sample.domain.contract.UseCaseContract

class UseCase(
    private val stringRepository: StringRepositoryContract,
    private val anotherRepository: AnotherRepositoryContract
) : UseCaseContract {

    override fun start(): String = "Repository 1: ${stringRepository.get("name")}\nRepository 2: ${anotherRepository.function()}"
}
