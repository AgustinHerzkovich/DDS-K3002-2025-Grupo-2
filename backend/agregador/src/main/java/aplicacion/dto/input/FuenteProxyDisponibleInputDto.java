package aplicacion.dto.input;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class FuenteProxyDisponibleInputDto extends FuenteDisponibleInputdto{
    public FuenteProxyDisponibleInputDto(String id){
        super(id);
    }
}

