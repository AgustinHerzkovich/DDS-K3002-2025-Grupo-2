package domain.solicitudes;

// DETECTOR DE SPAM
public class DetectorDeSpam{            // Suponemos que estará implementado por una biblioteca o algo similar
    Boolean esSpam(String texto){
        if (texto.contains("wasd")) // PLACEHOLDER
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}