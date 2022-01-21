package vn.vna.erivampir.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.erivampir.db.h2.ericfg.EriCfgRepository;
import vn.vna.erivampir.db.h2.ericfg.EriConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;

@RestController
public class HomeMapping {

    Logger logger = LoggerFactory.getLogger(HomeMapping.class);

    @Autowired
    EriCfgRepository eriCfgRepositoryH2;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        StringBuilder str = new StringBuilder();

        try {
            InputStream pageData =
                HomeMapping.class.getResourceAsStream("/static/index.html");
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(pageData)));

            String line;
            while (!Objects.isNull(line = bufferedReader.readLine())) {
                str.append(line);
            }

        } catch (IOException | NullPointerException | DuplicateKeyException ex) {
            logger.error(ex.getMessage());
        }

        EriConfig ex = new EriConfig();
        ex.setKey("ERI_VERSION");
        Optional<EriConfig> findResult = eriCfgRepositoryH2.findOne(Example.of(ex));
        System.out.println(findResult);

        return str.toString();
    }
}
