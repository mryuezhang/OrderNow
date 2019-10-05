package com.yue.ordernow.fragments


import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

//        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            menu_options.layoutManager = SpanningGridLayoutManager(context!!, 2)
//        } else {
//            menu_options.layoutManager = SpanningGridLayoutManager(context!!, 4)
//        }

        menu_options.layoutManager = SpanningGridLayoutManager(context!!, 2)

        menu_options.adapter = MenuListAdapter(activity!!, items)
    }

    private inner class SpanningGridLayoutManager(context: Context, spanCount: Int) :
        GridLayoutManager(context, spanCount) {

        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return adjustWidth(super.generateDefaultLayoutParams())
        }

        override fun generateLayoutParams(
            c: Context,
            attrs: AttributeSet
        ): RecyclerView.LayoutParams {
            return adjustWidth(super.generateLayoutParams(c, attrs))
        }

        override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
            return adjustWidth(super.generateLayoutParams(lp))
        }

        private fun adjustWidth(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
            layoutParams.width = (width - paddingRight - paddingLeft) / spanCount
            return layoutParams
        }
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
