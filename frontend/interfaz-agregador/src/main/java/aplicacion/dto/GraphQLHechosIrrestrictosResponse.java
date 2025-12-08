package aplicacion.dto;

import aplicacion.dto.output.HechoMapaOutputDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphQLHechosIrrestrictosResponse {
    private GraphQLData data;

    @Getter
    @Setter
    public static class GraphQLData {
        @JsonProperty("getHechosPorColeccion")
        private HechosWrapper hechosPorColeccion;
    }

    @Getter
    @Setter
    public static class HechosWrapper {
        private java.util.List<HechoMapaOutputDto> content;
        private PageInfo pageInfo;
    }

    @Getter
    @Setter
    public static class PageInfo {
        private int totalElements;
        private int totalPages;
        private int number;
        private int size;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}

