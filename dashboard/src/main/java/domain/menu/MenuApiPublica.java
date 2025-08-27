package domain.menu;

import domain.subMenu.apiAdministrativa.SubMenuEnviarPostFuenteDinamica;
import domain.subMenu.apiAdministrativa.SubMenuPostColeccion;
import domain.subMenu.apiPublica.SubMenuGetColecciones;
import domain.subMenu.apiPublica.SubMenuGetHechosCurados;
import domain.subMenu.apiPublica.SubMenuGetHechosIrrestrictos;
import domain.subMenu.apiPublica.SubMenuPostSolicitud;

public class MenuApiPublica extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println("API Publica\nElija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Get Coleccion curada");
        System.out.println("2. Enviar Get Coleccion irrestricta");
        System.out.println("3. Enviar Get Colecciones");
        System.out.println("4. Enviar Post Solicitud de eliminacion");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                new SubMenuGetHechosCurados().abrirMenu();
                break;
            case 2:
                new SubMenuGetHechosIrrestrictos().abrirMenu();
                break;
            case 3:
                SubMenuGetColecciones.abrirMenu();
                break;
            case 4:
                SubMenuPostSolicitud.abrirMenu();
                break;
        }
        return false;
    }
}
