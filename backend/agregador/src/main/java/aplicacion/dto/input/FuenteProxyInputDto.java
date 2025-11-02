package aplicacion.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FuenteProxyInputDto extends FuenteInputDto {

    public FuenteProxyInputDto(String id) {
    super(id);
    }
}
