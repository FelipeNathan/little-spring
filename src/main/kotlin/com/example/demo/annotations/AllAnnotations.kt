package com.example.demo.annotations

annotation class Loggable

// Spring Copy
annotation class MyComponent

annotation class MyBefore(val component: String = "")
annotation class MyAfter(val component: String = "")
annotation class MyAround(val component: String = "")

annotation class MyQualifier(val beanName: String)