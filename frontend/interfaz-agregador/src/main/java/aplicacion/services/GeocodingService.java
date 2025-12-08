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
            .expireAfterAccess(7, TimeUnit.DAYS)
            .build();
    private final Cache<String, String> cacheChiquito = Caffeine.newBuilder()
            .maximumSize(50000)
            .expireAfterAccess(7, TimeUnit.DAYS)
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
    public String obtenerDireccion(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            return Mono.just("Ubicación desconocida").block();
        }
        String direccionCacheada = cache.getIfPresent(latitud + "|" + longitud);
        if(direccionCacheada != null) {
            return direccionCacheada;
        }
        direccionCacheada = webClient.get()
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
                .defaultIfEmpty("Ubicación desconocida").block();
        cache.put(latitud + "|" + longitud, direccionCacheada);
        return direccionCacheada;
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
    public String obtenerDireccionCorta(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            return Mono.just("Ubicación desconocida").block();
        }
        String direccionCacheada = cacheChiquito.getIfPresent(latitud + "|" + longitud);
        if(direccionCacheada != null){
            return direccionCacheada;
        }
        direccionCacheada = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reverse")
                        .queryParam("lat", latitud)
                        .queryParam("lon", longitud)
                        .queryParam("format", "json")
                        .queryParam("addressdetails", 1)
                        .build())
                .header("User-Agent", "MetaMapa/1.0")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    if (response.has("address")) {
                        JsonNode address = response.get("address");
                        StringBuilder direccion = new StringBuilder();

                        if (address.has("city")) {
                            direccion.append(address.get("city").asText());
                        } else if (address.has("town")) {
                            direccion.append(address.get("town").asText());
                        } else if (address.has("village")) {
                            direccion.append(address.get("village").asText());
                        }

                        if (address.has("state")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("state").asText());
                        }

                        if (address.has("country")) {
                            if (direccion.length() > 0) direccion.append(", ");
                            direccion.append(address.get("country").asText());
                        }

                        return direccion.length() > 0 ? direccion.toString() : "Ubicación desconocida";
                    }

                    return "Ubicación desconocida";
                })
                .onErrorReturn("Ubicación no disponible")
                .defaultIfEmpty("Ubicación desconocida").block();
        cacheChiquito.put(latitud + "|" + longitud, direccionCacheada);
        return direccionCacheada;
    }
}

