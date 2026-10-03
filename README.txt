=====================================================
  LABORATORIO 2026 - SISTEMA DE GESTIÓN DE BIBLIOTECA
  Algorítmica y Programación II
=====================================================

ESTRUCTURA DEL PROYECTO
------------------------
biblioteca/
  aplicacion/
    Aplicacion.java     -> Punto de entrada (main). Orquesta las capas.
    Constante.java      -> Constantes de opciones del menú. YA IMPLEMENTADO.
  datos/
    CargarParametros.java -> Lee config.properties. YA IMPLEMENTADO.
    Dato.java           -> Carga libros, socios y préstamos desde archivos.
  interfaz/
    Interfaz.java       -> Menú de texto e interacción con el usuario.
  logica/
    Logica.java         -> Toda la lógica del sistema (búsquedas, préstamos, etc.)
  modelo/
    Libro.java          -> Entidad libro.
    Socio.java          -> Entidad socio.
    Prestamo.java       -> Entidad préstamo.

net/datastructures/     -> Librería del libro Goodrich & Tamassia. NO MODIFICAR.

config.properties       -> Rutas de los archivos de datos.
libros.txt              -> Datos de ejemplo de libros.
socios.txt              -> Datos de ejemplo de socios.
prestamos.txt           -> Datos de ejemplo de préstamos activos.


FORMATO DE LOS ARCHIVOS DE DATOS
---------------------------------
libros.txt     -> isbn;titulo;autor;genero;anio;ejemplaresDisponibles
socios.txt     -> nroSocio;nombre;apellido;email;activo
prestamos.txt  -> nroSocio;isbn;fechaPrestamo;fechaVencimiento
                  (solo préstamos activos al inicio del sistema)

Formato de fechas: dd/mm/aaaa


TAREAS A IMPLEMENTAR (buscar TODO en el código)
-----------------------------------------------
INCREMENTO 1:
  [ ] Libro.java          -> constructor, getters/setters, toString, equals
  [ ] Socio.java          -> constructor, getters/setters, toString, equals
  [ ] Prestamo.java       -> constructor, getters/setters, toString
  [ ] Dato.java           -> cargarLibros(), cargarSocios(), cargarPrestamos()
  [ ] Logica.java         -> prestar(), devolver(), buscarPorIsbn(),
                             buscarPorTitulo(), buscarPorAutor(),
                             listarDisponibles(), prestamosActivosDeSocio()
  [ ] Interfaz.java       -> menu(), pedirIsbn(), pedirNroSocio(), etc.
  [ ] Aplicacion.java     -> completar el switch con las opciones del incremento 1

INCREMENTO 2:
  [ ] Logica.java         -> agregarEspera(), asignarSiguienteEnEspera(),
                             historialDeSocio(), librosMasSolicitados(),
                             prestamosVencidos()
  [ ] Aplicacion.java     -> completar el switch con las opciones del incremento 2


NOTA IMPORTANTE
---------------
La clase Logica.java es el corazón del sistema. Antes de codificar,
analice qué estructuras de datos de net.datastructures son las más
adecuadas para cada operación y justifíquelas en la documentación.
=====================================================
