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

    fun executeBefore(methodPath: String) {
        val listeners = beforeListeners filterBeforeBy methodPath
        listeners.forEach { it.execute() }
    }

    fun executeAfter(methodPath: String) {
        val listeners = afterListeners filterAfterBy methodPath
        listeners.forEach { it.execute() }
    }

    fun executeAround(methodPath: String, block: () -> Unit) {

        val listeners = aroundListeners filterAroundBy methodPath
        listeners.takeIf { it.isNotEmpty() }
            ?.run {
                AroundJoinpoint(aroundListeners, block).proceed()
            } ?: block()
    }

    private fun getListenerByComponent(methodPath: String, listeners: List<MyAspect>): List<MyAspect>? {
        return listeners.filter { aspect ->
            aspect.javaClass.getAnnotation(MyAround::class.java).component filterBy methodPath
        }.takeIf { it.isNotEmpty() }
    }

    infix private fun List<MyAspect>.filterBeforeBy(methodPath: String): List<MyAspect> {
        return this.filter {
            it.javaClass.getAnnotation(MyBefore::class.java).component filterBy methodPath
        }
    }

    infix private fun List<MyAspect>.filterAfterBy(methodPath: String): List<MyAspect> {
        return this.filter {
            it.javaClass.getAnnotation(MyAfter::class.java).component filterBy methodPath
        }
    }

    infix private fun List<MyAspect>.filterAroundBy(methodPath: String): List<MyAspect> {
        return this.filter {
            it.javaClass.getAnnotation(MyAround::class.java).component filterBy methodPath
        }
    }

    infix private fun String.filterBy(methodPath: String): Boolean {
        return if (this.isEmpty()) {
            true
        } else {
            this.toRegex().matches(methodPath)
        }
    }
}
