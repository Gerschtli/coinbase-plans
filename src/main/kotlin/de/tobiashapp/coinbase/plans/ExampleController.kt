package de.tobiashapp.coinbase.plans

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController {

    @GetMapping("/")
    fun index(): String {
        return "index\n"
    }

}
