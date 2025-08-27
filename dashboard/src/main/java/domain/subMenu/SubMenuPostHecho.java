package domain.subMenu;

import java.time.LocalDateTime;
import java.util.Scanner;
import domain.dashboardDTOs.HechoPostDTO;

import domain.dashboardDTOs.HechoPostBuilder;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

public class SubMenuPostHecho {

    public static void abrirMenu() {
        Scanner scanner = new Scanner(System.in);
        HechoPostBuilder builder = new HechoPostBuilder();

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

        System.out.print("Contenido de texto (default: 'Los bomberos ya apagaron el fuego'): ");
        String texto = scanner.nextLine();
        if (texto.isEmpty()) texto = "Los bomberos ya apagaron el fuego";
        builder.setContenidoTexto(texto);

        System.out.print("¿Agregar contenido multimedia? (S/N) (Default: S): ");
        String addMultimedia = scanner.nextLine();
        if (addMultimedia.isEmpty() || addMultimedia.equalsIgnoreCase("S")) {
            builder.addContenidoMultimedia("video", "mp4", 100, "1920x1080", 30);
        }

        System.out.print("¿Es anónimo? (true/false) (default: false): ");
        String anonInput = scanner.nextLine();
        boolean anonimato = !anonInput.isEmpty() && Boolean.parseBoolean(anonInput);
        builder.setAnonimato(anonimato);

        System.out.print("ID contribuyente (default: '1'): ");
        String idContribuyente = scanner.nextLine();
        if (idContribuyente.isEmpty()) idContribuyente = "1";

        builder.setAutor(Integer.valueOf(idContribuyente));

        HechoPostDTO hecho = builder.build();
        try{
            ApiClient.postHecho(hecho, ConnectionManager.getInstance().getServidorLocal("Dinamica"));
            System.out.println("\n=== Hecho creado ===");
            System.out.println("Título: " + hecho.getTitulo());
            System.out.println("Descripción: " + hecho.getDescripcion());
            System.out.println("Categoría: " + hecho.getCategoria().getNombre());
            System.out.println("Ubicación: " + hecho.getUbicacion().getLatitud() + ", " + hecho.getUbicacion().getLongitud());
            System.out.println("Fecha Acontecimiento: " + hecho.getFechaAcontecimiento());
            System.out.println("Texto: " + hecho.getContenidoTexto());
            System.out.println("Multimedia: " + hecho.getContenidoMultimedia().size() + " archivo(s)");
            System.out.println("Anónimo: " + hecho.isAnonimato());
            System.out.println("Contribuyente ID: " + hecho.getContribuyenteId());
        }catch (Exception e){
            throw new RuntimeException("No se pudo enviar el post del hecho: \n" + e.getMessage());
        }
    }
}