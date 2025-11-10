package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import aplicacion.dto.input.HechoInputDto;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteEstatica extends Fuente {
    private Boolean fueConsultada;

    public FuenteEstatica(String id, String nombreServicio) {
        super(id, nombreServicio);
        this.fueConsultada = false;
    }

    public FuenteEstatica(String id) {
    }

    @Override
    public List<HechoInputDto> getHechosUltimaPeticion() {
        if (this.fueConsultada) {
            return new ArrayList<>();
        }
        this.fueConsultada = true;
        return super.getHechosUltimaPeticion();
    }

    @Override
    public String pathIntermedio() {
        return "fuentesEstaticas/" + this.getId();
    }
}
