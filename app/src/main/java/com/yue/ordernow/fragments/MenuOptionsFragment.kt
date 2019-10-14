package com.yue.ordernow.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yue.ordernow.activities.MainActivity
import com.yue.ordernow.adapters.MenuItemAdapter
import com.yue.ordernow.databinding.FragmentMenuOptionsBinding
import com.yue.ordernow.utils.InjectorUtils
import com.yue.ordernow.viewModels.MenuOptionsViewModel


private const val CATEGORY = "category"

class MenuOptionsFragment : Fragment() {

    private val viewModel: MenuOptionsViewModel by viewModels {
        InjectorUtils.provideMenuOptionsViewModelFactory(
            requireContext(),
            arguments!!.getString(CATEGORY)!!
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMenuOptionsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        // Set adapter
        val adapter = MenuItemAdapter(activity as MainActivity)
        binding.menuOptions.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }


    private fun subscribeUi(adapter: MenuItemAdapter) {
        viewModel.menuItems.observe(viewLifecycleOwner, Observer {
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
