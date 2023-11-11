package com.example.numbertesttask.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.example.numbertesttask.R
import com.example.numbertesttask.main.presentation.sl.ProvideViewModel
import com.example.numbertesttask.numbers.presentation.NumbersFragment
import com.example.numbertesttask.numbers.sl.NumbersModule

class MainActivity : AppCompatActivity(), ShowFragment, ProvideViewModel {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            show(NumbersFragment(), false)

    }
    override fun show(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
        show(fragment, true)
    }

    private fun show(fragment: Fragment, add: Boolean) {
        //todo make OOP
        val transaction = supportFragmentManager.beginTransaction()
        val container = R.id.container
        if (add)
            transaction.add(container, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
        else
            transaction.replace(R.id.container, NumbersFragment())
        transaction.commit()
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T =
        (application as ProvideViewModel).provideViewModel(clazz, owner)
}

interface ShowFragment {

    fun show(fragment: Fragment)
    class Empty : ShowFragment {
        override fun show(fragment: Fragment) = Unit

    }

}