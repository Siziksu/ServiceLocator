package sample.domain.models

data class Person(var name: String, var age: Int, val body: Body)

data class Body(var height: Int, var weight: Int)
