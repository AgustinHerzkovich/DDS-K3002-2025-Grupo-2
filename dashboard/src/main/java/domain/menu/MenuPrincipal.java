package domain.menu;

public class MenuPrincipal extends Menu{

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Que menu abrir");
        System.out.println("0. Salir");
        System.out.println("1. Menu agregador (api publica y administrativa)");
        System.out.println("2. Menu fuente dinámica");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                new MenuAgregador().abrirMenu();
                break;
            case 2:
                new MenuFuenteDinamica().abrirMenu();
                break;
        }
        return false;
    }
}
