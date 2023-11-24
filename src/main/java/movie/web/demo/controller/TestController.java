package movie.web.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return  "hi user";
    }

    @GetMapping("/manager")
    public String test2() {
        return  "hi manager ";
    }

}
