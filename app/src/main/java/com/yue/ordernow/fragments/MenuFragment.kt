package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yue.ordernow.R
import com.yue.ordernow.list.MenuListAdapter
import com.yue.ordernow.models.MenuItem
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment(private val items: ArrayList<MenuItem>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuList.layoutManager = LinearLayoutManager(activity)
        menuList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        menuList.adapter = MenuListAdapter(items)
    }
}