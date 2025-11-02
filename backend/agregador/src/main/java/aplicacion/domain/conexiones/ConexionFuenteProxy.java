package aplicacion.domain.conexiones;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ConexionFuenteProxy extends Conexion{
    public ConexionFuenteProxy(String id, String ip, Integer puerto){
        super(id, ip, puerto);
    }
}
