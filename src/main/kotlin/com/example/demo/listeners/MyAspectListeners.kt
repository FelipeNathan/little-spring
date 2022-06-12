package com.example.demo.listeners

import com.example.demo.annotations.MyAfter
import com.example.demo.annotations.MyAround
import com.example.demo.annotations.MyBefore
import com.example.demo.factory.ProxyFactory
import org.reflections.Reflections

object MyAspectListeners {

    private val beforeListeners =
        Reflections(ProxyFactory.ROOT_PACKAGE)
            .getTypesAnnotatedWith(MyBefore::class.java)
            .map { it.constructors.first().newInstance() as MyAspect }

    private val afterListeners =
        Reflections(ProxyFactory.ROOT_PACKAGE)
            .getTypesAnnotatedWith(MyAfter::class.java)
            .map { it.constructors.first().newInstance() as MyAspect }

    private val aroundListeners =
        Reflections(ProxyFactory.ROOT_PACKAGE)
            .getTypesAnnotatedWith(MyAround::class.java)
            .map { it.constructors.first().newInstance() as MyAspect }

    fun executeBefore() {
        beforeListeners.forEach {
            it.execute()
        }
    }

    fun executeAfter() {
        afterListeners.forEach {
            it.execute()
        }
    }

    fun executeAround(block: () -> Unit) {
        AroundJoinpoint(aroundListeners, block).proceed()
    }
}
