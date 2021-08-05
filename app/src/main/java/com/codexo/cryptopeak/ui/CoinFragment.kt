package com.codexo.cryptopeak.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.MainViewModel
import com.codexo.cryptopeak.MainViewModelFactory
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.databinding.FragmentCoinBinding

class CoinFragment : Fragment(R.layout.fragment_coin){
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding
    private val coinAdapter = CoinAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCoinBinding.bind(view)
        binding?.apply {
            rvMain.apply {
                adapter = coinAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        viewModel.response.observe(requireActivity(), {
            coinAdapter.submitList(it)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}