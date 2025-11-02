package aplicacion.domain.conexiones;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ConexionFuenteDinamica extends Conexion{
    public ConexionFuenteDinamica(String id, String ip, Integer puerto){
        super(id, ip, puerto);
    }
}
