package testUnitarios.services;

import aplicacion.services.normalizador.NormalizadorDeTerminos;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestNormalizador {
    @Test
    public void testNormalizadorDeTerminos(){
        NormalizadorDeTerminos norm = new NormalizadorDeTerminos(1);
        List<String> terminos = new ArrayList<>();
        terminos.add("Manzana");
        terminos.add("Banana");
        terminos.add("Pera");
        terminos.add("Anana");
        terminos.add("Damasco");
        terminos.add("Uva");
        terminos.add("Uba");
        terminos.add("uva");
        terminos.add("Anona");
        terminos.add("Manzan");
        terminos.add("Uy");
        List<String> terminosNormalizados = new ArrayList<>();
        for (String t : terminos){
            String out = norm.normalizarTermino(t);
            if(out == null){
                norm.agregarTermino(t);
                terminosNormalizados.add(t);
            }
            else {
                terminosNormalizados.add(out);
            }
        }

        assert (Objects.equals(terminosNormalizados.get(7), "Uva"));
        assert (Objects.equals(terminosNormalizados.getLast(), "Uy"));


    }
}
