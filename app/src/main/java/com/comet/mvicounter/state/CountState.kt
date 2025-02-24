package com.comet.mvicounter.state

import com.comet.mvicounter.mvi.IState

// count를 바로 나타내도 되고, 그냥 sealed class로 선언해서 여러개 다른 상태로 나타내도 되고
data class CountState(val count : Int) : IState {

    // 최초 값 제공
    companion object {
        fun init() = CountState(0)
    }

}