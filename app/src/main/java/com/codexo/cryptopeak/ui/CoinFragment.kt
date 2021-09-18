package com.codexo.cryptopeak.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.databinding.FragmentCoinBinding
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory

class CoinFragment : Fragment(R.layout.fragment_coin) {

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(database, repository)
    }
    private var _binding: FragmentCoinBinding? = null
    private val binding get() = _binding!!

    private lateinit var coinAdapter: CoinAdapter

    private lateinit var database: CoinDatabase
    private lateinit var repository: Repository

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCoinBinding.bind(view)

        coinAdapter = CoinAdapter(coinAdapterItemClickListener)
        database = CoinDatabase.getDatabase(requireContext().applicationContext)
        repository = Repository(database)

        binding.rvMain.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.coinData.observe(viewLifecycleOwner, { coinData ->
            coinAdapter.submitList(coinData)
        })
    }

    private val coinAdapterItemClickListener = object : CoinAdapter.OnItemClickListener {
        override fun onFavoriteClicked(markedFavorite: Boolean, id: String) {
            viewModel.markAsFavorite(markedFavorite, id)
        }

        override fun onOpenDetail(currentItem: CoinData) {
            val action = CoinFragmentDirections.actionCoinFragmentToDetailFragment(currentItem)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
