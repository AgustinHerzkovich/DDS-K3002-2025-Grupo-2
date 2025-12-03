package aplicacion.dto;

import aplicacion.dto.output.HechoMapaOutputDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphQLColeccionHechosResponse {
    private GraphQLData data;

    public GraphQLData getData() {
        return data;
    }

    public void setData(GraphQLData data) {
        this.data = data;
    }

    @Getter
    @Setter
    public static class GraphQLData {
        @JsonProperty("getHechosPorColeccion")
        private HechosWrapper hechosPorColeccionIrrestrictos;
        @JsonProperty("getHechosPorColeccion")
        private HechosWrapper hechosPorColeccionCurados;
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

