package com.example.numbertesttask.numbers.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

//обертка LiveData
interface Communication<T> {

    interface Observe<T> {
        fun observe(owner: LifecycleOwner, observe: Observer<T>)
    }

    interface Mutate<T> : Mapper.Unit<T>

    // закладываю значения и observe
    interface Mutable<T> : Observe<T>, Mutate<T>

    abstract class Abstract<T>(
        protected val liveData: MutableLiveData<T> = MutableLiveData()
    ) : Mutable<T> {
        override fun observe(owner: LifecycleOwner, observe: Observer<T>) =
            liveData.observe(owner, observe)
    }
//установка нового значения в LD в главном(UI) потоке/setting a new value to LiveData in main stream
    abstract class Ui<T>(
        liveData: MutableLiveData<T> = MutableLiveData()
    ) : Abstract<T>(liveData) {

        override fun map(source: T) {
            liveData.value = source
        }
    }
//setting a new value asynchronously to LD in side Stream
    abstract class Post<T>(
        liveData: MutableLiveData<T> = MutableLiveData()
    ) : Abstract<T>(liveData) {

        override fun map(source: T) {
            liveData.postValue(source)
        }
    }
}
