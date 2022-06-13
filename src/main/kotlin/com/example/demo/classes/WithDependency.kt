package com.example.demo.classes

import com.example.demo.annotations.MyComponent
import com.example.demo.annotations.MyQualifier
import com.example.demo.factory.ProxyFactory.ANSI_CYAN
import com.example.demo.factory.ProxyFactory.ANSI_RESET

@MyComponent
class WithDependency(
    @MyQualifier("LoggableClass")
    private val loggable: Log
) : Dependency {

    init {
        println("${ANSI_CYAN}Instantiating WithDependency$ANSI_RESET\n")
    }

    override fun annotated(a: String): String {
        loggable.annotated(a)
        return "Executed with success"
    }
}