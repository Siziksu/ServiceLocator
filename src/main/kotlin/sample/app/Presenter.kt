package sample.app

import sample.domain.contract.UseCaseContract

class Presenter(private val useCase: UseCaseContract) : PresenterContract {

    override fun start(): String {
        return useCase.start()
    }
}
