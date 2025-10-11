package domain.menu;

import domain.menu.subMenu.apiAdministrativa.SubMenuPostColeccion;

public class MenuApiAdministrativa extends MenuAbstracto {
    @Override
    void mostrarTextoOpciones() {
        System.out.println("API Administrativa\nElija una opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Coleccion");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                SubMenuPostColeccion.abrirMenu();
                break;
        }
        return false;
    }
}
