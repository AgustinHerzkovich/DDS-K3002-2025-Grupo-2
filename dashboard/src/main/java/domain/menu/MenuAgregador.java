package domain.menu;

import domain.subMenu.*;

public class MenuAgregador extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Elija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Hecho");
        System.out.println("2. Enviar Post Coleccion");
        System.out.println("3. Enviar Post FuenteDinamica");
        System.out.println("4. Enviar Get Coleccion curada");
        System.out.println("5. Enviar Get Coleccion irrestricta");
        System.out.println("6. Enviar Get Colecciones");
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
                SubMenuPostColeccion.abrirMenu();
                break;
            case 3:
                SubMenuEnviarPostFuente.abrirMenu();
                break;
            case 4:
                new SubMenuGetHechosCurados().abrirMenu();
                break;
            case 5:
                new SubMenuGetHechosIrrestrictos().abrirMenu();
                break;
            case 6:
                SubMenuGetColecciones.abrirMenu();
                break;
        }
        return false;
    }
}
