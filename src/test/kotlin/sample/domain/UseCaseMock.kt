package sample.domain

import sample.domain.contract.UseCaseContract

class UseCaseMock : UseCaseContract {

    override fun start(): String = "Raquel"
}
