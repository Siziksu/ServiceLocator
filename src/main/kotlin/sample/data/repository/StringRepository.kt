package sample.data.repository

import sample.data.repository.contract.StringRepositoryContract

class StringRepository : StringRepositoryContract {

    private val hash = HashMap<String, String>()

    override fun set(key: String, value: String) {
        hash[key] = value
    }

    override fun get(key: String): String? = hash[key]

    override fun remove(key: String): String? = hash.remove(key)
}
