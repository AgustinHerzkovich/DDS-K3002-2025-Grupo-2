package aplicacion.graphql.objects;

public record PageInfo(
        int totalElements,
        int totalPages,
        int number,
        int size,
        boolean hasNext,
        boolean hasPrevious
) {
}

