package domain.solicitudes;

// DETECTOR DE SPAM
public interface DetectorDeSpam{
    Boolean esSpam(String texto);
}