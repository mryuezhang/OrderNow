package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yue.ordernow.R
import com.yue.ordernow.list.MenuListAdapter
import com.yue.ordernow.models.MenuItem
import kotlinx.android.synthetic.main.fragment_menu.*

private const val ITEMS = "items"

class MenuFragment : Fragment() {

    var items = ArrayList<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            items = it.getParcelableArrayList(ITEMS)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuList.adapter = MenuListAdapter(items)
    }

    companion object {
        @JvmStatic
        fun newInstance(items: ArrayList<MenuItem>) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ITEMS, items)
                }
            }
    }
}