package aplicacion.domain.solicitudes;

import jakarta.persistence.Embeddable;

// DETECTOR DE SPAM
@Embeddable
public interface DetectorDeSpam{            // Suponemos que estará implementado por una biblioteca o algo similar
    Boolean esSpam(String texto);
}

