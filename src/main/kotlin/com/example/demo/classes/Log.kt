package com.example.demo.classes

interface Log {
    fun annotated(a: String)
    fun multipleArgsAnnotated(a: String, b: Boolean, c: Int)
    fun notAnnotated(a: String)
}
