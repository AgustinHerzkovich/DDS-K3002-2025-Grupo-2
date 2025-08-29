package aplicacion.domain.solicitudes;

public class DetectorDeSpamPrueba implements DetectorDeSpam {
    public Boolean esSpam(String texto) {
        return texto.contains("wasd");
    }
}
