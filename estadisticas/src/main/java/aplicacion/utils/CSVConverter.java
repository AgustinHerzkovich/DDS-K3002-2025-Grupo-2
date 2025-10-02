package aplicacion.utils;

import java.util.List;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CSVConverter {

    public static <T> String convert(List<T> elementos) throws Exception {
        if (elementos == null || elementos.isEmpty()) {
            return "";
        }

        CsvMapper mapper = new CsvMapper();
        Class<?> clazz = elementos.get(0).getClass();  // Toma la clase del primer elemento de elementos
        CsvSchema schema = mapper.schemaFor(clazz).withHeader();

        return mapper.writer(schema).writeValueAsString(elementos);
    }
}