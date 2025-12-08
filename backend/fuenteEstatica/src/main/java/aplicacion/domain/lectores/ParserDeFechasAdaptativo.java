package aplicacion.domain.lectores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ParserDeFechasAdaptativo {

    private static final List<DateTimeFormatter> FORMATTERS = new ArrayList<>(); // Formato ordenados por orden de prioridad

    static {
        // Formato ISO
        FORMATTERS.add(DateTimeFormatter.ISO_DATE_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_DATE);

        // Formato DD/MM/YYYY
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Otros formatos
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }


    public static LocalDateTime parse(String fecha) throws DateTimeParseException {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(fecha, formatter);
            } catch (DateTimeParseException e) {
                try {
                    return java.time.LocalDate.parse(fecha, formatter).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                }
            }
        }
        throw new DateTimeParseException("No se pudo parsear la fecha: " + fecha, fecha, 0);
    }
}