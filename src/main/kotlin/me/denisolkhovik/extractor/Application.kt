package me.denisolkhovik.extractor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoviesExtractorTelegramBotApplication

fun main(args: Array<String>) {
    runApplication<MoviesExtractorTelegramBotApplication>(*args)
}