package aplicacion.domain.fuentesProxy.fuentesMetamapa;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import aplicacion.domain.fuentesProxy.FuenteProxy;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteMetamapa extends FuenteProxy {

    private String url;

    public FuenteMetamapa(String url) {
        this.url = url;
    }
}
