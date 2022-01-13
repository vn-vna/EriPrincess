package vn.vna.erivampir.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

@RestController
public class Mapping {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        StringBuilder str = new StringBuilder();
        try {
            var pageData       = Mapping.class.getResourceAsStream("/static/index.html");
            var bufferedReader = new BufferedReader(new InputStreamReader(pageData));

            String line;
            while (!Objects.isNull(line = bufferedReader.readLine())) {
                str.append(line);
            }

        } catch (IOException | NullPointerException ioex) {
            ioex.printStackTrace();
        }
        return str.toString();
    }
}
