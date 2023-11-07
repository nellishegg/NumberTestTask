package com.example.numbertesttask.numbers.presentation

import android.widget.TextView

data class NumberUi(private val id: String, private val fact: String):Mapper<Boolean,NumberUi> {

    //todo 1 more method (concat with n/n/), then mapper interface
    fun map(head: TextView, subTitle: TextView) {
        head.text = id
        subTitle.text = fact
    }

    override fun map(source: NumberUi): Boolean {
        return source.id ==id
    }

}