package aplicacion.domain.conexiones;

import jakarta.persistence.*;
import lombok.*;

import java.net.URI;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Conexion {
    @Id
    private String id; // Id unica del nodo. Si el nodo cambia de IP o puerto, se podria seguir identificando por esto
    private String ip;
    private Integer puerto;
    private String protocolo;
    private boolean onlineUltimaVez;
    public Conexion(String id, String ip, Integer puerto){
        this.id = id;
        this.puerto = puerto;
        this.ip = ip;
        this.protocolo = "http";
        this.onlineUltimaVez = true;
    }
    public String construirURI() {
        return URI.create(protocolo + "://" + ip + ":" + puerto).toString();
    }
    public void update(Conexion conexion){
        this.ip = conexion.ip;
        this.puerto = conexion.puerto;
        this.protocolo = conexion.protocolo;
    }
}
