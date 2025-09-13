package aplicacion.domain.colecciones.fuentes;

import aplicacion.dto.input.HechoInputDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("estatica")
@NoArgsConstructor
@Getter
@Setter
public class FuenteEstatica extends Fuente {
    private Boolean fueConsultada;

    public FuenteEstatica(FuenteId id, String ip, Integer puerto) {
        super(id, ip, puerto);
        this.fueConsultada = false;
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
        return "fuentesEstaticas/" + this.getId().getIdExterno();
    }
}
