package vn.vna.erivampir.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeMapping {

    public static Logger logger = LoggerFactory.getLogger(HomeMapping.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        return "Hello and welcome";
    }
}
