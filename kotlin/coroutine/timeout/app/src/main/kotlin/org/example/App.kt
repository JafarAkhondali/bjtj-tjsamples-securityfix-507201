/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

import kotlinx.coroutines.*

class App {

    // WORKING (withTimeout)
    fun example1() = runBlocking {
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        // kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
    }

    // WORKING (withTimeoutOrNull)
    fun example2() = runBlocking {
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "Done" // will get cancelled before it produces this result
        }
        println("Result is $result") // OUT: Result is null
    }


    //  Asynchronous timeout and resources﻿


    var acquired = 0

    inner class Resource {
        init { acquired++ } // Acquire the resource
        fun close() { acquired-- } // Release the resource
    }

    // PROBLEM (not always print 0)
    fun example3() {
        acquired = 0
        runBlocking {
            repeat(10_000) { // Launch 10K coroutines
                launch { 
                    val resource = withTimeout(60) { // Timeout of 60 ms
                        delay(50) // Delay for 50 ms
                        Resource() // Acquire a resource and return it from withTimeout block     
                    }
                    resource.close() // Release the resource
                }
            }
        }
        // Outside of runBlocking all coroutines have completed
        println(acquired) // Print the number of resources still acquired
    }

    // SOLVED (always print 0)
    fun example4() {
        acquired = 0
        runBlocking {
            repeat(10_000) { // Launch 10K coroutines
                launch { 
                    var resource: Resource? = null // Not acquired yet
                    try {
                        withTimeout(60) { // Timeout of 60 ms
                            delay(50) // Delay for 50 ms
                            resource = Resource() // Store a resource to the variable if acquired      
                        }
                        // We can do something else with the resource here
                    } finally {  
                        resource?.close() // Release the resource if it was acquired
                    }
                }
            }
        }
        // Outside of runBlocking all coroutines have completed
        println(acquired) // Print the number of resources still acquired
    }
    
}

fun main() {
    println("======== EXAMPLE1 ========")
    try {
        App().example1()
    } catch (e: TimeoutCancellationException) {
        e.printStackTrace()
    }
    println("======== EXAMPLE2 ========")
    App().example2()
    println("======== EXAMPLE3 ========")
    App().example3()
    println("======== EXAMPLE4 ========")
    App().example4()
}
