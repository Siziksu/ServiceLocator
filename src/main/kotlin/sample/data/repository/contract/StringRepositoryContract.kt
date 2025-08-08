package sample.data.repository.contract

interface StringRepositoryContract {

    fun set(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String): String?
}
