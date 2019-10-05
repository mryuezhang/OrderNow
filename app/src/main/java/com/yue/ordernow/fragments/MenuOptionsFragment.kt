package com.yue.ordernow.fragments


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yue.ordernow.R
import com.yue.ordernow.list.MenuListAdapter
import com.yue.ordernow.models.MenuItem
import kotlinx.android.synthetic.main.fragment_menu_options.*


private const val MENU_ITEMS = "menu_items"

class MenuOptionsFragment : Fragment() {

    var items = ArrayList<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            items = it.getParcelableArrayList(MENU_ITEMS)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_menu_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            menu_options.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else {
            menu_options.layoutManager =
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        }

        menu_options.adapter = MenuListAdapter(activity!!, items)
    }

    companion object {
        @JvmStatic
        fun newInstance(items: ArrayList<MenuItem>) =
            MenuOptionsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(MENU_ITEMS, items)
                }
            }
    }


}
