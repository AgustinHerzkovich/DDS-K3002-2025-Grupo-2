package aplicacion.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true)
public class FuenteEstaticaDisponibleInputDto extends FuenteDisponibleInputdto {
    public FuenteEstaticaDisponibleInputDto(String id, List<String> archivos){
        super(id);
        this.archivos = archivos;
    }
    private List<String> archivos;
}
