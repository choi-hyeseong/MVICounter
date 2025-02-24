package com.comet.mvicounter.state

import com.comet.mvicounter.mvi.IEffect

sealed class CounterEffect : IEffect {
    data class NotifyToast(val message : String) : CounterEffect()
    data class NotifySnackBar(val message : String) : CounterEffect()
}