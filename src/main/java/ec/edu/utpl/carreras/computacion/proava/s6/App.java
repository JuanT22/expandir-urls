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

    public static void main(String[] args) {
        var path = Path.of(
            "C:/Users/juani/Downloads/urls.csv"
        );
        try (var lines = Files.lines(path)) {
            lines
                    .filter(line -> !line.isBlank()) //Eliminar urls vacias. List<String>
                    .map(String::trim) //Elmininar espacios en blanco en una url. List<String>
                    .map(URLExpanderTask::expand) //Transformar la url en su version expandida. List<Optinnal<Striing>>
                    .filter(opt -> opt.isPresent()) //Eliminar urls que no pudieron expandinrse.
                    .map(Optional::get)//Obtener la url expandida. List<String>
                    .forEach(System.out::println);
        } catch (IOException e) {}
    }
}
