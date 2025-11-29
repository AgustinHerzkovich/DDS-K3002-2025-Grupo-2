package aplicacion.graphql.objects;

import java.util.List;

public record HechoItemPage(
        List<HechoItem> content,
        PageInfo pageInfo
) {
}

