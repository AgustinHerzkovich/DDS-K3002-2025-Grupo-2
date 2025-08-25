package domain.subMenu;

import domain.DTOs.IdentidadPatchDTO;
import domain.DTOs.PostContribuyenteDTO;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

public class SubMenuPatchIdentidad {
    @Setter
    public static Integer lastContribuyenteId = 1;
    public static void abrirMenu(){
        Integer id;
        LocalDate nacimiento;
        System.out.println("Se enviara un patch a fuenteDinamica.");
        System.out.println("Ingrese el ID del contribuyente al que le va a hacer patch");
        System.out.println("Default: "+lastContribuyenteId);
        Scanner scanner = new Scanner(System.in);
        IdentidadPatchDTO identidadPatchDTO;
        try {
            id = scanner.nextInt();
        } catch (Exception e){
            id = lastContribuyenteId;
        }
        System.out.println("Ingrese el nombre de la identidad");
        System.out.println("Default: Barbara");
        String nombre = scanner.nextLine();
        nombre = (nombre == null || Objects.equals(nombre, "")) ? "Barbara" : nombre;
        System.out.println("Ingrese el apellido de la identidad");
        System.out.println("Default: Liskov");
        String apellido = scanner.nextLine();
        apellido = (apellido == null || Objects.equals(apellido, "")) ? "Liskov" : apellido;
        System.out.println("Ingrese la fecha de nacimiento de la identidad");
        System.out.println("Default: 1939-11-7");
        try {
            nacimiento = LocalDate.parse(scanner.nextLine());
        }catch (Exception e){
            nacimiento = LocalDate.parse("1939-11-7");
        }
        identidadPatchDTO = new IdentidadPatchDTO(nombre, apellido, nacimiento);
        ApiClient.patchIdentidad(identidadPatchDTO, id, ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        System.out.println("Patch enviado");
    }
}
