package com.example.demo.classes

import com.example.demo.annotations.MyComponent
import com.example.demo.factory.ProxyFactory

@MyComponent
class NotLoggableClass : Log {
    init {
        println("${ProxyFactory.ANSI_CYAN}Instantiating NotLoggableClass${ProxyFactory.ANSI_RESET}\n")
    }

    override fun annotated(a: String) {
        // Not Loggable method
    }

    override fun multipleArgsAnnotated(a: String, b: Boolean, c: Int) {
        // Loggable method
    }

    override fun notAnnotated(a: String) {
        // Not Annotated
    }
}
