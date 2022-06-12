package com.example.demo

import com.example.demo.classes.Dependency
import com.example.demo.classes.Log
import com.example.demo.classes.LoggableClass
import com.example.demo.classes.NotLoggableClass
import com.example.demo.classes.WithDependency
import com.example.demo.factory.ProxyFactory

class DemoApplication

fun main() {
    ProxyFactory.initialize<DemoApplication>()

    val withDependency = ProxyFactory.getBean<WithDependency>() as Dependency
    val response = withDependency.annotated("Executed from WithDependency")

    repeat(5) { print("@") }
    println(response)
//    val loggableClass = ProxyFactory.getBean<LoggableClass>() as Log
//    loggableClass.annotated("Executed from LoggableClass")
//    loggableClass.multipleArgsAnnotated("mylittlestring", true, 30)
//
//    val notLoggableClass = ProxyFactory.getBean<NotLoggableClass>() as Log
//    notLoggableClass.annotated("Executed from NotLoggableClass")
//    notLoggableClass.notAnnotated("Executed from NotLoggableClass")
}
