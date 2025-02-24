package com.comet.mvicounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.comet.mvicounter.databinding.ActivityMainBinding
import com.comet.mvicounter.state.CounterEffect
import com.comet.mvicounter.state.CounterIntent
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    val viewModel by lazy {
        val factory = MainViewModelFactory()
        val provider = ViewModelProvider(this, factory)
        provider[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            viewModel.viewState.onEach {
                bind.number.text = it.count.toString()
            }.collect()
        }

        lifecycleScope.launch {
            // onEach하고 collect하거나 collect {} 하거나
            viewModel.sideEffect.onEach {
                // 처리할게 많은경우 fun으로 분리
                when (it) {
                    is CounterEffect.NotifySnackBar -> Snackbar.make(bind.root, it.message, Snackbar.LENGTH_SHORT).show()
                    is CounterEffect.NotifyToast -> Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }.collect()
        }

        // 카운트 인텐트 전송
        bind.count.setOnClickListener {
            viewModel.sendIntent(CounterIntent.ClickIntent)
        }

        // 리셋 인텐트 전송
        bind.reset.setOnClickListener {
            viewModel.sendIntent(CounterIntent.ResetIntent)
        }

        setContentView(bind.root)
    }
}