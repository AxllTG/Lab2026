package biblioteca.datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import net.datastructures.ProbeHashMap;
import net.datastructures.LinkedPositionalList;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Socio;
import biblioteca.modelo.Prestamo;

public class Dato {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static ProbeHashMap<String, Libro> cargarLibros(String fileName) throws FileNotFoundException {
        ProbeHashMap<String, Libro> libros = new ProbeHashMap<>();
        int numLinea = 0;

        try (Scanner read = new Scanner(new File(fileName))) {
            while (read.hasNextLine()) {
                String linea = read.nextLine();
                numLinea++;
                if (linea.isBlank() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";", -1);

                if (partes.length != 6)
                    throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": se esperaban 6 campos, se encontraron " + partes.length + " -> \"" + linea + "\"");

                String isbn    = partes[0].trim();
                String titulo  = partes[1].trim();
                String autor   = partes[2].trim();
                String genero  = partes[3].trim();

                if (isbn.isEmpty())    throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'isbn' está vacío -> \"" + linea + "\"");
                if (titulo.isEmpty())  throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'titulo' está vacío -> \"" + linea + "\"");
                if (autor.isEmpty())   throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'autor' está vacío -> \"" + linea + "\"");
                if (genero.isEmpty())  throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'genero' está vacío -> \"" + linea + "\"");

                int anio, ejemplares;
                try { anio = Integer.parseInt(partes[4].trim()); }
                catch (NumberFormatException e) { throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": 'anioPublicacion' debe ser un número entero, se encontró: \"" + partes[4].trim() + "\""); }

                try { ejemplares = Integer.parseInt(partes[5].trim()); }
                catch (NumberFormatException e) { throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": 'ejemplaresDisponibles' debe ser un número entero, se encontró: \"" + partes[5].trim() + "\""); }

                libros.put(isbn, new Libro(isbn, titulo, autor, genero, anio, ejemplares));
            }
        }
        return libros;
    }

    public static ProbeHashMap<String, Socio> cargarSocios(String fileName) throws FileNotFoundException {
        ProbeHashMap<String, Socio> socios = new ProbeHashMap<>();
        int numLinea = 0;

        try (Scanner read = new Scanner(new File(fileName))) {
            while (read.hasNextLine()) {
                String linea = read.nextLine();
                numLinea++;
                if (linea.isBlank() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";", -1);

                if (partes.length != 5)
                    throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": se esperaban 5 campos, se encontraron " + partes.length + " -> \"" + linea + "\"");

                String nroSocio = partes[0].trim();
                String nombre   = partes[1].trim();
                String apellido = partes[2].trim();
                String email    = partes[3].trim();
                String activo   = partes[4].trim();

                if (nroSocio.isEmpty()) throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'nroSocio' está vacío -> \"" + linea + "\"");
                if (nombre.isEmpty())   throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'nombre' está vacío -> \"" + linea + "\"");
                if (apellido.isEmpty()) throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'apellido' está vacío -> \"" + linea + "\"");
                if (email.isEmpty())    throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el campo 'email' está vacío -> \"" + linea + "\"");
                if (!activo.equalsIgnoreCase("true") && !activo.equalsIgnoreCase("false"))
                    throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": 'activo' debe ser true o false, se encontró: \"" + activo + "\"");

                socios.put(nroSocio, new Socio(nroSocio, nombre, apellido, email, Boolean.parseBoolean(activo)));
            }
        }
        return socios;
    }

    public static ProbeHashMap<String, LinkedPositionalList<Prestamo>> cargarPrestamos(String fileName,
            ProbeHashMap<String, Socio> socios, ProbeHashMap<String, Libro> libros) throws FileNotFoundException {

        ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos = new ProbeHashMap<>();
        int numLinea = 0;

        try (Scanner read = new Scanner(new File(fileName))) {
            while (read.hasNextLine()) {
                String linea = read.nextLine();
                numLinea++;
                if (linea.isBlank() || linea.startsWith("#")) continue;

                String[] partes = linea.split(";", -1);

                if (partes.length != 4)
                    throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": se esperaban 4 campos, se encontraron " + partes.length + " -> \"" + linea + "\"");

                String nroSocio = partes[0].trim();
                String isbn     = partes[1].trim();

                Socio socio = socios.get(nroSocio);
                Libro libro = libros.get(isbn);

                if (socio == null) throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el socio \"" + nroSocio + "\" no existe");
                if (libro == null) throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": el libro \"" + isbn + "\" no existe");

                LocalDate fechaPrestamo, fechaVencimiento;
                try { fechaPrestamo    = LocalDate.parse(partes[2].trim(), FORMATO_FECHA); }
                catch (DateTimeParseException e) { throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": 'fechaPrestamo' formato incorrecto, se encontró: \"" + partes[2].trim() + "\""); }

                try { fechaVencimiento = LocalDate.parse(partes[3].trim(), FORMATO_FECHA); }
                catch (DateTimeParseException e) { throw new IllegalArgumentException("Archivo: " + fileName + " | Línea " + numLinea + ": 'fechaVencimiento' formato incorrecto, se encontró: \"" + partes[3].trim() + "\""); }

                Prestamo p = new Prestamo(socio, libro, fechaPrestamo, fechaVencimiento);
                LinkedPositionalList<Prestamo> lista = prestamos.get(isbn);
                if (lista == null) { lista = new LinkedPositionalList<>(); prestamos.put(isbn, lista); }
                lista.addLast(p);
            }
        }
        return prestamos;
    }
}