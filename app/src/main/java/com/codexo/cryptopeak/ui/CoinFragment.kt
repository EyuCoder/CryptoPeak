package com.codexo.cryptopeak.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.databinding.FragmentCoinBinding
import com.codexo.cryptopeak.workers.LiveUpdateWork.Companion.Progress
import com.codexo.cryptopeak.workers.RefreshDataWork
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class CoinFragment : Fragment(R.layout.fragment_coin) {
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


        viewModel.coinData.observe(requireActivity(), {
            coinAdapter.submitList(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}