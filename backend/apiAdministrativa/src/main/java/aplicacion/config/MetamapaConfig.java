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

    @JsonProperty("ip_fuente_estatica")
    private String ipFuenteEstatica;
    @JsonProperty("puerto_fuente_estatica")
    private int puertoFuenteEstatica;

    @JsonProperty("ip_fuente_proxy")
    private String ipFuenteProxy;
    @JsonProperty("puerto_fuente_proxy")
    private int puertoFuenteProxy;

    @JsonProperty("ip_fuente_dinamica")
    private String ipFuenteDinamica;
    @JsonProperty("puerto_fuente_dinamica")
    private int puertoFuenteDinamica;
}
