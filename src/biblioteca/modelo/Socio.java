package biblioteca.modelo;

import java.util.Objects;

public class Socio {

    private String nroSocio;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;

    public Socio(String nroSocio, String nombre, String apellido,
                 String email, boolean activo) {
        // TODO: inicializar atributos
    		this.nroSocio = nroSocio;
    		this.nombre = nombre;
    		this.apellido = apellido;
    		this.email = email;
    		this.activo = activo;
    }

    // TODO: getters y setters

    public String getNroSocio() {return nroSocio;}

	public String getNombre() {return nombre;}

	public String getApellido() {return apellido;}

	public String getEmail() {return email;}

	public boolean isActivo() {return activo;}

	@Override
    public String toString() {
        // TODO: implementar
        return "Socio = [" + "NroSocio: " + nroSocio + ", Nombre: " + nombre + ", Apellido: " + apellido
        		+ ", email: " + email + ", Activo: " + activo;
    }

    @Override
	public int hashCode() {
		return Objects.hash(nroSocio);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: implementar - dos socios son iguales si tienen el mismo nroSocio
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Socio other = (Socio) obj;
		return Objects.equals(nroSocio, other.nroSocio);
	}
}
