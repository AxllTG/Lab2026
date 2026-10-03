package biblioteca.modelo;

import java.time.LocalDate;

public class Prestamo {

    private Socio     socio;
    private Libro     libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private boolean   activo;

    public Prestamo(Socio socio, Libro libro,
                    LocalDate fechaPrestamo, LocalDate fechaVencimiento) {
        // TODO: inicializar atributos
        // Nota: al crear un préstamo, activo debe ser true
    		this.socio = socio;
    		this.libro = libro;
    		this.fechaPrestamo = fechaPrestamo;
    		this.fechaVencimiento = fechaVencimiento;
    		this.activo = true;
    }

    // TODO: getters y setters
    public Socio getSocio() {return socio;}

	public Libro getLibro() {return libro;}

	public LocalDate getFechaPrestamo() {return fechaPrestamo;}

	public LocalDate getFechaVencimiento() {return fechaVencimiento;}

	public boolean isActivo() {return activo;}
    
    public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
     * Retorna true si el préstamo está activo y la fecha de vencimiento
     * es anterior a la fecha indicada.
     */
    public boolean estaVencido(LocalDate hoy) {
        // TODO: implementar
        return activo && fechaVencimiento.isBefore(hoy);
    }
	@Override
	public String toString() {
		return "Prestamo [socio=" + socio + ", libro=" + libro + ", fechaPrestamo=" + fechaPrestamo
				+ ", fechaVencimiento=" + fechaVencimiento + ", activo=" + activo + "]";
	}
}
