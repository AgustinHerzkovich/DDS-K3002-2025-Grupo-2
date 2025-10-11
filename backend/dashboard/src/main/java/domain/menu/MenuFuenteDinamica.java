package domain.menu;

import domain.menu.subMenu.fuenteDinamica.SubMenuEnviarPostFuenteDinamica;
import domain.menu.subMenu.fuenteDinamica.SubMenuPostIdentidad;
import domain.menu.subMenu.fuenteDinamica.SubMenuPostContribuyente;
import domain.menu.subMenu.fuenteDinamica.SubMenuPostHecho;

public class MenuFuenteDinamica extends MenuAbstracto {
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Elija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Hecho");
        System.out.println("2. Enviar Post Contribuyente");
        System.out.println("3. Enviar Post Identidad");
        System.out.println("4. Enviar Post FuenteDinamica");

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
                SubMenuPostIdentidad.abrirMenu();
                break;
            case 4:
                SubMenuEnviarPostFuenteDinamica.abrirMenu();
                break;
        }
        return false;
    }
}
