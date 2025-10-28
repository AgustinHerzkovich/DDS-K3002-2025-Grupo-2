package aplicacion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Service
public class GeocodingService {

    private final WebClient webClient;
    // Cache de caffeine (pseudo LRU) de 50000 ubicaciones (No debería ser más de 30 MB de ram, estimando entradas de 700 bytes, tirando MUY para arriba)
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();
    public GeocodingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://nominatim.openstreetmap.org")
                .build();
    }

    /**
     * Obtiene la dirección a partir de coordenadas (geocoding inverso)
     * Usa la API gratuita de Nominatim de OpenStreetMap
     */
    public Mono<String> obtenerDireccion(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            return Mono.just("Ubicación desconocida");
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reverse")
                        .queryParam("lat", latitud)
                        .queryParam("lon", longitud)
                        .queryParam("format", "json")
                        .queryParam("addressdetails", 1)
                        .build())
                .header("User-Agent", "MetaMapa/1.0") // Requerido por Nominatim
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    // Intentar obtener el display_name que contiene la dirección completa
                    if (response.has("display_name")) {
                        return response.get("display_name").asText();
                    }

                    // Si no está disponible, construir la dirección desde los componentes
                    if (response.has("address")) {
                        JsonNode address = response.get("address");
                        StringBuilder direccion = new StringBuilder();

                        // Agregar calle y número si están disponibles
                        if (address.has("road")) {
                            direccion.append(address.get("road").asText());
                        }
                        if (address.has("house_number")) {
                            direccion.append(" ").append(address.get("house_number").asText());
                        }

                        // Agregar ciudad
                        if (address.has("city")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("city").asText());
                        } else if (address.has("town")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("town").asText());
                        } else if (address.has("village")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("village").asText());
                        }

                        // Agregar provincia/estado
                        if (address.has("state")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("state").asText());
                        }

                        // Agregar país
                        if (address.has("country")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("country").asText());
                        }

                        return direccion.length() > 0 ? direccion.toString() : "Ubicación desconocida";
                    }

                    return "Ubicación desconocida";
                })
                .onErrorReturn("Ubicación no disponible")
                .defaultIfEmpty("Ubicación desconocida");
    }

    /**
     * Obtiene una dirección corta (ciudad, provincia, país)
     */
//    public Mono<String> obtenerDireccionCorta(Double latitud, Double longitud) {
//        if (latitud == null || longitud == null) {
//            return Mono.just("Ubicación desconocida");
//        }
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/reverse")
//                        .queryParam("lat", latitud)
//                        .queryParam("lon", longitud)
//                        .queryParam("format", "json")
//                        .queryParam("addressdetails", 1)
//                        .build())
//                .header("User-Agent", "MetaMapa/1.0")
//                .retrieve()
//                .bodyToMono(JsonNode.class)
//                .map(response -> {
//                    if (response.has("address")) {
//                        JsonNode address = response.get("address");
//                        StringBuilder direccion = new StringBuilder();
//
//                        // Solo ciudad, provincia y país
//                        if (address.has("city")) {
//                            direccion.append(address.get("city").asText());
//                        } else if (address.has("town")) {
//                            direccion.append(address.get("town").asText());
//                        } else if (address.has("village")) {
//                            direccion.append(address.get("village").asText());
//                        }
//
//                        if (address.has("state")) {
//                            if (direccion.length() > 0) direccion.append(", ");
//                            direccion.append(address.get("state").asText());
//                        }
//
//                        if (address.has("country")) {
//                            if (direccion.length() > 0) direccion.append(", ");
//                            direccion.append(address.get("country").asText());
//                        }
//
//                        return direccion.length() > 0 ? direccion.toString() : "Ubicación desconocida";
//                    }
//
//                    return "Ubicación desconocida";
//                })
//                .onErrorReturn("Ubicación no disponible")
//                .defaultIfEmpty("Ubicación desconocida");
//    }

    public Mono<String> obtenerDireccionCorta(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            return Mono.just("Ubicación desconocida");
        }

        String key = latitud + "," + longitud;

        // Revisar cache primero
        String cached = cache.getIfPresent(key);
        if (cached != null) {
            return Mono.just(cached);
        }

        String url = "https://api.bigdatacloud.net/data/reverse-geocode-client"
                + "?latitude=" + latitud
                + "&longitude=" + longitud
                + "&localityLanguage=es";

        return webClient.get()
                .uri(url)
                .header("User-Agent", "MetaMapa/1.0")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    StringBuilder direccion = new StringBuilder();

                    if (response.has("city") && !response.get("city").asText().isEmpty()) {
                        direccion.append(response.get("city").asText());
                    } else if (response.has("principalSubdivision") && !response.get("principalSubdivision").asText().isEmpty()) {
                        direccion.append(response.get("principalSubdivision").asText());
                    }

                    if (response.has("principalSubdivision") && !response.get("principalSubdivision").asText().isEmpty()) {
                        if (!direccion.isEmpty()) direccion.append(", ");
                        direccion.append(response.get("principalSubdivision").asText());
                    }

                    if (response.has("countryName") && !response.get("countryName").asText().isEmpty()) {
                        if (!direccion.isEmpty()) direccion.append(", ");
                        direccion.append(response.get("countryName").asText());
                    }
                    String result = !direccion.isEmpty() ? direccion.toString() : "Ubicación desconocida";
                    cache.put(key, result); // Guardar en cache
                    return result;
                })
                .onErrorReturn("Ubicación no disponible")
                .defaultIfEmpty("Ubicación desconocida");
    }
}

