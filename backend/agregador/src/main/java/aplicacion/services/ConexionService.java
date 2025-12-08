package aplicacion.services;

import aplicacion.domain.conexiones.Conexion;
import aplicacion.repositories.ConexionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConexionService {
    private final ConexionRepository conexionRepository;

    public ConexionService(ConexionRepository conexionRepository) {
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
