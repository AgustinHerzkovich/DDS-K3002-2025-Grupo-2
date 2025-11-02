package aplicacion.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FuenteDinamicaInputDto extends FuenteInputDto {
    public FuenteDinamicaInputDto(String id){
        super(id);
    }
}
