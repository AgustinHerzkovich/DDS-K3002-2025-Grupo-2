package aplicacion.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class FuenteEstaticaInputDto extends FuenteInputDto {
    public FuenteEstaticaInputDto(String id){
        super(id);
    }
}
