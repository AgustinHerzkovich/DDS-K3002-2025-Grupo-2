package colecciones;

import domain.colecciones.criterios.CriterioDePertenencia;
import domain.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.criterios.CriterioDePertenencia;

import java.util.ArrayList;
import java.util.List;

// COLECCION
public class Coleccion{
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criterios_pertenencia;
    private Fuente fuente;
    private String identificadorHandle;

    public Coleccion(String titulo, String descripcion, Fuente fuente, String identificadorHandle) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios_pertenencia = new ArrayList<>();
        this.fuente = fuente;
        this.identificadorHandle = identificadorHandle;
    }

    public List<Hecho> mostrarHechos(){
        //TODO
    }

    public Boolean cumpleCriterios(Hecho hecho){
        return criterios_pertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public List<Hecho> mostrarHechosFiltrados(List<CriterioDePertenencia> filtros){
        //TODO
    }

    public void validarIdentificador(String identificador){
        //TODO : Ver si solo retornamos Boolean o debemos tirar alguna excepcion
	// Se debe validar que sea alfanumérico sin espacios
    }
}