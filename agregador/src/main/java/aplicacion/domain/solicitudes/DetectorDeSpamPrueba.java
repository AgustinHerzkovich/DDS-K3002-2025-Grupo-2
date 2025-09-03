package aplicacion.domain.solicitudes;

import jakarta.persistence.Embeddable;

@Embeddable
public class DetectorDeSpamPrueba implements DetectorDeSpam {
    public Boolean esSpam(String texto) {
        return texto.contains("wasd");
    }
}
