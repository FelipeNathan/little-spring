package com.example.demo.listeners

import com.example.demo.annotations.MyAround
import com.example.demo.factory.ProxyFactory.ANSI_CYAN
import com.example.demo.factory.ProxyFactory.ANSI_RED
import com.example.demo.factory.ProxyFactory.ANSI_RESET

@MyAround
class SecondAroundMethod : MyAspect() {

    override fun execute(joinpoint: AroundJoinpoint) {
        println("${ANSI_RED}Executing another (before) @Around$ANSI_RESET")
        val someRule = true
        if (someRule)
            joinpoint.proceed()
        println("${ANSI_RED}Executing another (after) @Around$ANSI_RESET")
        println("""${ANSI_CYAN}This is "executing AROUND" the method$ANSI_RESET""")
    }
}