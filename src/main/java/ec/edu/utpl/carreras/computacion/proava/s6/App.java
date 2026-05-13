package ec.edu.utpl.carreras.computacion.proava.s6;

import ec.edu.utpl.carreras.computacion.proava.s6.util.URLExpanderTask;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException{
        var path = Path.of(
                "C:/Users/juani/Downloads/urls.csv"
        );
        var allData = Files.readAllLines(path);
        for (var line : allData) {
            new Thread(()->
                    URLExpanderTask.expand(line).ifPresent(System.out::println)
            ).start();
        }
    }
}
