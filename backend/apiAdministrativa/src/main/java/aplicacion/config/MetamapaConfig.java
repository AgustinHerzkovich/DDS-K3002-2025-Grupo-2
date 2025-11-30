package aplicacion.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MetamapaConfig {
    @JsonProperty("ip_agregador")
    private String ipAgregador;
    @JsonProperty("puerto_agregador")
    private int puertoAgregador;

    @JsonProperty("ip_estadisticas")
    private String ipEstadisticas;
    @JsonProperty("puerto_estadisticas")
    private int puertoEstadisticas;

    @JsonProperty("ip_fuente_estatica")
    private String ipFuenteEstatica;
    @JsonProperty("puerto_fuente_estatica")
    private int puertoFuenteEstatica;
}
