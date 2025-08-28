package aplicacion.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgregadorConfig {
    @JsonProperty("ip_agregador")
    private String ipAgregador;
    @JsonProperty("puerto_agregador")
    private int puertoAgregador;
}
