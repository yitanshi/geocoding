package io.patamon.geocoding

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author  yunpeng.gu
 * @date  2021/6/4 15:21
 * @Email:yunpeng.gu@percent.cn
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
open class KotlinDemoApplication : CommandLineRunner {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(KotlinDemoApplication::class.java, *args)
        }
    }

    override fun run(vararg args: String?) {
        println("*************************** ok ***********************************")
    }
}
