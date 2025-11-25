package domain.menu;

import java.util.Scanner;

public abstract class MenuAbstracto {
    String textoAMostrar;
    public void abrirMenu(){
        boolean exit = false;
        int opcion = 0;
        while (!exit){
            try{
                opcion = elegirOpcionMenuPrincipal();
                exit = ejecutarOperacionMenuPrincipal(opcion);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    private int elegirOpcionMenuPrincipal() throws Exception {
        Scanner scanner = new Scanner(System.in);
        mostrarTextoOpciones();
        return scanner.nextInt();
    }
    abstract void mostrarTextoOpciones();
    abstract boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception;
}
