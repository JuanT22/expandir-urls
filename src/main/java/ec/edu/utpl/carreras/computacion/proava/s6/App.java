package ec.edu.utpl.carreras.computacion.proava.s6;

import ec.edu.utpl.carreras.computacion.proava.s6.util.URLExpanderTask;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {
//v1
    public static void main(String[] args) {
        URLExpanderTask urlExpanderTask = new URLExpanderTask();
        Optional<String> expandedUrl = urlExpanderTask.expand("https://t.co/TGqER33gn7");
        expandedUrl.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No se puede leer la URL")
        );
    }
}