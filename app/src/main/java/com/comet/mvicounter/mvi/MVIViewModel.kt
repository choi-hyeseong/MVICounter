package com.comet.mvicounter.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MVIViewModel<S : IState, I : IIntent, E : IEffect>(initial: S) : ViewModel() {

    init {
        // init시 collect 시작
        viewModelScope.launch {
            collect()
        }
    }

    // 뷰가 처리할 상태
    private val _state: MutableStateFlow<S> = MutableStateFlow(initial)
    val viewState: StateFlow<S>
        get() = _state.asStateFlow()

    protected val currentState : S
        get() = _state.value

    // vm에서 핸들링할 인텐트
    private val intent: MutableSharedFlow<I> = MutableSharedFlow()

    // 뷰에서 처리될 이펙트
    private val _effect: Channel<E> = Channel()
    val sideEffect: Flow<E>
        get() = _effect.receiveAsFlow()

    // vm에서 이펙트 전달
    protected suspend fun sendEffect(effect: E) {
        _effect.send(effect)
    }

    // 뷰에서 인텐트 전달
    fun sendIntent(viewIntent : I)  {
        viewModelScope.launch {
            intent.emit(viewIntent)
        }
    }

    // 해당 VM이 처리할 Intent
    protected abstract suspend fun handleIntent(intent: I)

    private fun collect() {
        intent.onEach {
            handleIntent(it)
        }.launchIn(viewModelScope)
    }

    // state 값 변경. state는 불변
    protected fun reduce(reducer : S.() -> S) {
        val result = currentState.reducer()
        _state.value = result
    }
}

interface IState

interface IIntent

interface IEffect