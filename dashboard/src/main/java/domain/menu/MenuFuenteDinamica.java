package domain.menu;

import domain.subMenu.fuenteDinamica.SubMenuEnviarPostFuenteDinamica;
import domain.subMenu.fuenteDinamica.SubMenuPatchIdentidad;
import domain.subMenu.fuenteDinamica.SubMenuPostContribuyente;
import domain.subMenu.fuenteDinamica.SubMenuPostHecho;

public class MenuFuenteDinamica extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Elija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Hecho");
        System.out.println("2. Enviar Post Contribuyente");
        System.out.println("3. Enviar Patch Identidad");
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
                SubMenuPatchIdentidad.abrirMenu();
                break;
            case 4:
                SubMenuEnviarPostFuenteDinamica.abrirMenu();
                break;
        }
        return false;
    }
}
