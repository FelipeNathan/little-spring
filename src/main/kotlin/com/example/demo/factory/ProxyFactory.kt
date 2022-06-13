package com.example.demo.factory

import com.example.demo.annotations.Loggable
import com.example.demo.annotations.MyComponent
import com.example.demo.annotations.MyQualifier
import com.example.demo.listeners.MyAspectListeners.executeAfter
import com.example.demo.listeners.MyAspectListeners.executeAround
import com.example.demo.listeners.MyAspectListeners.executeBefore
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import org.reflections.Reflections

object ProxyFactory {

    const val ANSI_RESET = "\u001B[0m"
    const val ANSI_GREEN = "\u001B[32m"
    const val ANSI_CYAN = "\u001B[36m"
    const val ANSI_RED = "\u001B[31m"
    const val ROOT_PACKAGE = "com.example.demo"

    inline fun <reified T> initialize() {
        Reflections(T::class.java.packageName)
            .getTypesAnnotatedWith(MyComponent::class.java)
            .forEach { getBean(it) }
    }

    inline fun <reified T : Any> getBean() = getBean(T::class.java)

    fun getBean(bean: Class<*>, qualifier: String = ""): Any {

        val clazz = findClassToCreate(bean, qualifier)

        return ProxyCache.getClassOrElse(clazz) {
            val constructor = clazz.declaredConstructors.first()
            val params = findInstanceParameters(constructor)

            println("${ANSI_GREEN}Creating a brand new ${clazz.simpleName}${ANSI_RESET}")
            val instance = constructor.newInstance(*params)
            createProxy(instance, clazz)
        }
    }

    /*
    * Busca a classe a ser criada a instância/proxy
    * Isto é, caso seja uma interface, busca uma implementação desta interface
    * Caso a interface contenha mais de 1 implementação, verifica o nome da classe com base no @MyQualifier
    * obs: validado apenas a classe é uma interface (não validado se é abstract também por exemplo),
    * visto que é apenas para estudo de reflection e "simulação" do spring
    * */
    fun findClassToCreate(bean: Class<*>, qualifier: String): Class<*> {

        if (!bean.isInterface) {
            return bean
        }

        return with(Reflections(ROOT_PACKAGE).getSubTypesOf(bean)) {
            first {
                size == 1 || it.simpleName == qualifier
            }
        }
    }

    /*
    * Pega o construtor da classe a ser instanciada, lista o parâmetros caso tenha e instancia
    * No caso de ter parâmetros, realiza um getBean() para instanciar (ou pegar do cache) a instância da classe do parametro
    * obs: não foi feito nada para parâmetros de tipos primitivos,
    * visto que é apenas para estudo de reflection e "simulação" do spring
    * */
    fun findInstanceParameters(constructor: Constructor<*>): Array<Any> {
        return constructor
            .parameters
            .map {
                if (it.isAnnotationPresent(MyQualifier::class.java)) {
                    val qualifier = it.getAnnotation(MyQualifier::class.java).beanName
                    getBean(it.type, qualifier)
                } else {
                    getBean(it.type)
                }
            }
            .toTypedArray()
    }

    fun createProxy(instance: Any, clazz: Class<*>): Any {
        return Proxy.newProxyInstance(
            clazz.classLoader,
            clazz.interfaces,
            createInvocationHandler(instance)
        )
    }

    /*
    * Cria o `handler` de invocação do proxy, responsável por gerenciar as chamadas dos métodos
    * Neste ponto podemos fazer quaisquer regras antes e depois da chamada do método real
    * */
    private fun <T> createInvocationHandler(instance: T) = InvocationHandler { _, method, args ->
        //Precisa coletar o retorno do método para propagar atrávez do handler
        //Sem isso, ignoraríamos o retorno real
        var retVal: Any? = null

        // Executa o @Before do @Aspect
        val methodInstancePath = getInstanceMethod(instance, method).toString()
        executeBefore(methodInstancePath)
        // O @Around do @Aspect faz um tipo de "wrap" do `method.invoke`
        executeAround(methodInstancePath) {
            print("Invoking method ${method.name}: ")
            retVal = method.invoke(instance, *args)
            println()
        }
        // Execute o @After do @Aspect
        executeAfter(methodInstancePath)

        if (checkAnnotationOnMethod(instance, method, Loggable::class.java)) {
            val mapArgs = args.joinToString("\n") { "type: ${it.javaClass.simpleName}, value: $it" }
            println("Method executed with args:\n$mapArgs")
        }

        println()
        retVal
    }

    private fun <T> checkAnnotationOnMethod(instance: T, method: Method, ann: Class<out Annotation>): Boolean {
        val instanceMethod = instance!!::class.java.getMethod(method.name, *method.parameterTypes)
        return instanceMethod.isAnnotationPresent(ann)
    }

    private fun <T> getInstanceMethod(instance: T, method: Method): Method {
        return instance!!::class.java.getMethod(method.name, *method.parameterTypes)
    }
}
