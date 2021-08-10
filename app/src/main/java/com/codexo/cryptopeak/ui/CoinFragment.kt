package com.codexo.cryptopeak.ui

import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.databinding.FragmentCoinBinding
import com.codexo.cryptopeak.utils.NetworkUtil
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar


class CoinFragment : Fragment(R.layout.fragment_coin) {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding
    private val coinAdapter = CoinAdapter()

    private lateinit var networkUtil: NetworkUtil

    @RequiresApi(Build.VERSION_CODES.N)
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


        viewModel.coinData.observe(viewLifecycleOwner, {
            coinAdapter.submitList(it)
        })
//        viewModel.status.observe(requireActivity(), {
//            Snackbar.make(requireView(), it.toString(), Snackbar.LENGTH_LONG).show()
//        })

        checkInternet()
    }

    private fun checkInternet() {
        networkUtil = NetworkUtil(requireContext())

        networkUtil.observe(requireActivity(), {
            if (it) {
                Snackbar.make(requireView(), "Connected!", Snackbar.LENGTH_LONG)
                    .setTextColor(Color.GREEN)
                    .show()
            } else Snackbar.make(requireView(), "Disconnected!", Snackbar.LENGTH_LONG)
                .setTextColor(Color.RED)
                .show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}