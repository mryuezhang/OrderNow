package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.yue.ordernow.R

class NoOrderFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_no_order, container, false)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_no_order, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
