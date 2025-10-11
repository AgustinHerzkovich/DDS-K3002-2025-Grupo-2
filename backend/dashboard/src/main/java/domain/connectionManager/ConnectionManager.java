package domain.connectionManager;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConnectionManager {
    private Integer puertoDefault;
    private final List<Conexion> servidoresRegistrados = new ArrayList<>();
    private static ConnectionManager instance;
    //Singleton
    private ConnectionManager(){
    }
    public static ConnectionManager getInstance(){
        if(instance == null ){
            instance = new ConnectionManager();
        }
        return instance;
    }


    public void registrarLocalHost(){
        registrarServidor("localhost", "Servidor local");
    }
    public void registrarLocalHost(Integer puerto, String nombre){
        registrarServidor("localhost", nombre, puerto);
    }
    public void registrarServidor(String servidor, String nombre, Integer puerto) {
        servidoresRegistrados.add(new Conexion(servidor, puerto, nombre));
    }
    public void registrarServidor(String servidor, String nombre) {
        servidoresRegistrados.add(new Conexion(servidor, puertoDefault, nombre));
    }
    public List<Conexion> getServerConnections(){
        return servidoresRegistrados;
    }
    public Conexion getServidorLocal(String nombre){
        return getServidorByIpAndNombre("localhost", nombre);
    }
    public Conexion getServidorByIpAndNombre(String ip, String nombre){
        final Optional<Conexion> serverConnection = servidoresRegistrados.stream().filter(serv -> serv.ipEquals(ip)).filter(serv-> Objects.equals(serv.getNombre(), nombre)).findFirst();
        if(serverConnection.isEmpty()) {
            throw new RuntimeException("No se encontro el servidor");
        }
        return serverConnection.get();
    }

}
