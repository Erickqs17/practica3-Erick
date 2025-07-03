import java.util.*;

// --- Patrón Observer ---
// El patrón Observer permite que varios objetos estén suscritos a otro objeto 
// y reciban notificaciones automáticas cuando ese objeto cambia su estado.
// Aquí en el ejemplo el  Tutor y Estudiante son Observadores que reciben notificaciones cuando cambia el estado de la tutoría segun propuesta
interface Observador {
    void actualizar(String mensaje);
}

class Tutor implements Observador {
    private String nombre;
    public Tutor(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    
    // Método que se llama para notificar al tutor
    public void actualizar(String mensaje) {
        System.out.println("Notificación a Tutor " + nombre + ": " + mensaje);
    }
}

class Estudiante implements Observador {
    private String nombre;
    public Estudiante(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    
    // Método que se llama para notificar al estudiante
    public void actualizar(String mensaje) {
        System.out.println("Notificación a Estudiante " + nombre + ": " + mensaje);
    }
}

// --- Patrón State ---
// El patrón State permite que un objeto cambie su comportamiento cuando cambia su estado interno.
// Aquí la tutoría o clase  tiene diferentes estados (Solicitada, Aceptada, Rechazada, Completada) y cada estado define
// cómo responder a acciones como aceptar, rechazar o completar la tutoría.
interface EstadoTutoria {
    void aceptar(Tutoria tutoria);
    void rechazar(Tutoria tutoria);
    void completar(Tutoria tutoria);
    String getEstadoNombre();
}

class EstadoSolicitada implements EstadoTutoria {
    public void aceptar(Tutoria tutoria) {
       
        tutoria.setEstado(new EstadoAceptada());
        tutoria.notificar("Tutoría aceptada."); 
    }
    public void rechazar(Tutoria tutoria) {
        
        tutoria.setEstado(new EstadoRechazada());
        tutoria.notificar("Tutoría rechazada."); 
    }
    public void completar(Tutoria tutoria) {
        System.out.println("No se puede completar una tutoría solicitada.");
    }
    public String getEstadoNombre() { return "Solicitada"; }
}

class EstadoAceptada implements EstadoTutoria {
    public void aceptar(Tutoria tutoria) {
        System.out.println("La tutoría ya está aceptada.");
    }
    public void rechazar(Tutoria tutoria) {
        
        tutoria.setEstado(new EstadoRechazada());
        tutoria.notificar("Tutoría rechazada.");
    }
    public void completar(Tutoria tutoria) {
        
        tutoria.setEstado(new EstadoCompletada());
        tutoria.notificar("Tutoría completada.");
    }
    public String getEstadoNombre() { return "Aceptada"; }
}

class EstadoRechazada implements EstadoTutoria {
    public void aceptar(Tutoria tutoria) {
        System.out.println("No se puede aceptar una tutoría rechazada.");
    }
    public void rechazar(Tutoria tutoria) {
        System.out.println("La tutoría ya está rechazada.");
    }
    public void completar(Tutoria tutoria) {
        System.out.println("No se puede completar una tutoría rechazada.");
    }
    public String getEstadoNombre() { return "Rechazada"; }
}

class EstadoCompletada implements EstadoTutoria {
    public void aceptar(Tutoria tutoria) {
        System.out.println("No se puede aceptar una tutoría completada.");
    }
    public void rechazar(Tutoria tutoria) {
        System.out.println("No se puede rechazar una tutoría completada.");
    }
    public void completar(Tutoria tutoria) {
        System.out.println("La tutoría ya está completada.");
    }
    public String getEstadoNombre() { return "Completada"; }
}

//CLASE PRINCIPAL
class Tutoria {
    private EstadoTutoria estado;
    private List<Observador> observadores = new ArrayList<>();
    private String descripcion;

    public Tutoria(String descripcion) {
        this.descripcion = descripcion;
        this.estado = new EstadoSolicitada(); }

    
    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    
    public void notificar(String mensaje) {
        for (Observador o : observadores) {
            o.actualizar(mensaje);
        }
    }

   
    public void setEstado(EstadoTutoria estado) {
        this.estado = estado;
    }

   
    public EstadoTutoria getEstado() {
        return estado;
    }

   //METODOS DE LA CLASE
    public void aceptar() {
        estado.aceptar(this);
    }
    public void rechazar() {
        estado.rechazar(this);
    }
    public void completar() {
        estado.completar(this);
    }

    public String getDescripcion() {
        return descripcion;
    }
}

// --- Patrón Command ---
// El patrón Command en este caso se utiliza para encapsular una acción como un objeto, permitiendo parametrizar métodos con acciones,
// guardar historial de acciones para deshacer
// Aquí el administrador ejecuta comandos para acciones administrativas sobre el cambio de tutorías.
interface Command {
    void ejecutar();
    void deshacer();
}


class AsignarTutorCommand implements Command {
    private Tutoria tutoria;
    private Tutor tutor;
    private Tutor tutorPrevio; 

    public AsignarTutorCommand(Tutoria tutoria, Tutor tutor) {
        this.tutoria = tutoria;
        this.tutor = tutor;
    }

    public void ejecutar() {
        System.out.println("Asignando tutor " + tutor.getNombre() + " a la tutoría...");
        
    }

    public void deshacer() {
        System.out.println("Deshaciendo asignación de tutor " + tutor.getNombre() + "...");
        
    }
}


class Administrador {
    private boolean tienePermisos;
    private Stack<Command> historial = new Stack<>();

    public Administrador(boolean tienePermisos) {
        this.tienePermisos = tienePermisos;
    }

    
    public void ejecutarComando(Command comando) {
        if (tienePermisos) {
            comando.ejecutar();
            historial.push(comando);
        } else {
            System.out.println("No tiene permisos para ejecutar este comando.");
        }
    }

    
    public void deshacerUltimoComando() {
        if (!historial.isEmpty()) {
            Command cmd = historial.pop();
            cmd.deshacer();
        } else {
            System.out.println("No hay comandos para deshacer.");
        }
    }
}
//LOS DATOS SERAN INGRESADOS POR TECLADO
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Ingrese nombre del tutor: ");
        String nombreTutor = sc.nextLine();
        Tutor tutor = new Tutor(nombreTutor);

        System.out.print("Ingrese nombre del estudiante: ");
        String nombreEstudiante = sc.nextLine();
        Estudiante estudiante = new Estudiante(nombreEstudiante);

        System.out.print("Ingrese descripción de la tutoría: ");
        String descripcionTutoria = sc.nextLine();
        Tutoria tutoria = new Tutoria(descripcionTutoria);

       
        tutoria.agregarObservador(tutor);
        tutoria.agregarObservador(estudiante);

       
        Administrador admin = new Administrador(true);

        boolean salir = false;
        while(!salir) {
            System.out.println("\nEstado actual de la tutoría: " + tutoria.getEstado().getEstadoNombre());
            System.out.println("Seleccione una opción:");
            System.out.println("1. Aceptar tutoría");
            System.out.println("2. Rechazar tutoría");
            System.out.println("3. Completar tutoría");
            System.out.println("4. Asignar tutor (comando)");
            System.out.println("5. Deshacer último comando");
            System.out.println("6. Salir");
            System.out.print("Opción: ");

            int opcion = Integer.parseInt(sc.nextLine());
            switch(opcion) {
                case 1:
                    
                    tutoria.aceptar();
                    break;
                case 2:
                    
                    tutoria.rechazar();
                    break;
                case 3:
                    
                    tutoria.completar();
                    break;
                case 4:
                    
                    Command cmd = new AsignarTutorCommand(tutoria, tutor);
                    admin.ejecutarComando(cmd);
                    break;
                case 5:
                    
                    admin.deshacerUltimoComando();
                    break;
                case 6:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        sc.close();
        System.out.println("Programa terminado.");
    }
}