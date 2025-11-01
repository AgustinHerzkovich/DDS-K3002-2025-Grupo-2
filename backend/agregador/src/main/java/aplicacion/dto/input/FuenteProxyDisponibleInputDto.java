package aplicacion.dto.input;

import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
public class FuenteProxyDisponibleInputDto extends FuenteDisponibleInputdto{
    public FuenteProxyDisponibleInputDto(String id){
        super(id);
    }
}

