package aplicacion.services;

import aplicacion.domain.conexiones.Conexion;
import aplicacion.repositorios.RepositorioDeConexiones;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConexionService {
    private final RepositorioDeConexiones conexionRepository;

    public ConexionService(RepositorioDeConexiones conexionRepository) {
        this.conexionRepository = conexionRepository;
    }

    public void agregarOActualizarConexion(Conexion conexion){
        Optional<Conexion> conexionBD = conexionRepository.findById(conexion.getId());
        if(conexionBD.isEmpty()){
            conexionRepository.save(conexion);
        }
        else {
            conexionBD.get().update(conexion);
            conexionRepository.save(conexionBD.get());
        }
    }
    public List<Conexion> findAllConexiones(){
        return conexionRepository.findAll();
    }

}
