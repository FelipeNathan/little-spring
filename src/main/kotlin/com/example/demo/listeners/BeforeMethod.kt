package com.example.demo.listeners

import com.example.demo.annotations.MyBefore
import com.example.demo.factory.ProxyFactory.ANSI_RED
import com.example.demo.factory.ProxyFactory.ANSI_RESET

@MyBefore
class BeforeMethod : MyAspect() {
    override fun execute() = println("${ANSI_RED}Executing the @MyBefore Method$ANSI_RESET")
}