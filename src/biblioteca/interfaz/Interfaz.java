package biblioteca.interfaz;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import biblioteca.aplicacion.Constante;
import biblioteca.datos.Dato;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Socio;

public class Interfaz {

	private static final Scanner SC = new Scanner(System.in);
	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Muestra el menú principal y retorna la opción elegida por el usuario.
	 */
	public static int menu() {
		System.out.println("\n===== SISTEMA DE GESTIÓN DE BIBLIOTECA =====");
		System.out.println(Constante.OPCION_PRESTAR + ". Registrar préstamo");
		System.out.println(Constante.OPCION_DEVOLVER + ". Registrar devolución");
		System.out.println(Constante.OPCION_BUSCAR_ISBN + ". Buscar libro por ISBN");
		System.out.println(Constante.OPCION_BUSCAR_TITULO + ". Buscar libro por título");
		System.out.println(Constante.OPCION_BUSCAR_AUTOR + ". Buscar libro por autor");
		System.out.println(Constante.OPCION_DISPONIBLES + ". Listar libros disponibles");
		System.out.println(Constante.OPCION_PRESTAMOS_SOCIO + ". Ver préstamos activos de un socio");
		// System.out.println("---- Incremento 2 ----");
		System.out.println(Constante.OPCION_HISTORIAL + ". Ver historial de un socio");
		System.out.println(Constante.OPCION_RANKING + ". Libros más solicitados");
		System.out.println(Constante.OPCION_VENCIDOS + ". Préstamos vencidos");
		System.out.println(Constante.OPCION_SALIR + ". Salir");
		System.out.print("Ingrese una opción: ");

		// TODO: validar que la entrada sea un número dentro del rango válido
		while (true) {
			String linea = SC.nextLine().trim();
			try {
				int opcion = Integer.parseInt(linea);
				if (opcion == Constante.OPCION_SALIR
						|| (opcion >= Constante.OPCION_PRESTAR && opcion <= Constante.OPCION_VENCIDOS)) {
					return opcion;
				}
				System.out.println("Opcion fuera de rango: Ingrese una opcion valida: ");
			} catch (NumberFormatException e) {
				System.out.println("Entrada  invalida. Ingrese un numero: ");
			}
		}

	}

	public static String pedirIsbn() {
		System.out.print("Ingrese ISBN: ");
		// TODO: implementar
		return SC.nextLine().trim();
	}

	public static String pedirNroSocio() {
		System.out.print("Ingrese número de socio: ");
		// TODO: implementar
		return SC.nextLine().trim();
	}

	public static String pedirTitulo() {
		System.out.print("Ingrese título (o parte del título): ");
		// TODO: implementar
		return SC.nextLine().trim();
	}

	public static String pedirAutor() {
		System.out.print("Ingrese nombre del autor: ");
		// TODO: implementar
		return SC.nextLine().trim();
	}

	public static int pedirN() {
		System.out.print("Ingrese cantidad de libros a mostrar: ");
		// TODO: implementar
		while (true) {
			String linea = SC.nextLine().trim();
			try {
				int n = Integer.parseInt(linea);
				if (n > 0)
					return n;
				System.out.println("Debe ser un numero mayor a 0: ");
			} catch (NumberFormatException e) {
				System.out.println("Entrada invalida. Ingrese un numero entero: ");
			}

		}
	}

	/**
	 * Solicita una fecha al usuario en formato dd/MM/yyyy y la retorna como
	 * LocalDate. Debe validar el formato antes de retornar.
	 */
	public static LocalDate pedirFecha(String etiqueta) {
		System.out.print("Ingrese " + etiqueta + " (dd/MM/yyyy): ");
		// TODO: implementar y validar formato usando DateTimeFormatter FMT
		while (true) {
			String linea = SC.nextLine().trim();
			try {
				return LocalDate.parse(linea, FMT);
			} catch (DateTimeException e) {
				System.out.println("Formato invalido. Ingrese " + etiqueta + "(dd/MM/yyyy) ");
			}
		}
	}

	// ── Métodos de presentación de resultados ──

	public static void mostrarLibro(Libro libro) {
		// TODO: implementar
		if (libro == null) {
			mostrarError("El libro no existe");
			return;
		}
		System.out.println(" ISBN : " + libro.getIsbn());
		System.out.println(" Titulo: " + libro.getTitulo());
		System.out.println(" Autor : " + libro.getAutor());
		System.out.println(" Disponibilidad: " + (libro.isDisponible() ? "Si" : "No"));
	}

	public static void mostrarListaLibros(Iterable<Libro> libros) {
		// TODO: implementar
		boolean hayLibros = false;
		System.out.println();
		for (Libro libro : libros) {
			System.out.println("[ " + libro.getIsbn() + "] " + libro.getTitulo() + " - " + libro.getAutor()
					+ "| Diponibilidad: " + (libro.getEjemplaresDisponibles()));
			hayLibros = true;
		}
		if (!hayLibros) {
			System.out.println("(No se encontraron libros)");
		}
		System.out.println();
	}

	public static void mostrarListaPrestamos(Iterable<Prestamo> prestamos) {
		// TODO: implementar
		boolean hayPrestamos = false;
		for (Prestamo p : prestamos) {
			System.out.println(" Socio: " + p.getSocio().getNroSocio() + "-" + p.getSocio().getNombre() + " "
					+ p.getSocio().getApellido());
			System.out.println(" Libro: [" + p.getLibro().getIsbn() + "] " + p.getLibro().getTitulo());
			System.out.println(" Vence: " + p.getFechaVencimiento().format(FMT));
			System.out.println(" Estado: " + (p.isActivo() ? "Activo" : "Devuelto"));
			System.out.println();
			hayPrestamos = true;
		}
		if (!hayPrestamos) {
			System.out.println(" (No hay prestamos para mostrar.)");
		}
		System.out.println();
	}
	public static void mostrarAsignacionEspera(String isbn, Socio socio) {
		System.out.println("Se asigno el libro [" + isbn + "] a: " + socio.getNombre() + " " + socio.getApellido()
				+ " (Socio: " + socio.getNroSocio() + ")");
	}
	
		public static void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}

	public static void mostrarError(String mensaje) {
		System.err.println("ERROR: " + mensaje);
	}
	
	
}
