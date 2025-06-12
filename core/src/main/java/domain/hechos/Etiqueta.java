package domain.hechos;

// ETIQUETA
public class Etiqueta {
    private String nombre;
    private String descripcion;

    public Etiqueta(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esIdenticaA(Etiqueta etiqueta) {
        return this.nombre.equals(etiqueta.getNombre());
    }
}