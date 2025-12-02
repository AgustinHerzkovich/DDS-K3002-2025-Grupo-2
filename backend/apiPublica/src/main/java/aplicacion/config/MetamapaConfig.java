package aplicacion.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetamapaConfig {
    @JsonProperty("ip_agregador")
    private String ipAgregador;
    @JsonProperty("puerto_agregador")
    private int puertoAgregador;

    @JsonProperty("ip_estadisticas")
    private String ipEstadisticas;
    @JsonProperty("puerto_estadisticas")
    private int puertoEstadisticas;

    @JsonProperty("ip_fuente_dinamica")
    private String ipFuenteDinamica;
    @JsonProperty("puerto_fuente_dinamica")
    private int puertoFuenteDinamica;
}
