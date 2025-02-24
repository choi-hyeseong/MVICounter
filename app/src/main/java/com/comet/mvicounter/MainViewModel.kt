package com.comet.mvicounter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.comet.mvicounter.mvi.MVIViewModel
import com.comet.mvicounter.state.CountState
import com.comet.mvicounter.state.CounterEffect
import com.comet.mvicounter.state.CounterIntent
import java.lang.IllegalArgumentException

class MainViewModel : MVIViewModel<CountState, CounterIntent, CounterEffect>(CountState.init()) {

    // 인텐트 핸들링
    override suspend fun handleIntent(intent: CounterIntent) {
        when (intent) {
            CounterIntent.ClickIntent -> handleClick()
            CounterIntent.ResetIntent -> reduce { copy(count = 0) }
        }
    }

    private suspend fun handleClick() {
        reduce { copy(count = count + 1) }
        // effect part
        if (currentState.count % 5 == 0)
            sendEffect(CounterEffect.NotifyToast("5의 배수 ${currentState.count} 돌파!"))
        if (currentState.count % 100 == 0)
            sendEffect(CounterEffect.NotifySnackBar("100의 배수 ${currentState.count} 돌파!"))
    }

}

class MainViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(MainViewModel::class.java))
            throw IllegalArgumentException("Unknown class")
        return MainViewModel() as T
    }
}