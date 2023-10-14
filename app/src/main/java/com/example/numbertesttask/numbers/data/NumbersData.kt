package com.example.numbertesttask.numbers.data

data class NumbersData(
    private val id: String,
    private val fact: String
) {

    interface Mapper<T> {
        fun map(id: String, fact: String): T
        class Matches(private val id: String) : Mapper<Boolean> {
            override fun map(id: String, fact: String): Boolean {
                return this.id == id
            }
        }
    }
    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, fact)

    fun matches(number: String) = number == id

}
