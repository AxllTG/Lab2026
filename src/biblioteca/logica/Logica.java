package biblioteca.logica;

import java.time.LocalDate;

import net.datastructures.ProbeHashMap;
import net.datastructures.LinkedPositionalList;
import net.datastructures.LinkedQueue;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Socio;
import biblioteca.modelo.Prestamo;

public class Logica {

	private ProbeHashMap<String, Libro> catalogo;
	private ProbeHashMap<String, Socio> socios;
	// TODO: definir las estructuras adicionales que necesite
	// Pensar: ¿dónde guardar los préstamos activos?
	// Pensar: ¿cómo modelar la lista de espera por libro?
	// Pensar: ¿dónde guardar el historial de préstamos por socio?
	private ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamosActivos;
	private ProbeHashMap<String, LinkedPositionalList<Prestamo>> historialPorSocio;
	private ProbeHashMap<String, LinkedQueue<Socio>> colaEspera;

	public Logica(ProbeHashMap<String, Libro> catalogo, ProbeHashMap<String, Socio> socios,
			ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamosActivos) {
		this.catalogo = catalogo;
		this.socios = socios;
		// TODO: inicializar las estructuras internas a partir de los datos recibidos
		this.prestamosActivos = prestamosActivos;

		// ── INCREMENTO 2 ──────────────────────────────────────────────
		this.historialPorSocio = new ProbeHashMap<>();
		this.colaEspera = new ProbeHashMap<>();

		// Inicializar historial a partir de los prestamos ya cargados
		for (String isbn : prestamosActivos.keySet()) {
			for (Prestamo p : prestamosActivos.get(isbn)) {
				String nroSocio = p.getSocio().getNroSocio();
				LinkedPositionalList<Prestamo> historial = historialPorSocio.get(nroSocio);
				if (historial == null) {
					historial = new LinkedPositionalList<>();
					historialPorSocio.put(nroSocio, historial);
				}

				historial.addLast(p);

			}

		}

	}

	// ── INCREMENTO 1 ──────────────────────────────────────────────

	/**
	 * Registra el préstamo de un libro a un socio. La fecha de préstamo es la fecha
	 * actual y el vencimiento se calcula automáticamente (14 días). Condiciones: el
	 * socio debe estar activo y debe haber ejemplares disponibles.
	 * 
	 * @return true si el préstamo se realizó, false en caso contrario
	 */
	public boolean prestar(String nroSocio, String isbn) {
		// TODO: implementar
		Socio socio = socios.get(nroSocio);
		Libro libro = catalogo.get(isbn);
		// Verifica que socio y el libro exitan
		if (socio == null || libro == null)
			return false;
		// verifica que el socio este activo
		if (!socio.isActivo())
			return false;
		// verifica que haya ejemplares disponibles
		if (libro.getEjemplaresDisponibles() <= 0) {
			agregarEspera(nroSocio, isbn);
			return false;
		}

		LocalDate hoy = LocalDate.now();
		LocalDate vencimiento = hoy.plusDays(14);
		Prestamo prestamo = new Prestamo(socio, libro, hoy, vencimiento);

		libro.prestar();// inicializa el metodo prestar...

		LinkedPositionalList<Prestamo> lista = prestamosActivos.get(isbn);
		if (lista == null) {
			lista = new LinkedPositionalList<>();
			prestamosActivos.put(isbn, lista);
		}
		lista.addLast(prestamo);

		// historial del socio
		LinkedPositionalList<Prestamo> historial = historialPorSocio.get(nroSocio);
		if (historial == null) {
			historial = new LinkedPositionalList<>();
			historialPorSocio.put(nroSocio, historial);
		}
		historial.addLast(prestamo);
		return true;
	}

	/**
	 * Registra la devolución de un libro. Actualiza el estado del préstamo y la
	 * disponibilidad del libro.
	 * 
	 * @return true si la devolución se realizó, false en caso contrario
	 */
	public boolean devolver(String nroSocio, String isbn) {
		LinkedPositionalList<Prestamo> lista = prestamosActivos.get(isbn);
		if (lista == null)
			return false;

		for (Prestamo p : lista) {
			if (p.getSocio().getNroSocio().equals(nroSocio) && p.isActivo()) {
				p.setActivo(false);
				catalogo.get(isbn).devolver();

				asignarSiguienteEnEspera(isbn);
				return true;
			}
		}
		return false;
	}

	/**
	 * Busca un libro por su ISBN.
	 * 
	 * @return el Libro encontrado, o null si no existe
	 */
	public Libro buscarPorIsbn(String isbn) {
		// TODO: implementar
		return catalogo.get(isbn);

	}

	/**
	 * Busca libros cuyo título contenga la cadena indicada (sin distinguir
	 * mayúsculas).
	 */
	public LinkedPositionalList<Libro> buscarPorTitulo(String titulo) {
		// TODO: implementar
		LinkedPositionalList<Libro> resultado = new LinkedPositionalList<>();
		String buscar = titulo.toLowerCase();
		for (Libro libro : catalogo.values()) {
			if (libro.getTitulo().toLowerCase().contains(buscar))
				resultado.addLast(libro);
		}
		return resultado;
	}

	/**
	 * Busca libros de un autor dado (sin distinguir mayúsculas).
	 */
	public LinkedPositionalList<Libro> buscarPorAutor(String autor) {
		// TODO: implementar
		LinkedPositionalList<Libro> resultado = new LinkedPositionalList<>();
		String buscar = autor.toLowerCase();
		for (Libro libro : catalogo.values()) {
			if (libro.getAutor().toLowerCase().contains(buscar))
				resultado.addLast(libro);
		}
		return resultado;
	}

	/**
	 * Retorna todos los libros con al menos un ejemplar disponible.
	 */
	public LinkedPositionalList<Libro> listarDisponibles() {
		// TODO: implementar
		LinkedPositionalList<Libro> resultado = new LinkedPositionalList<>();
		for (Libro libro : catalogo.values()) {
			if (libro.getEjemplaresDisponibles() > 0)
				resultado.addLast(libro);
		}
		return resultado;
	}

	/**
	 * Retorna los préstamos activos de un socio.
	 */
	public LinkedPositionalList<Prestamo> prestamosActivosDeSocio(String nroSocio) {
		// TODO: implementar
		LinkedPositionalList<Prestamo> resultado = new LinkedPositionalList<>();
		for (LinkedPositionalList<Prestamo> lista : prestamosActivos.values()) {
			for (Prestamo p : lista) {
				if (p.getSocio().getNroSocio().equals(nroSocio) && p.isActivo()) {
					resultado.addLast(p);
				}
			}
		}
		return resultado;
	}

	// ── INCREMENTO 2 ──────────────────────────────────────────────

	/**
	 * Agrega un socio a la cola de espera de un libro. Se invoca cuando no hay
	 * ejemplares disponibles al momento del pedido.
	 */
	public void agregarEspera(String nroSocio, String isbn) {
		// TODO: implementar
		Socio socio = socios.get(nroSocio);
		if (socio == null)
			return;

		LinkedQueue<Socio> cola = colaEspera.get(isbn);
		if (cola == null) {
			cola = new LinkedQueue<>();
			colaEspera.put(isbn, cola);
		}
		cola.enqueue(socio);
	}

	/**
	 * Al devolver un libro, si hay socios en espera, asigna el ejemplar
	 * automáticamente al primero en la cola y lo notifica.
	 */
	public Socio asignarSiguienteEnEspera(String isbn) {
		LinkedQueue<Socio> cola = colaEspera.get(isbn);
		if (cola == null || cola.isEmpty())
			return null;

		Socio siguiente = cola.dequeue();
		if (prestar(siguiente.getNroSocio(), isbn)) {
			return siguiente;
		}
		// por si el socio esta inactivo, reintentamos con el siguiente de la cola.
		return asignarSiguienteEnEspera(isbn);
	}

	/**
	 * Retorna el historial completo de préstamos de un socio (activos e
	 * históricos), en orden cronológico.
	 */
	public LinkedPositionalList<Prestamo> historialDeSocio(String nroSocio) {
		// TODO: implementar
		LinkedPositionalList<Prestamo> historial = historialPorSocio.get(nroSocio);
		if (historial == null) {
			return new LinkedPositionalList<>();
		}
		return historial;
	}

	/**
	 * Retorna los N libros más solicitados (préstamos activos + históricos).
	 * 
	 * @param n cantidad de libros a retornar
	 */
	public LinkedPositionalList<Libro> librosMasSolicitados(int n) {
		// Contar cuántas veces se prestó cada libro (activos + históricos)
		ProbeHashMap<String, Integer> conteo = new ProbeHashMap<>();
		for (LinkedPositionalList<Prestamo> historial : historialPorSocio.values()) {
			for (Prestamo p : historial) {
				String isbn = p.getLibro().getIsbn();
				Integer actual = conteo.get(isbn);
				conteo.put(isbn, actual == null ? 1 : actual + 1);
			}
		}

		// Selección de los N más solicitados (selection sort parcial)
		// Construimos arrays auxiliares para poder marcar ya seleccionados
		LinkedPositionalList<String> isbnsProcesados = new LinkedPositionalList<>();
		LinkedPositionalList<Libro> resultado = new LinkedPositionalList<>();

		int seleccionados = 0;
		while (seleccionados < n) {
			String maxIsbn = null;
			int maxCount = -1;

			for (String isbn : conteo.keySet()) {
				// Verificar que no fue ya seleccionado
				boolean yaEsta = false;
				for (String sel : isbnsProcesados) {
					if (sel.equals(isbn)) {
						yaEsta = true;
						break;
					}
				}
				if (yaEsta)
					continue;

				int c = conteo.get(isbn);
				if (c > maxCount) {
					maxCount = c;
					maxIsbn = isbn;
				}
			}

			if (maxIsbn == null)
				break; // no hay más libros
			isbnsProcesados.addLast(maxIsbn);
			resultado.addLast(catalogo.get(maxIsbn));
			seleccionados++;
		}

		return resultado;
	}

	/**
	 * Retorna todos los préstamos cuya fecha de vencimiento expiró y que aún no
	 * fueron devueltos.
	 * 
	 * @param hoy fecha actual
	 */
	public LinkedPositionalList<Prestamo> prestamosVencidos(LocalDate hoy) {
		// TODO: implementar
		LinkedPositionalList<Prestamo> resultado = new LinkedPositionalList<>();
		for (LinkedPositionalList<Prestamo> lista : prestamosActivos.values()) {
			for (Prestamo p : lista) {
				if (p.estaVencido(hoy))
					resultado.addLast(p);
			}

		}

		return resultado;
	}
}
