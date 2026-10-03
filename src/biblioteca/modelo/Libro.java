package biblioteca.modelo;

import java.util.Objects;

public class Libro {

	private String isbn;
	private String titulo;
	private String autor;
	private String genero;
	private int anioPublicacion;
	private int ejemplaresDisponibles;

	public Libro(String isbn, String titulo, String autor, String genero, int anioPublicacion,
			int ejemplaresDisponibles) {
		// TODO: inicializar atributos
		this.isbn = isbn;
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.anioPublicacion = anioPublicacion;
		this.ejemplaresDisponibles = ejemplaresDisponibles;
	}

	
	
	// TODO: getters y setters
	public String getIsbn() {return isbn;}

	public String getTitulo() {return titulo;}

	public String getAutor() {return autor;}

	public String getGenero() {return genero;}

	public int getAnioPublicacion() {return anioPublicacion;}

	public int getEjemplaresDisponibles() {return ejemplaresDisponibles;}
	

	@Override
	public String toString() {
		// TODO: implementar
		return "Libro = [ ISBN: " + isbn + ", TITULO: " + titulo + ", AUTOR: " + autor + ", GENERO: "
				+ genero + ", AÑO DE PUBLICACION: " + anioPublicacion + ", DISPONIBLES: " + ejemplaresDisponibles + "] ";
	}
	//verifica que halla libro
	 public boolean isDisponible() {
	        return ejemplaresDisponibles > 0;
	    }
	 
	 public void prestar() {
	        if (ejemplaresDisponibles > 0) ejemplaresDisponibles--;
	    }
	   public void devolver() {
	        ejemplaresDisponibles++;
	    }
	
	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: implementar - dos libros son iguales si tienen el mismo ISBN
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return Objects.equals(isbn, other.isbn);
	}
	
}
