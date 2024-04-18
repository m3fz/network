package com.soc.network.core.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
@Hidden
class DefaultController {

    @GetMapping("/")
    fun default(): String {
        return "redirect:/swagger-ui.html"
    }
}