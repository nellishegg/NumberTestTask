package com.example.numbertesttask.numbers.presentation

import android.widget.TextView

data class NumberUi(private val id: String, private val fact: String):Mapper<Boolean,NumberUi> {
//show details
    fun map(head: TextView, subTitle: TextView) {
        head.text = id
        subTitle.text = fact
    }

    override fun map(source: NumberUi): Boolean {
        return source.id ==id
    }

}