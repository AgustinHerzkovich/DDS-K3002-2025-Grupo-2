package domain.menu;

import domain.subMenu.SubMenuPostColeccion;

public class MenuAgregador extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Elija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Hecho");
        System.out.println("2. Enviar Post Coleccion");
        System.out.println("3. Enviar Get Colecciones");
        System.out.println("4. Enviar Get Coleccion restricta");
        System.out.println("5. Enviar Get Coleccion irrestricta");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                break;
            case 2:
                SubMenuPostColeccion.abrirMenu();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
        return false;
    }
}
