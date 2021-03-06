package com.codexo.cryptopeak.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.adapters.BindingAdapter
import com.codexo.cryptopeak.adapters.CoinDetailAdapter
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.data.database.CoinHistory
import com.codexo.cryptopeak.databinding.FragmentDetailBinding
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.utils.NetworkStatus
import com.codexo.cryptopeak.utils.TimeInterval
import com.codexo.cryptopeak.viewmodels.DetailViewModel
import com.codexo.cryptopeak.viewmodels.DetailViewModel.DetailViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var adapter: CoinDetailAdapter
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var viewModel: DetailViewModel
    private var binding: FragmentDetailBinding? = null

    private lateinit var database: CoinDatabase
    private lateinit var repository: Repository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        database = CoinDatabase.getDatabase(requireContext().applicationContext)
        repository = Repository(database)


        val viewModelFactory = DetailViewModelFactory(database, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        refreshHistory()

        initUI()
        eventListeners()
        setHasOptionsMenu(true)
    }

    private fun eventListeners() {
        binding!!.sparkviewHistory.apply {
            isScrubEnabled = true
            setScrubListener { item ->
                if (item is CoinHistory) {
                    updateInfoForDate(item)
                }
            }
        }

        binding?.rgTimeIntervals?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_week -> updateTimeInterval(TimeInterval.WEEK)
                R.id.rb_month -> updateTimeInterval(TimeInterval.MONTH)
                R.id.rb_year -> updateTimeInterval(TimeInterval.YEAR)
                R.id.rb_all -> updateTimeInterval(TimeInterval.ALL)
            }
        }
    }

    private fun updateTimeInterval(interval: TimeInterval) {
        //reloadLineChart()
        adapter.timeInterval = interval
        adapter.notifyDataSetChanged()
    }

    private fun initUI() {

        viewModel.coinHistory.observe(viewLifecycleOwner, { coinHistory ->
            adapter = CoinDetailAdapter(coinHistory.reversed())
            binding?.sparkviewHistory?.adapter = adapter
            updateInfoForDate(coinHistory.first())
        })

        viewModel.coinDetail.observe(viewLifecycleOwner, { coinDetail ->

            binding?.apply {
                tvCoinName.text = coinDetail.nameFormatted
                tvRank.text = coinDetail.rankFormatted
                tvMarketCap.text = coinDetail.marketCapUsdFormatted
                tvVwap24hr.text = coinDetail.vwap24HrFormatted
                tvSupply.text = coinDetail.supplyFormatted
                tvVolume24hr.text = coinDetail.volumeUsd24HrFormatted
                tvChange24hr.text = coinDetail.changePercent24HrFormatted
                tvChartPrice.text = coinDetail.priceUsdFormatted
                BindingAdapter.bindImage(ivLogo, coinDetail.symbol)
            }
        })

        viewModel.status.observe(viewLifecycleOwner, { status ->
            when (status!!) {
                NetworkStatus.LOADING -> binding!!.pbHistoryLoading.visibility = View.VISIBLE
                NetworkStatus.DONE -> {
                    binding!!.pbHistoryLoading.visibility = View.GONE
                    radioButtonsEnable(true)
                }
                NetworkStatus.ERROR -> {
                    sparkLoadError()
                }
            }
        })
        binding!!.ibtnRetry.setOnClickListener { refreshHistory() }
    }

    private fun refreshHistory() {
        binding!!.pbHistoryLoading.visibility = View.VISIBLE
        binding!!.ibtnRetry.visibility = View.GONE
        radioButtonsEnable(false)
        viewModel.getCoinDetails(args.selectedItem.id)
    }

    private fun radioButtonsEnable(enabled: Boolean) {
        if (enabled) {
            binding!!.apply {
                rbAll.isEnabled = true
                rbYear.isEnabled = true
                rbMonth.isEnabled = true
                rbWeek.isEnabled = true
            }
        } else {
            binding!!.apply {
                rbAll.isEnabled = false
                rbYear.isEnabled = false
                rbMonth.isEnabled = false
                rbWeek.isEnabled = false
            }
        }
    }

    private fun sparkLoadError() {
        binding!!.pbHistoryLoading.visibility = View.GONE
        binding!!.ibtnRetry.visibility = View.VISIBLE
    }

    private fun updateInfoForDate(item: CoinHistory) {
        binding?.apply {
            tvChartPrice.text = item.priceFormatted
            tvChartDate.text = item.dateFormatted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
        }
        return true
    }
}