package aplicacion.dto.input;

import aplicacion.dto.mappers.FuenteEstaticaInputMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Setter
public class FuenteEstaticaInputDto extends FuenteInputDto {
    public FuenteEstaticaInputDto(String id){
        super(id);
    }
}
