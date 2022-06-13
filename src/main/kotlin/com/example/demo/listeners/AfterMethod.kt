package com.example.demo.listeners

import com.example.demo.annotations.MyAfter
import com.example.demo.factory.ProxyFactory.ANSI_RED
import com.example.demo.factory.ProxyFactory.ANSI_RESET

@MyAfter(".*(Not)?LoggableClass.*")
class AfterMethod : MyAspect() {
    override fun execute() = println("${ANSI_RED}Executing the @MyAfter Method$ANSI_RESET")
}