package domain.menu;

import domain.connectionManager.ConnectionManager;
import domain.subMenu.*;

public class MenuAgregador extends Menu{
    @Override
    void mostrarTextoOpciones() {
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Admin"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Publica"));
        System.out.println("Elija la opcion:");
        System.out.println("0. Salir");
        System.out.println("1. Enviar Post Coleccion (api administrativa)");
        System.out.println("2. Enviar Post FuenteDinamica (api administrativa)");
        System.out.println("3. Enviar Get Coleccion curada (api publica)");
        System.out.println("4. Enviar Get Coleccion irrestricta (api publica)");
        System.out.println("5. Enviar Get Colecciones (api publica)");
        System.out.println("6. Enviar Post Solicitud de eliminacion (api publica)");
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
                SubMenuEnviarPostFuente.abrirMenu();
                break;
            case 3:
                new SubMenuGetHechosCurados().abrirMenu();
                break;
            case 4:
                new SubMenuGetHechosIrrestrictos().abrirMenu();
                break;
            case 5:
                SubMenuGetColecciones.abrirMenu();
                break;
            case 6:
                SubMenuPostSolicitud.abrirMenu();
                break;
        }
        return false;
    }
}
