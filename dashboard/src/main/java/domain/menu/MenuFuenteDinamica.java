package domain.menu;

import domain.connectionManager.ConnectionManager;
import domain.subMenu.SubMenuPatchIdentidad;
import domain.subMenu.SubMenuPostColeccion;
import domain.subMenu.SubMenuPostContribuyente;
import domain.subMenu.SubMenuPostHecho;

public class MenuFuenteDinamica extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Admin"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Publica"));
        System.out.println("Elija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Hecho");
        System.out.println("2. Enviar Post Contribuyente");
        System.out.println("3. Enviar Patch Identidad");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                SubMenuPostHecho.abrirMenu();
                break;
            case 2:
                SubMenuPostContribuyente.abrirMenu();
                break;
            case 3:
                SubMenuPatchIdentidad.abrirMenu();
                break;
        }
        return false;
    }
}
