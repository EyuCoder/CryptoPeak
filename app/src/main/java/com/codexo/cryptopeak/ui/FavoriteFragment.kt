package com.codexo.cryptopeak.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.databinding.FragmentFavoriteBinding
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(database, repository)
    }
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var coinAdapter: CoinAdapter

    private lateinit var database: CoinDatabase
    private lateinit var repository: Repository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        coinAdapter = CoinAdapter(coinAdapterItemClickListener)
        database = CoinDatabase.getDatabase(requireContext().applicationContext)
        repository = Repository(database)

        binding.rvFavorite.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.favoriteCoins.observe(viewLifecycleOwner, {
            coinAdapter.submitList(it)
        })

        setHasOptionsMenu(true)
    }

    private val coinAdapterItemClickListener = object : CoinAdapter.OnItemClickListener {
        override fun onFavoriteClicked(markedFavorite: Boolean, id: String) {
            viewModel.markAsFavorite(markedFavorite, id)
        }

        override fun onOpenDetail(currentItem: CoinData) {
            val action =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(currentItem)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
