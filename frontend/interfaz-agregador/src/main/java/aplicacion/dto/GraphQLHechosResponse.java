package aplicacion.dto;

import aplicacion.dto.output.HechoMapaOutputDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphQLHechosResponse {
    private GraphQLData data;

    @Getter
    @Setter
    public static class GraphQLData {
        private HechosEnMapaWrapper hechosEnMapa;
    }

    @Getter
    @Setter
    public static class HechosEnMapaWrapper {
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

