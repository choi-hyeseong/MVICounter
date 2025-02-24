package com.comet.mvicounter.state

import com.comet.mvicounter.mvi.IIntent

sealed class CounterIntent : IIntent {
    object ClickIntent : CounterIntent()
    object ResetIntent : CounterIntent()
}