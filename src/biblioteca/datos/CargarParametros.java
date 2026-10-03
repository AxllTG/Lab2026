package biblioteca.datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CargarParametros {

    private static String archivoLibros;
    private static String archivoSocios;
    private static String archivoPrestamos;

    public static void parametros() throws IOException {
        System.out.println("Buscando config en: " 
                + new java.io.File("config.properties").getAbsolutePath());
        Properties prop = new Properties();
        InputStream input = new FileInputStream("config.properties");
        prop.load(input);
        archivoLibros    = prop.getProperty("libros");
        archivoSocios    = prop.getProperty("socios");
        archivoPrestamos = prop.getProperty("prestamos");
    }

    public static String getArchivoLibros() {
        return archivoLibros;
    }

    public static String getArchivoSocios() {
        return archivoSocios;
    }

    public static String getArchivoPrestamos() {
        return archivoPrestamos;
    }
}
