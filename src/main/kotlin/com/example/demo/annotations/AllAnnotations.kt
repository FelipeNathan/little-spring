package com.example.demo.annotations

annotation class Loggable

// Spring Copy
annotation class MyComponent
annotation class MyBefore
annotation class MyAfter
annotation class MyAround

annotation class MyQualifier(val beanName: String)