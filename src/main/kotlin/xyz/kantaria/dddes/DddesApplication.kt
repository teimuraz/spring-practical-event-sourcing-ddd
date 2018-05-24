package xyz.kantaria.dddes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DddesApplication

fun main(args: Array<String>) {
    runApplication<DddesApplication>(*args)
}
