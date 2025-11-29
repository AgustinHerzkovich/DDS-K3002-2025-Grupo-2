package aplicacion.graphql.controllers;

import aplicacion.graphql.objects.HechoFiltros;
import aplicacion.graphql.objects.HechoItem;
import aplicacion.graphql.objects.HechoItemPage;
import aplicacion.graphql.objects.PageInfo;
import aplicacion.services.HechoService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HechoGraphqlController {
    private final HechoService hechoService;

    public HechoGraphqlController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @QueryMapping
    public HechoItemPage getHechos(@Argument HechoFiltros filtros, @Argument Integer page, @Argument Integer limit) {
        Page<HechoItem> hechos = hechoService.obtenerHechosParaMapaGraphql(
            filtros,
            page == null ? 0 : page,
            limit == null ? 100 : limit
        );

        PageInfo pageInfo = new PageInfo(
            (int) hechos.getTotalElements(),
            hechos.getTotalPages(),
            hechos.getNumber(),
            hechos.getSize(),
            hechos.hasNext(),
            hechos.hasPrevious()
        );

        return new HechoItemPage(hechos.getContent(), pageInfo);
    }

    @QueryMapping
    public HechoItemPage getHechosPorColeccionIrrestrictos(@Argument String idColeccion, @Argument HechoFiltros filtros, @Argument Integer page, @Argument Integer limit) {
        Page<HechoItem> hechos = hechoService.obtenerHechosDeColeccionIrrestrictosGraphql(
                idColeccion,
                filtros,
                page == null ? 0 : page,
                limit == null ? 100 : limit
        );

        PageInfo pageInfo = new PageInfo(
                (int) hechos.getTotalElements(),
                hechos.getTotalPages(),
                hechos.getNumber(),
                hechos.getSize(),
                hechos.hasNext(),
                hechos.hasPrevious()
        );

        return new HechoItemPage(hechos.getContent(), pageInfo);
    }


    @QueryMapping
    public HechoItemPage getHechosPorColeccionCurados(@Argument String idColeccion, @Argument HechoFiltros filtros, @Argument Integer page, @Argument Integer limit){
        Page<HechoItem> hechos = hechoService.obtenerHechosDeColeccionCuradosGraphql(
                idColeccion,
                filtros,
                page == null ? 0 : page,
                limit == null ? 100 : limit
        );

        PageInfo pageInfo = new PageInfo(
                (int) hechos.getTotalElements(),
                hechos.getTotalPages(),
                hechos.getNumber(),
                hechos.getSize(),
                hechos.hasNext(),
                hechos.hasPrevious()
        );

        return new HechoItemPage(hechos.getContent(), pageInfo);
    }
}