package com.example.demo.factory

object ProxyCache {

    private val cacheInstances = mutableMapOf<String, Any>()

    fun getClassOrElse(clazz: Class<*>, block: () -> Any): Any {
        return if (cacheInstances.containsKey(clazz.simpleName)) {
            println("${ProxyFactory.ANSI_GREEN}Returning the cached ${clazz.simpleName}${ProxyFactory.ANSI_RESET}")
            cacheInstances[clazz.simpleName] as Any
        } else block().also { cacheInstances[clazz.simpleName] = it }
    }
}