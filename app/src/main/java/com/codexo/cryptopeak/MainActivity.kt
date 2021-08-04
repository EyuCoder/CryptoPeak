package com.codexo.cryptopeak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.codexo.cryptopeak.repository.Repository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val tvLog: TextView by lazy { findViewById(R.id.tv_log) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.response.observe(this, {
            tvLog.text = it.toString()
        })

    }
}