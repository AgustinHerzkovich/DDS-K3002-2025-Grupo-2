package aplicacion.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Getter
@Setter
public class ConfigService {
    private final AgregadorConfig config;

    public ConfigService() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.readValue(
                new ClassPathResource("config.json").getFile(),
                AgregadorConfig.class
        );
    }

    public String getUrl() {
        return "http://" + config.getIpAgregador() + ":" + config.getPuertoAgregador() + "/agregador";
    }
}
