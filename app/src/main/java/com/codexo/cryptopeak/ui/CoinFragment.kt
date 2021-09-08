package com.codexo.cryptopeak.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.databinding.FragmentCoinBinding
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.utils.NetworkUtil
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class CoinFragment : Fragment(R.layout.fragment_coin), CoinAdapter.OnItemClickListener {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentCoinBinding? = null
    private val binding
        get() = _binding
    private val coinAdapter = CoinAdapter(this)

    private lateinit var database: CoinDatabase
    private lateinit var repository: Repository

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

        database = CoinDatabase.getDatabase(requireContext().applicationContext)
        repository = Repository(database)


        val viewModelFactory = MainViewModelFactory(database, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        viewModel.coinData.observe(viewLifecycleOwner, {
            coinAdapter.submitList(it)
        })
        viewModel.dataCount.observe(requireActivity(), {
            viewModel.count = it ?:0
        })

        //checkInternet()
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

    override fun onFavoriteClicked(markedFavorite: Boolean, id: String) {
        viewModel.markAsFavorite(markedFavorite, id)
    }

    override fun onOpenDetail(currentItem: CoinData) {
        val action = CoinFragmentDirections.actionCoinFragmentToDetailFragment(currentItem)
        findNavController().navigate(action)
    }
}