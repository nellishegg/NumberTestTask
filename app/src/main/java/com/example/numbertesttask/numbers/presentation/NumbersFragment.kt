package com.example.numbertesttask.numbers.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.numbertesttask.R
import com.example.numbertesttask.details.presentation.DetailsFragment
import com.example.numbertesttask.main.presentation.ShowFragment

class NumbersFragment : Fragment() {

    private var showFragment: ShowFragment = ShowFragment.Empty()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ShowFragment)
            showFragment = context as ShowFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        view.findViewById<View>(R.id.getFactButton).setOnClickListener {
            //todo refactor and remove hardcode
                showFragment.show(DetailsFragment.newInstance("some information about random number hardcoded"))

        }
    }

    override fun onDetach() {
        super.onDetach()
        showFragment = ShowFragment.Empty()
    }
}