package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.repositorio.CategoriaRepository;
import co.edu.sena.mesaayuda.repositorio.CategoriaRepositoryJdbc;
import co.edu.sena.mesaayuda.repositorio.ComentarioRepository;
import co.edu.sena.mesaayuda.repositorio.ComentarioRepositoryJdbc;
import co.edu.sena.mesaayuda.repositorio.NotificacionRepository;
import co.edu.sena.mesaayuda.repositorio.NotificacionRepositoryJdbc;
import co.edu.sena.mesaayuda.repositorio.PrioridadRepository;
import co.edu.sena.mesaayuda.repositorio.PrioridadRepositoryJdbc;
import co.edu.sena.mesaayuda.repositorio.TicketHistorialRepository;
import co.edu.sena.mesaayuda.repositorio.TicketHistorialRepositoryJdbc;
import co.edu.sena.mesaayuda.repositorio.TicketRepository;
import co.edu.sena.mesaayuda.repositorio.TicketRepositoryJdbc;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepository;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepositoryJdbc;
import co.edu.sena.mesaayuda.servicio.AutenticacionService;
import co.edu.sena.mesaayuda.servicio.AutenticacionServiceImpl;
import co.edu.sena.mesaayuda.servicio.ComentarioService;
import co.edu.sena.mesaayuda.servicio.ComentarioServiceImpl;
import co.edu.sena.mesaayuda.servicio.ConsultaTicketService;
import co.edu.sena.mesaayuda.servicio.OperacionesAdministrador;
import co.edu.sena.mesaayuda.servicio.OperacionesAgente;
import co.edu.sena.mesaayuda.servicio.OperacionesSolicitante;
import co.edu.sena.mesaayuda.servicio.PrioridadService;
import co.edu.sena.mesaayuda.servicio.PrioridadServiceImpl;
import co.edu.sena.mesaayuda.servicio.TicketServiceImpl;
import co.edu.sena.mesaayuda.servicio.asignacion.AsignacionPorMenorCarga;
import co.edu.sena.mesaayuda.servicio.asignacion.EstrategiaAsignacion;
import co.edu.sena.mesaayuda.servicio.notificacion.Notificador;
import co.edu.sena.mesaayuda.servicio.notificacion.NotificadorCorreo;
import co.edu.sena.mesaayuda.servicio.notificacion.NotificadorEnAplicacion;
import co.edu.sena.mesaayuda.servicio.notificacion.PublicadorNotificaciones;
import co.edu.sena.mesaayuda.servicio.sla.EstrategiaSla;
import co.edu.sena.mesaayuda.servicio.sla.SlaPorHorasPrioridad;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * Punto de composicion (composition root) de la aplicacion, igual rol que en el
 * proyecto de referencia: aqui, y SOLO aqui, se crean las implementaciones
 * concretas y se inyectan por constructor. El resto del codigo (servicios,
 * servlets) trabaja contra interfaces (DIP).
 *
 * Cambiar la estrategia de asignacion activa (por menor carga vs. turno
 * rotativo) o los canales de notificacion activos se hace UNICAMENTE en este
 * archivo (OCP: no hay que tocar TicketService).
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    // ISP: cuatro claves para la MISMA instancia de TicketServiceImpl, cada
    // una publicada bajo el tipo de interfaz que le corresponde a su rol.
    // Un servlet que solo necesita las operaciones de agente pide
    // OPERACIONES_AGENTE y el compilador no lo deja llamar, por ejemplo,
    // cancelar() (esa es de administrador): el objeto real puede hacerlo
    // todo, pero la interfaz que cada consumidor ve es la minima necesaria.
    public static final String CONSULTA_TICKET_SERVICE = "consultaTicketService";
    public static final String OPERACIONES_SOLICITANTE = "operacionesSolicitante";
    public static final String OPERACIONES_AGENTE = "operacionesAgente";
    public static final String OPERACIONES_ADMINISTRADOR = "operacionesAdministrador";
    public static final String COMENTARIO_SERVICE = "comentarioService";
    public static final String AUTENTICACION_SERVICE = "autenticacionService";
    public static final String CATEGORIA_REPOSITORY = "categoriaRepository";
    public static final String USUARIO_REPOSITORY = "usuarioRepository";

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ServletContext contexto = evento.getServletContext();

        // 1. Repositorios JDBC (unico lugar que conoce las implementaciones concretas).
        UsuarioRepository usuarioRepository = new UsuarioRepositoryJdbc();
        CategoriaRepository categoriaRepository = new CategoriaRepositoryJdbc();
        PrioridadRepository prioridadRepository = new PrioridadRepositoryJdbc();
        TicketRepository ticketRepository = new TicketRepositoryJdbc();
        ComentarioRepository comentarioRepository = new ComentarioRepositoryJdbc();
        NotificacionRepository notificacionRepository = new NotificacionRepositoryJdbc();
        TicketHistorialRepository historialRepository = new TicketHistorialRepositoryJdbc();

        // 2. Estrategias (Strategy). Para cambiar la politica de asignacion
        // basta con instanciar AsignacionPorTurnoRotativo en su lugar.
        EstrategiaSla estrategiaSla = new SlaPorHorasPrioridad();
        EstrategiaAsignacion estrategiaAsignacion = new AsignacionPorMenorCarga(ticketRepository);

        // 3. Canales de notificacion activos (OCP: agregar NotificadorSms es sumarlo a esta lista).
        List<Notificador> notificadores = List.of(
                new NotificadorEnAplicacion(notificacionRepository),
                new NotificadorCorreo(notificacionRepository)
        );
        PublicadorNotificaciones publicadorNotificaciones = new PublicadorNotificaciones(notificadores);

        // 4. Servicios, recibiendo sus dependencias por constructor.
        PrioridadService prioridadService = new PrioridadServiceImpl(prioridadRepository);
        AutenticacionService autenticacionService = new AutenticacionServiceImpl(usuarioRepository);
        ComentarioService comentarioService = new ComentarioServiceImpl(comentarioRepository, ticketRepository);
        TicketServiceImpl ticketService = new TicketServiceImpl(
                ticketRepository, categoriaRepository, usuarioRepository, comentarioRepository,
                historialRepository, prioridadService, estrategiaSla, estrategiaAsignacion, publicadorNotificaciones);

        // 5. Publicar en el contexto para que los servlets los usen. La MISMA
        // instancia se publica 4 veces, cada vez "disfrazada" del tipo de
        // interfaz correspondiente (ISP en accion en el punto de inyeccion).
        contexto.setAttribute(CONSULTA_TICKET_SERVICE, (ConsultaTicketService) ticketService);
        contexto.setAttribute(OPERACIONES_SOLICITANTE, (OperacionesSolicitante) ticketService);
        contexto.setAttribute(OPERACIONES_AGENTE, (OperacionesAgente) ticketService);
        contexto.setAttribute(OPERACIONES_ADMINISTRADOR, (OperacionesAdministrador) ticketService);
        contexto.setAttribute(COMENTARIO_SERVICE, comentarioService);
        contexto.setAttribute(AUTENTICACION_SERVICE, autenticacionService);
        contexto.setAttribute(CATEGORIA_REPOSITORY, categoriaRepository);
        contexto.setAttribute(USUARIO_REPOSITORY, usuarioRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        // Apaga a mano el hilo interno de limpieza del driver de MySQL.
        // Sin esto, ese hilo sigue vivo despues de parar la app, con una
        // referencia al ClassLoader de este WAR, y Tomcat lo reporta como
        // fuga de memoria en cada redeploy ("esta instancia de aplicacion
        // web ya ha sido parada"). Es el arreglo oficial de MySQL Connector/J.
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            // No relanzamos: si falla el apagado del hilo de limpieza no
            // debe impedir que el resto del contexto termine de destruirse.
            java.util.logging.Logger.getLogger(AppContextListener.class.getName())
                    .warning("No se pudo apagar AbandonedConnectionCleanupThread: " + e.getMessage());
        }

        // Tambien anulamos el driver registrado para este ClassLoader, por
        // la misma razon: evita que quede referenciado tras el redeploy.
        java.util.Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == this.getClass().getClassLoader()) {
                try {
                    java.sql.DriverManager.deregisterDriver(driver);
                } catch (java.sql.SQLException e) {
                    java.util.logging.Logger.getLogger(AppContextListener.class.getName())
                            .warning("No se pudo desregistrar el driver JDBC: " + e.getMessage());
                }
            }
        }
    }
}
