package aplicacion.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigService {
    private final MetamapaConfig config;

    public ConfigService() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.readValue(
                new ClassPathResource("config.json").getFile(),
                MetamapaConfig.class
        );
    }

    public String getUrlAgregador() {
        return "http://" + config.getIpAgregador() + ":" + config.getPuertoAgregador() + "/agregador";
    }
}
