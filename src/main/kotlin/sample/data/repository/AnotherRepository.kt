package sample.data.repository

import sample.data.repository.contract.AnotherRepositoryContract

class AnotherRepository : AnotherRepositoryContract {

    override fun function(): String = "Another repository"
}
