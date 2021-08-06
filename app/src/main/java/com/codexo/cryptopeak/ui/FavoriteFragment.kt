package com.codexo.cryptopeak.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codexo.cryptopeak.MainViewModel
import com.codexo.cryptopeak.MainViewModelFactory
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.CoinAdapter
import com.codexo.cryptopeak.database.CoinData
import com.codexo.cryptopeak.databinding.FragmentCoinBinding
import com.codexo.cryptopeak.databinding.FragmentDetailBinding
import com.codexo.cryptopeak.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment(R.layout.fragment_favorite){
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding
        get() = _binding
    private val coinAdapter = CoinAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)



        viewModel.response.observe(requireActivity(), {
            //todo()
        })

        setHasOptionsMenu(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}