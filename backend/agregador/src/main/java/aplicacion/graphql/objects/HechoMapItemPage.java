package aplicacion.graphql.objects;

import java.util.List;

public record HechoMapItemPage(
        List<HechoMapItem> content,
        PageInfo pageInfo
) {
}

