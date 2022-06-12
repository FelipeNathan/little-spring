package com.example.demo.listeners

class AroundJoinpoint(
    private val aspects: List<MyAspect>,
    private val block: () -> Unit) {

    private var index = 0
    private val length = aspects.size

    fun proceed() {
        if (length == 0)
            return

        if (index == length) {
            block()
            return
        }

        aspects[index++].execute(this)
    }
}