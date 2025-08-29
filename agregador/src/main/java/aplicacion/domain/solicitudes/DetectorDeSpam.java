package domain.solicitudes;

// DETECTOR DE SPAM
public interface DetectorDeSpam{            // Suponemos que estará implementado por una biblioteca o algo similar
    Boolean esSpam(String texto);
}

