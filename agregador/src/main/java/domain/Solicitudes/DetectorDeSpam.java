package domain.Solicitudes;

// DETECTOR DE SPAM
public interface DetectorDeSpam{
    boolean esSpam(String texto);
}