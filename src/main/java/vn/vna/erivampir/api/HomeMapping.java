package vn.vna.erivampir.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.erivampir.db.ericfg.EriCfgRepoI;
import vn.vna.erivampir.db.ericfg.EriConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Objects;

@RestController
public class HomeMapping {

    Logger logger = LoggerFactory.getLogger(HomeMapping.class);

    @Autowired
    private EriCfgRepoI eriCfgRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        StringBuilder str = new StringBuilder();
        try {
            InputStream    pageData       = HomeMapping.class.getResourceAsStream("/static/index.html");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageData));

            String line;
            while (!Objects.isNull(line = bufferedReader.readLine())) {
                str.append(line);
            }

            EriConfiguration configuration = eriCfgRepository.insert(new EriConfiguration("KEY1_", "VALUE_", new Date()));
        } catch (IOException | NullPointerException | DuplicateKeyException ex) {
            logger.error(ex.getMessage());
        }

        return str.toString();
    }
}
