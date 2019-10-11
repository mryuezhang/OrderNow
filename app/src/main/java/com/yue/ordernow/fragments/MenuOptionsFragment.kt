package com.yue.ordernow.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yue.ordernow.databinding.FragmentMenuOptionsBinding
import com.yue.ordernow.list.MenuItemAdapter
import com.yue.ordernow.utils.InjectorUtils
import com.yue.ordernow.viewModels.MenuOptionsViewModel


private const val CATEGORY = "category"

class MenuOptionsFragment : Fragment() {

    private lateinit var viewModel: MenuOptionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val category = it.getString(CATEGORY)!!
            val vm: MenuOptionsViewModel by viewModels {
                InjectorUtils.provideMenuOptionsViewModelFactory(requireContext(), category)
            }

            viewModel = vm
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMenuOptionsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = MenuItemAdapter(activity!!)
        binding.menuOptions.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: MenuItemAdapter) {
        viewModel.menuItems.observe(viewLifecycleOwner, Observer {
            Log.i("FUCK", it.toString())
            adapter.submitList(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(string: String) =
            MenuOptionsFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY, string)
                }
            }
    }
}
