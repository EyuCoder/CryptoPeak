package com.codexo.cryptopeak.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.databinding.FragmentFavoriteBinding
import com.codexo.cryptopeak.repository.Repository

class FavoriteFragment : Fragment(R.layout.fragment_favorite), CoinAdapter.OnItemClickListener {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding
        get() = _binding
    private val coinAdapter = CoinAdapter(this)
    private lateinit var database: CoinDatabase
    private lateinit var repository: Repository
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)
        database = CoinDatabase.getDatabase(requireContext().applicationContext)
        repository = Repository(database)

        val viewModelFactory = MainViewModelFactory(database, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding?.apply {
            rvFavorite.apply {
                adapter = coinAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

        }


        viewModel.favoriteCoin.observe(requireActivity(), {
            coinAdapter.submitList(it)
        })

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFavoriteClicked(markedFavorite: Boolean, id: String) {
        viewModel.markAsFavorite(markedFavorite, id)
    }
}