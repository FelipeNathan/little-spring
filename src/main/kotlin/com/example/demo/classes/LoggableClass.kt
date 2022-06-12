package com.example.demo.classes

import com.example.demo.annotations.Loggable
import com.example.demo.annotations.MyComponent
import com.example.demo.factory.ProxyFactory.ANSI_CYAN
import com.example.demo.factory.ProxyFactory.ANSI_RESET

@MyComponent
class LoggableClass : Log {
    init {
        println("${ANSI_CYAN}Instantiating LoggableClass$ANSI_RESET\n")
    }

    @Loggable
    override fun annotated(a: String) {
        // Loggable method
    }

    @Loggable
    override fun multipleArgsAnnotated(a: String, b: Boolean, c: Int) {
        // Loggable method
    }

    override fun notAnnotated(a: String) {
        // Log annotated
    }
}
