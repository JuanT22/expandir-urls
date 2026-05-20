package ec.edu.utpl.carreras.computacion.proava.s6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Future;

/**
 * Hello world!
 */
public class App {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    public static void main(String[] args) throws IOException{
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Optional<String>>> futures = new ArrayList<>();

        var path = Path.of(
                "C:/Users/juani/Downloads/urls.csv"
        );
        var allData = Files.readAllLines(path);
        for (var line : allData) {

        }
    }
}
