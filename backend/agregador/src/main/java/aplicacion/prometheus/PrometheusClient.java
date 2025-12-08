package aplicacion.prometheus;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class PrometheusClient {

    private final String prometheusUrl = "http://localhost:9090"; // Cambiá si es necesario

    public String query(String promql) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = prometheusUrl + "/api/v1/query?query=" + URLEncoder.encode(promql, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
