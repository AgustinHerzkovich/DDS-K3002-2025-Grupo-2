package testUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;

public class RandomThingsGenerator {

    private static final Random random = new Random();

    private static final List<String> SUJETOS = Arrays.asList(
            "el perro", "una vecina", "un testigo", "la policía", "un trabajador", "una persona anónima", "una anciana", "un elefante", "un terrorista", "un honorable miembro de la camara de senadores"
    );

    private static final List<String> VERBOS = Arrays.asList(
            "vio", "escuchó", "reportó", "denunció", "evitó" , "filmó", "investigó", "causo", "se balanceaba sobre", "participó en"
    );

    private static final List<String> OBJETOS = Arrays.asList(
            "un robo", "un accidente", "una manifestación", "un show de cocina", "una pelea", "un incendio", "una situación sospechosa", "la tela de una araña", "un desfile multicultural", "una batalla de rap"
    );

    private static final List<String> PALABRAS = Arrays.asList(
            "robo", "sospechoso", "urgente", "vecino", "denuncia", "evidencia", "testimonio", "baile", "peligroso"
    );

    private static final List<String> NOMBRES = List.of(
            "Juan", "María", "Lucía", "Carlos", "Sofía", "Andrés", "Valentina", "Mateo"
    );

    private static final List<String> APELLIDOS = List.of(
            "Gómez", "Pérez", "Rodríguez", "Fernández", "López", "Martínez", "Díaz", "Torres"
    );

    private static final List<String> YOUTUBE_VIDEOS = List.of(
            "https://youtu.be/dQw4w9WgXcQ",
            "https://youtu.be/tgbNymZ7vqY",
            "https://youtu.be/Gp_7aieGEMQ?t=43"
    );
    private static final List<String> PLANTILLAS = List.of(
            "%s %s %s. ",
            "Ayer, %s %s %s en la calle. ",
            "Se reportó que %s %s %s. ",
            "%s aparentemente %s %s. ",
            "Según testigos, %s %s %s. ",
            "Mientras caminaba, %s %s %s. ",
            "Me contó un pajarito que, %s %s %s cerca de cierto lugar. ",
            "%s dice que mi perro %s %s. "
    );

    private static final List<String> CONECTORES = List.of(
            "Además,", "Sin embargo,", "Por otro lado,", "Más tarde,", "Entonces,", "Mientras tanto,"
    );

    public static String generarLinkYoutubeAleatorio() {
        return YOUTUBE_VIDEOS.get(random.nextInt(YOUTUBE_VIDEOS.size()));
    }

    public static Origen generarOrigenAleatorio() {
        Origen[] valores = Origen.values();
        return valores[random.nextInt(valores.length)];
    }

    public static String generarNombreAleatorio() {
        return NOMBRES.get(random.nextInt(NOMBRES.size()));
    }

    public static String generarApellidoAleatorio() {
        return APELLIDOS.get(random.nextInt(APELLIDOS.size()));
    }


    private static LocalDateTime generarFechaAleatoria(LocalDate desde, LocalDate hasta) {
        long startEpochDay = desde.toEpochDay();
        long endEpochDay = hasta.toEpochDay();

        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);

        return randomDate.atTime(hour, minute, second);
    }
    public static LocalDate generarFechaDeNacimientoAleatoria(){
        return generarFechaAleatoria(LocalDate.now().minusYears(80), LocalDate.now().minusYears(18)).toLocalDate();
    }
    public LocalDateTime generarFechaDePublicacionAleatoria(){
        return generarFechaAleatoria(LocalDate.now().minusYears(10), LocalDate.now());
    }
    public static String generarOracionAleatoria() {
        String sujeto = mayusculaPrimeraLetra(SUJETOS.get(random.nextInt(SUJETOS.size())));
        String verbo = VERBOS.get(random.nextInt(VERBOS.size()));
        String objeto = OBJETOS.get(random.nextInt(OBJETOS.size()));
        return sujeto + " " + verbo + " " + objeto + ".";
    }
    public static String generarOracionComplejaAleatoria() {
        String sujeto = SUJETOS.get(random.nextInt(SUJETOS.size()));
        String verbo = VERBOS.get(random.nextInt(VERBOS.size()));
        String objeto = OBJETOS.get(random.nextInt(OBJETOS.size()));
        String plantilla = PLANTILLAS.get(random.nextInt(PLANTILLAS.size()));
        if(plantilla.charAt(0) == '%'){
            sujeto = mayusculaPrimeraLetra(sujeto);
        }
        return String.format(plantilla, sujeto, verbo, objeto);
    }
    public static String generarTextoAleatorio() {
        StringBuilder texto = new StringBuilder();
        texto.append(generarOracionComplejaAleatoria());
        while (texto.length() < 200) {
            String conector = CONECTORES.get(random.nextInt(CONECTORES.size()));
            texto.append(conector).append(" ");
            texto.append(minusculaPrimeraLetra(generarOracionComplejaAleatoria())).append(" ");
        }

        return texto.toString().trim();
    }

    public static String generarPalabraAleatoria() {
        return PALABRAS.get(random.nextInt(PALABRAS.size()));
    }

    public static Ubicacion generarUbicacionAleatoria() {
        double latitud = -90 + (180 * random.nextDouble());
        double longitud = -180 + (360 * random.nextDouble());
        return new Ubicacion(latitud, longitud);
    }
    private static String mayusculaPrimeraLetra(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }
        return palabra.substring(0, 1).toUpperCase() + palabra.substring(1);
    }
    private static String minusculaPrimeraLetra(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }
        return palabra.substring(0, 1).toLowerCase() + palabra.substring(1);
    }
}