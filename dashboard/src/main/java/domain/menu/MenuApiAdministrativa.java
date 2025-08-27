package domain.menu;

import domain.subMenu.apiAdministrativa.SubMenuEnviarPostFuenteDinamica;
import domain.subMenu.apiAdministrativa.SubMenuPostColeccion;

public class MenuApiAdministrativa extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println("API Administrativa\nElija una opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Coleccion");
        System.out.println("2. Enviar Post FuenteDinamica");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                SubMenuPostColeccion.abrirMenu();
                break;
            case 2:
                SubMenuEnviarPostFuenteDinamica.abrirMenu();
                break;
        }
        return false;
    }
}
