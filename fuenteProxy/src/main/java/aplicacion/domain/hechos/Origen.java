package aplicacion.domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;

// ORIGEN
public enum Origen {
    EXTERNO;

    @JsonCreator
    public static Origen fromString(String value) {
        if (value == null) return null;
        switch (value.toUpperCase()) {
            case "DATASET", "CARGA_MANUAL","CONTRIBUYENTE", "EXTERNO":
                return EXTERNO; // lo mapeás a EXTERNO, si querés
            default:
                throw new IllegalArgumentException("Valor desconocido para Origen: " + value);
        }
    }
}
