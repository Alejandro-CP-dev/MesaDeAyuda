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
import co.edu.sena.mesaayuda.servicio.PrioridadService;
import co.edu.sena.mesaayuda.servicio.PrioridadServiceImpl;
import co.edu.sena.mesaayuda.servicio.TicketService;
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
 * Punto de composicion (composition root) de la aplicacion, igual rol que
 * en el proyecto de referencia: aqui, y SOLO aqui, se crean las
 * implementaciones concretas y se inyectan por constructor. El resto del
 * codigo (servicios, servlets) trabaja contra interfaces (DIP).
 *
 * Cambiar la estrategia de asignacion activa (por menor carga vs. turno
 * rotativo) o los canales de notificacion activos se hace UNICAMENTE en
 * este archivo (OCP: no hay que tocar TicketService).
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    public static final String TICKET_SERVICE = "ticketService";
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
        TicketService ticketService = new TicketServiceImpl(
                ticketRepository, categoriaRepository, usuarioRepository, comentarioRepository,
                historialRepository, prioridadService, estrategiaSla, estrategiaAsignacion, publicadorNotificaciones);

        // 5. Publicar en el contexto para que los servlets los usen.
        contexto.setAttribute(TICKET_SERVICE, ticketService);
        contexto.setAttribute(COMENTARIO_SERVICE, comentarioService);
        contexto.setAttribute(AUTENTICACION_SERVICE, autenticacionService);
        contexto.setAttribute(CATEGORIA_REPOSITORY, categoriaRepository);
        contexto.setAttribute(USUARIO_REPOSITORY, usuarioRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        // Sin pool de conexiones ni otros recursos que liberar explicitamente.
    }
}
