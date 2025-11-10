package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import aplicacion.dto.input.HechoInputDto;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteEstatica extends Fuente {
    private Boolean fueConsultada;

    public FuenteEstatica(String id) {
        super(id);
        this.fueConsultada = false;
    }

    @Override
    public List<HechoInputDto> getHechosUltimaPeticion(String url) {
        if (this.fueConsultada) {
            return new ArrayList<>();
        }
        this.fueConsultada = true;
        return super.getHechosUltimaPeticion(url);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesEstaticas/" + this.getId();
    }
}
