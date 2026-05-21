package ec.edu.utpl.carreras.computacion.proava.s6;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;

public class URLExpanderTask implements Callable<String> {

    private final String shortenedUrl;
    private final HttpClient httpClient;
    private static final int TIMEOUT_SECONDS = 5;

    public URLExpanderTask(String shortenedUrl, HttpClient httpClient) {
        this.shortenedUrl = shortenedUrl;
        this.httpClient = httpClient;
    }

    @Override
    public String call() throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(shortenedUrl))
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return request.uri().toString();

        } catch (java.net.http.HttpTimeoutException e) {
            return "TIMEOUT: " + shortenedUrl;
        } catch (java.net.ConnectException e) {
            return "CONNECTION_ERROR: " + shortenedUrl;
        } catch (Exception e) {
            return "ERROR: " + shortenedUrl + " (" + e.getMessage() + ")";
        }
    }
}