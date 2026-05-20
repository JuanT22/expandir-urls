package ec.edu.utpl.carreras.computacion.proava.s6.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;

public class URLExpanderTask implements Callable<Optional<String>> {
    private final HttpClient client;
    private final String urlShortened;

    public URLExpanderTask(HttpClient client, String urlShortened) {
        this.client = client;
        this.urlShortened = urlShortened;
    }

    @Override
    public Optional<String> call() throws Exception {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(urlShortened))
                    .timeout(Duration.ofSeconds(5))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = client.send(
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