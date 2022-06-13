package com.example.demo.listeners

import com.example.demo.annotations.MyAround
import com.example.demo.factory.ProxyFactory.ANSI_CYAN
import com.example.demo.factory.ProxyFactory.ANSI_RED
import com.example.demo.factory.ProxyFactory.ANSI_RESET

@MyAround
open class AroundMethod : MyAspect() {

    override fun execute(joinPoint: AroundJoinpoint) {
        println("${ANSI_RED}Executing some rule before the method execute on @Around$ANSI_RESET")
        val someRule = true
        if (someRule)
            joinPoint.proceed()
        println("${ANSI_RED}Executing some rule after the method execute on @Around$ANSI_RESET")
        println("""${ANSI_CYAN}This is "executing AROUND" the method$ANSI_RESET""")
    }
}