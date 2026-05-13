package ec.edu.utpl.carreras.computacion.proava.s6.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class URLExpanderTask {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();

    public static Optional<String> expand(String urlShortened) {
        try {
            var request = HttpRequest.newBuilder()
                .uri(URI.create(urlShortened))
                .timeout(Duration.ofSeconds(5))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<Void> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.discarding()
            );
            return switch (response.statusCode()) {
                case 200 -> Optional.of(response.uri().toString());
                default -> Optional.empty();
            };
        } catch (Exception _) {
            return Optional.empty();
        }
    }
}
