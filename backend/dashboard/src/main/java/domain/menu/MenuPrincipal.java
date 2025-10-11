package domain.menu;


public class MenuPrincipal extends MenuAbstracto {

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Que menu desea abrir?                                        (Presione 0 para salir)");
        System.out.println("1. Menu API publica");
        System.out.println("2. Menu API administrativa");
        System.out.println("3. Menu fuente dinámica");
        System.out.println("4. Menu estadisticas");
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
            case 4:
                new MenuEstadisticas().abrirMenu();
                break;
        }
        return false;
    }
}
