package ec.edu.utpl.carreras.computacion.proava.s6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.net.http.HttpClient;
import java.time.Duration;

public class App {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        int poolSize = 16;

        System.out.println("[INFO] Leyendo archivo de URLs...");
        long tTotalInicio = System.nanoTime();

        Path path = Path.of("C:/Users/juani/Downloads/urls.csv");
        List<String> urls = Files.readAllLines(path);

        System.out.println("[INFO] URLs cargadas: " + urls.size());
        System.out.println("[INFO] Tamaño del pool: " + poolSize);
        System.out.println("[INFO] Iniciando procesamiento...\n");

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        CountDownLatch startGate = new CountDownLatch(1);

        CountDownLatch endGate = new CountDownLatch(urls.size());

        List<Future<String>> futures = new ArrayList<>();

        for (String url : urls) {
            Future<String> future = executor.submit(() -> {
                try {
                    startGate.await();

                    URLExpanderTask task = new URLExpanderTask(url, CLIENT);
                    return task.call();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "INTERRUPTED: " + url;
                } catch (Exception e) {
                    return "ERROR: " + url + " (" + e.getMessage() + ")";
                } finally {
                    endGate.countDown();
                }
            });

            futures.add(future);
        }

        long tParInicio = System.nanoTime();

        System.out.println("[INFO] Señal de inicio enviada...");
        startGate.countDown();

        System.out.println("[INFO] Esperando que terminen todas las tareas...");
        endGate.await();

        long tParFin = System.nanoTime();

        System.out.println("\n[INFO] Recolectando resultados...");
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < futures.size(); i++) {
            try {
                String result = futures.get(i).get();
                if (result.startsWith("ERROR") || result.startsWith("TIMEOUT") || result.startsWith("CONNECTION")) {
                    errorCount++;
                } else {
                    successCount++;
                }
            } catch (Exception e) {
                errorCount++;
            }
        }

        long tTotalFin = System.nanoTime();

        double tTotal = (tTotalFin - tTotalInicio) / 1_000_000.0;  // ms
        double tPar = (tParFin - tParInicio) / 1_000_000.0;        // ms
        double tSequential = tTotal - tPar;                         // ms (lectura + escritura)

        System.out.println("\n" + "=".repeat(60));
        System.out.println("RESULTADOS");
        System.out.println("=".repeat(60));
        System.out.printf("Tamaño del pool:           %d%n", poolSize);
        System.out.printf("URLs procesadas:           %d%n", urls.size());
        System.out.printf("  - Exitosas:              %d%n", successCount);
        System.out.printf("  - Con error:             %d%n", errorCount);
        System.out.println("-".repeat(60));
        System.out.printf("Tiempo PARALELO:           %.2f ms%n", tPar);
        System.out.printf("Tiempo SECUENCIAL:         %.2f ms%n", tSequential);
        System.out.printf("Tiempo TOTAL:              %.2f ms%n", tTotal);
        System.out.println("-".repeat(60));

        double P = tPar / tTotal;
        System.out.printf("Fracción paralelizable P:  %.4f%n", P);
        System.out.printf("Fracción secuencial (1-P): %.4f%n", 1 - P);
        System.out.println("=".repeat(60));

        executor.shutdown();
        System.out.println("\n[INFO] ExecutorService cerrado. ¡Proceso completado!");
    }
}
