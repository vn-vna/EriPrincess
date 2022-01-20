package vn.vna.erivampir.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@RestController
public class HomeMapping {

    Logger logger = LoggerFactory.getLogger(HomeMapping.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        StringBuilder str = new StringBuilder();
        try {
            InputStream    pageData       = HomeMapping.class.getResourceAsStream("/static/index.html");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(pageData)));

            String line;
            while (!Objects.isNull(line = bufferedReader.readLine())) {
                str.append(line);
            }
        } catch (IOException | NullPointerException | DuplicateKeyException ex) {
            logger.error(ex.getMessage());
        }

        return str.toString();
    }
}
