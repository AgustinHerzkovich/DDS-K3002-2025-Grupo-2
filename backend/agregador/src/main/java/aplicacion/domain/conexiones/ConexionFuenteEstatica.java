package aplicacion.domain.conexiones;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ConexionFuenteEstatica extends Conexion{
    public ConexionFuenteEstatica(String id, String ip, Integer puerto){
        super(id, ip, puerto);
    }
}
