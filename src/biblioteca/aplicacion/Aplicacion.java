package biblioteca.aplicacion;

import java.io.IOException;
import java.time.LocalDate;

import net.datastructures.LinkedPositionalList;
import net.datastructures.ProbeHashMap;
import biblioteca.datos.CargarParametros;
import biblioteca.datos.Dato;
import biblioteca.interfaz.Interfaz;
import biblioteca.logica.Logica;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Socio;

public class Aplicacion {

	public static void main(String[] args) {

		// 1. Cargar parámetros de configuración
		try {
			CargarParametros.parametros();
		} catch (IOException e) {
			System.err.println("Error al cargar config.properties");
			System.exit(-1);
		}

		// 2. Cargar datos desde archivos
		ProbeHashMap<String, Libro> catalogo = null;
		ProbeHashMap<String, Socio> socios = null;
		ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos = null;

		try {
			catalogo = Dato.cargarLibros(CargarParametros.getArchivoLibros());
			socios = Dato.cargarSocios(CargarParametros.getArchivoSocios());
			prestamos = Dato.cargarPrestamos(CargarParametros.getArchivoPrestamos(), socios, catalogo);
		} catch (Exception e) {
			System.err.println("Error al cargar archivos de datos: " + e.getMessage());
			System.exit(-1);
		}

		// 3. Inicializar capa lógica
		Logica logica = new Logica(catalogo, socios, prestamos);

		// 4. Ciclo principal de la aplicación
		int opcion;
		do {
			opcion = Interfaz.menu();

			switch (opcion) {
			case Constante.OPCION_PRESTAR: {
				// TODO: pedir datos al usuario y llamar a logica.prestar(...)
				String isbn = Interfaz.pedirIsbn();
				String nroSocio = Interfaz.pedirNroSocio();

				Socio socio = socios.get(nroSocio); // necesitás acceso al mapa, o crear métodos en Logica
				Libro libro = logica.buscarPorIsbn(isbn);

				if (libro == null) {
					Interfaz.mostrarError("El libro no existe.");
				} else if (socio == null) {
					Interfaz.mostrarError("El socio no existe.");
				} else if (!socio.isActivo()) {
					Interfaz.mostrarError("El socio " + nroSocio + " no está activo.");
				} else if(libro.getEjemplaresDisponibles() == 0) {
					logica.prestar(nroSocio, isbn);
					Interfaz.mostrarMensaje("No hay ejemplares disponibles. El socio fue agregado a la lista de espera.");
				} else if(logica.prestar(nroSocio, isbn)) {
					Interfaz.mostrarMensaje("Presamo registrado correctamente.");
				} else {
					Interfaz.mostrarError("No se pudo registrar prestamo.");
				}
				break;
			}
			case Constante.OPCION_DEVOLVER: {
				// TODO: pedir datos al usuario y llamar a logica.devolver(...)
				String isbn = Interfaz.pedirIsbn();
				String nroSocio = Interfaz.pedirNroSocio();
				if (logica.devolver(nroSocio, isbn)) {
					Interfaz.mostrarMensaje("Devolucion registrada correctamente. ");
					// Si hay socios en espera, se le asigna el libro al primero
					Socio asignado = logica.asignarSiguienteEnEspera(isbn);
					if (asignado != null)
						Interfaz.mostrarAsignacionEspera(isbn, asignado);
				} else {
					Interfaz.mostrarError("No se encontro un prestamo activo para ese socio y libro.");
				}
				break;
			}
			case Constante.OPCION_BUSCAR_ISBN: {
				// TODO: pedir ISBN y mostrar resultado de logica.buscarPorIsbn(...)
				String isbn = Interfaz.pedirIsbn();
				Libro libro = logica.buscarPorIsbn(isbn);
				Interfaz.mostrarLibro(libro);
				break;
			}
			case Constante.OPCION_BUSCAR_TITULO: {
				// TODO: pedir título y mostrar resultados de logica.buscarPorTitulo(...)
				String titulo = Interfaz.pedirTitulo();
				LinkedPositionalList<Libro> porTitulo = logica.buscarPorTitulo(titulo);
				Interfaz.mostrarListaLibros(porTitulo);
				break;
			}
			case Constante.OPCION_BUSCAR_AUTOR: {
				// TODO: pedir autor y mostrar resultados de logica.buscarPorAutor(...)
				String autor = Interfaz.pedirAutor();
				LinkedPositionalList<Libro> porAutor = logica.buscarPorAutor(autor);
				Interfaz.mostrarListaLibros(porAutor);
				break;
			}
			case Constante.OPCION_DISPONIBLES: {
				// TODO: mostrar resultado de logica.listarDisponibles()
				LinkedPositionalList<Libro> disponibles = logica.listarDisponibles();
				Interfaz.mostrarListaLibros(disponibles);
				break;
			}
			case Constante.OPCION_PRESTAMOS_SOCIO: {
				// TODO: pedir nroSocio y mostrar logica.prestamosActivosDeSocio(...)
				String nroSocio = Interfaz.pedirNroSocio();
				LinkedPositionalList<Prestamo> activos = logica.prestamosActivosDeSocio(nroSocio);
				Interfaz.mostrarListaPrestamos(activos);
				break;
			}
			case Constante.OPCION_HISTORIAL: {
				// TODO: pedir nroSocio y mostrar logica.historialDeSocio(...)
				String nroSocio = Interfaz.pedirNroSocio();
				LinkedPositionalList<Prestamo> historial = logica.historialDeSocio(nroSocio);
				Interfaz.mostrarListaPrestamos(historial);
				break;
			}
			case Constante.OPCION_RANKING: {
				// TODO: pedir N y mostrar logica.librosMasSolicitados(N)
				Integer n = Interfaz.pedirN();
				LinkedPositionalList<Libro> ranking = logica.librosMasSolicitados(n);
				Interfaz.mostrarListaLibros(ranking);
				break;
			}
			case Constante.OPCION_VENCIDOS: {
				// TODO: pedir fecha con Interfaz.pedirFecha(...) y mostrar
				// logica.prestamosVencidos(LocalDate)
				LocalDate hoy = LocalDate.now();
				LinkedPositionalList<Prestamo> vencido = logica.prestamosVencidos(hoy);
				Interfaz.mostrarListaPrestamos(vencido);
				break;
			}
			case Constante.OPCION_SALIR:
				Interfaz.mostrarMensaje("Hasta luego.");
				break;

			default:
				Interfaz.mostrarError("Opción no válida.");
			}

		} while (opcion != Constante.OPCION_SALIR);

	}
}
