package domain.subMenu.apiPublica;

import domain.dashboardDTOs.hechos.HechoDTO;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Scanner;

@NoArgsConstructor
public abstract class SubMenuGetHechos {
    String id;
    public void abrirMenu(){
        System.out.println("Ingrese el ID de la coleccion:");
        Scanner sc = new Scanner(System.in);
        id = sc.nextLine();
        System.out.println("Hechos obtenidos:");
        this.obtenerHechos().forEach(hechoDTO -> {System.out.println("--------");System.out.println(hechoDTO.toString());});
    }
    abstract List<HechoDTO> obtenerHechos();
}
