package kr.ac.cnu.web.controller;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    final DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
    @RequestMapping(value = "/")
    String blackjackMain(){
        return "index";
        //return defaultResourceLoader.getResource("file:resources/static/blackjack/index.html");
    }
}
