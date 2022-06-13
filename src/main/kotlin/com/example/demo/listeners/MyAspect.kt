package com.example.demo.listeners

abstract class MyAspect {
    open fun execute() {}
    open fun execute(joinPoint: AroundJoinpoint) {}
}