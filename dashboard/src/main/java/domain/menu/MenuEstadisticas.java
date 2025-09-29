package domain.menu;


import domain.menu.subMenu.estadisticas.*;

public class MenuEstadisticas extends MenuAbstracto {
    @Override
    void mostrarTextoOpciones() {
        System.out.println("""
                Elija una opcion
                0) Salir
                1) Actualizar estadisticas
                2) Ver cantidad de solicitudes de eliminacion SPAM
                3) Ver provincias con mas hechos de cierta coleccion
                4) Ver provincias con mas hechos de cierta categoria
                5) Ver horas con mas hechos de cierta categoria
                6) Ver categorias con mas hechos
                7) Ver colecciones disponibles
                """);
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                SubMenuActualizarEstadisticas.abrirMenu();
                break;
            case 2:
                SubMenuObtenerSolicitudesSpam.abrirMenu();
                break;
            case 3:
                SubMenuProvinciasConMasHechosDeColeccion.abrirMenu();
                break;
            case 4:
                SubMenuProvinciasConMasHechosDeCategoria.abrirMenu();
                break;
            case 5:
                SubMenuHorasConMasHechosDeCategoria.abrirMenu();
                break;
            case 6:
                SubMenuCategoriasConMasHechos.abrirMenu();
                break;
            case 7:
                SubMenuColeccionesDisponibles.abrirMenu();
                break;
        }
        return false;
    }
}
