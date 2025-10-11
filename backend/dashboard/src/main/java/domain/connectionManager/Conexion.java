package domain.connectionManager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Conexion {
    String nombre; // Nomas para diferenciarlos
    String ip;
    Integer puerto;
    public Conexion(String ip, Integer puerto, String nombre){
        this.ip = ip;
        this.puerto = puerto;
        this.nombre = nombre;
    }
    public String getUri(){
        return "http://"+ip+":" + puerto;
    }
    @Override
    public String toString() {
        return "ServerConnection{" +
                "ip='" + ip + '\'' +
                ", puerto=" + puerto +
                ", uri='" + getUri() + '\'' +
                '}';
    }

    public Boolean ipEquals(String ipComparada){
        return Objects.equals(ip, ipComparada);
    }
}
