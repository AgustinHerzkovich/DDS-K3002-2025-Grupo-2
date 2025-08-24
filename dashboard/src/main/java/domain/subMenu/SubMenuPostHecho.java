package domain.subMenu;

import domain.DTOs.HechoBuilder;
import domain.DTOs.HechoDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

public class SubMenuPostHecho {

    public static void abrirMenu() {
        Scanner scanner = new Scanner(System.in);
        HechoBuilder builder = new HechoBuilder();

        System.out.println("=== Crear Hecho ===");

        System.out.print("Título (default: 'Incendio forestal en formosa'): ");
        String titulo = scanner.nextLine();
        if (titulo.isEmpty()) titulo = "Incendio forestal en formosa";
        builder.setTitulo(titulo);

        System.out.print("Descripción (default: 'Se prendio fuego formosa'): ");
        String descripcion = scanner.nextLine();
        if (descripcion.isEmpty()) descripcion = "Se prendio fuego formosa";
        builder.setDescripcion(descripcion);

        System.out.print("Categoría (default: 'Incendios forestales'): ");
        String categoria = scanner.nextLine();
        if (categoria.isEmpty()) categoria = "Incendios forestales";
        builder.setCategoria(categoria);

        System.out.print("Latitud (default: 0): ");
        String latInput = scanner.nextLine();
        double lat = latInput.isEmpty() ? 0 : Double.parseDouble(latInput);

        System.out.print("Longitud (default: 0): ");
        String lonInput = scanner.nextLine();
        double lon = lonInput.isEmpty() ? 0 : Double.parseDouble(lonInput);
        builder.setUbicacion(lat, lon);

        System.out.print("Fecha de acontecimiento (yyyy-MM-ddTHH:mm:ss) (default: 2019-07-10T19:28:54): ");
        String fechaInput = scanner.nextLine();
        LocalDateTime fecha = fechaInput.isEmpty()
                ? LocalDateTime.parse("2019-07-10T19:28:54")
                : LocalDateTime.parse(fechaInput);
        builder.setFechaAcontecimiento(fecha);

        builder.setOrigen();

        System.out.print("Contenido de texto (default: 'Los bomberos ya apagaron el fuego'): ");
        String texto = scanner.nextLine();
        if (texto.isEmpty()) texto = "Los bomberos ya apagaron el fuego";
        builder.setContenidoTexto(texto);

        System.out.print("¿Agregar contenido multimedia? (S/N): ");
        String addMultimedia = scanner.nextLine();
        if (addMultimedia.isEmpty() || addMultimedia.equalsIgnoreCase("S")) {
            builder.addContenidoMultimedia("video", "mp4", 100, "1920x1080", 30);
        }

        System.out.print("¿Es anónimo? (true/false) (default: false): ");
        String anonInput = scanner.nextLine();
        boolean anonimato = !anonInput.isEmpty() && Boolean.parseBoolean(anonInput);
        builder.setAnonimato(anonimato);

        System.out.print("Nombre autor (default: 'Barbara'): ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) nombre = "Barbara";

        System.out.print("Apellido autor (default: 'Liskov'): ");
        String apellido = scanner.nextLine();
        if (apellido.isEmpty()) apellido = "Liskov";

        System.out.print("Fecha nacimiento (yyyy-MM-dd) (default: 1939-11-07): ");
        String fechaNacInput = scanner.nextLine();
        LocalDate fechaNac = fechaNacInput.isEmpty()
                ? LocalDate.parse("1939-11-07")
                : LocalDate.parse(fechaNacInput);

        System.out.print("ID contribuyente (default: 'Id ejemplo'): ");
        String idContribuyente = scanner.nextLine();
        if (idContribuyente.isEmpty()) idContribuyente = "Id ejemplo";

        System.out.print("¿Es administrador? (true/false) (default: true): ");
        String adminInput = scanner.nextLine();
        boolean esAdmin = adminInput.isEmpty() || Boolean.parseBoolean(adminInput);

        builder.setAutor(nombre, apellido, fechaNac, idContribuyente, esAdmin);

        HechoDTO hecho = builder.build();

        System.out.println("\n=== Hecho creado ===");
        System.out.println("Título: " + hecho.getTitulo());
        System.out.println("Descripción: " + hecho.getDescripcion());
        System.out.println("Categoría: " + hecho.getCategoria().getNombre());
        System.out.println("Ubicación: " + hecho.getUbicacion().getLatitud() + ", " + hecho.getUbicacion().getLongitud());
        System.out.println("Fecha Acontecimiento: " + hecho.getFechaAcontecimiento());
        System.out.println("Origen: " + hecho.getOrigen());
        System.out.println("Texto: " + hecho.getContenidoTexto());
        System.out.println("Multimedia: " + hecho.getContenidoMultimedia().size() + " archivo(s)");
        System.out.println("Anónimo: " + hecho.isAnonimato());
        System.out.println("Autor: " + hecho.getAutor().getNombre() + " " + hecho.getAutor().getApellido());
        System.out.println("Contribuyente ID: " + hecho.getAutor().getContribuyente().getContribuyenteId());
        System.out.println("Es administrador: " + hecho.getAutor().getContribuyente().isEsAdministrador());
        scanner.close();
        ApiClient.postHecho(hecho, ConnectionManager.getInstance().getServidorLocal("Dinamica"));
    }
}