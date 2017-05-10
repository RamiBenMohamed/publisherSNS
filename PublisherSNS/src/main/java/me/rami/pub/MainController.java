package me.rami.pub;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {
         private static final String message = "Hello";
	@Autowired
         Publisher ms;
        @RequestMapping("/publish")
        public Publisher  write(@RequestParam(value="name", defaultValue="World") String name) {
                ms.publier(String.format(message,name));
                return ms;
                
        }
}