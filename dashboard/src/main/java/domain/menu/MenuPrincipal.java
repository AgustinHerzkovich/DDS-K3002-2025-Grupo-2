package domain.menu;

public class MenuPrincipal extends Menu{

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Que menu desea abrir?");
        System.out.println("0. Salir");
        System.out.println("1. Menu API publica");
        System.out.println("2. Menu API administrativa");
        System.out.println("3. Menu fuente dinámica");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 0:
                return true;
            case 1:
                new MenuApiPublica().abrirMenu();
                break;
            case 2:
                new MenuApiAdministrativa().abrirMenu();
                break;
            case 3:
                new MenuFuenteDinamica().abrirMenu();
                break;
        }
        return false;
    }
}
