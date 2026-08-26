# Documento de diseño — Mesa de Ayuda CIMM

## 1. Arquitectura general

Se replica la arquitectura del proyecto de referencia `tienda-ecommerce`:
Servlet (control HTTP) → Service (reglas de negocio) → Repository (acceso a
datos), con `AppContextListener` como composition root que cablea todas las
dependencias a mano (sin Spring).

```
Navegador
   │
   ▼
Servlet (co.edu.sena.mesaayuda.web)          <- solo HTTP, cero reglas de negocio
   │  usa interfaces de...
   ▼
Service (co.edu.sena.mesaayuda.servicio)     <- reglas de negocio, patron State y Strategy
   │  usa interfaces de...
   ▼
Repository (co.edu.sena.mesaayuda.repositorio) <- JDBC contra MesaAyudaDb
```

Las JSP (`WEB-INF/jsp`) reciben siempre DTOs (`co.edu.sena.mesaayuda.dto`),
nunca entidades de dominio ni el objeto `EstadoTicket`: no necesitan saber
que existe el patron State, solo pintan strings.

## 2. Patron State — ciclo de vida del ticket

`co.edu.sena.mesaayuda.modelo.estado`

| Clase | Rol |
|---|---|
| `EstadoTicket` | Interfaz. Metodos default que lanzan `TransicionInvalidaException`; cada estado concreto sobreescribe solo las transiciones que permite. |
| `Nuevo`, `Asignado`, `EnProceso`, `Resuelto`, `Cerrado`, `Cancelado` | Un singleton por estado (sin datos propios). Cada uno decide a que estado puede pasar. |
| `EstadoTicketFactory` | Traduce el `String` guardado en `Ticket.Estado` a la instancia correspondiente al leer de la BD. |

`Ticket` (en `modelo`) NUNCA pregunta "si estoy en X y me piden Y, entonces
Z": delega siempre en `estado.transicion()`. Por eso no hay ningun
if/else ni switch de estados en el proyecto.

## 3. Patron Strategy — OCP

| Variabilidad | Paquete | Interfaz | Implementaciones incluidas |
|---|---|---|---|
| Calculo de SLA (RF-09) | `servicio.sla` | `EstrategiaSla` | `SlaPorHorasPrioridad` |
| Asignacion de agente (RF-04) | `servicio.asignacion` | `EstrategiaAsignacion` | `AsignacionPorMenorCarga`, `AsignacionPorTurnoRotativo` |
| Notificacion (RF-08) | `servicio.notificacion` | `Notificador` | `NotificadorEnAplicacion`, `NotificadorCorreo`, `NotificadorSms` |

Para activar una implementacion distinta (por ejemplo `AsignacionPorTurnoRotativo`
en vez de `AsignacionPorMenorCarga`) se cambia UNA linea en
`AppContextListener`. Ninguna interfaz de servicio se toca.

## 4. Tabla SOLID → clases

| Principio | Donde se aplica | Como |
|---|---|---|
| **S**RP | `web.*Servlet` vs `servicio.*ServiceImpl` vs `repositorio.*RepositoryJdbc` | Cada capa tiene una sola razon para cambiar: el Servlet si cambia el flujo HTTP, el Service si cambia una regla de negocio, el Repository si cambia la forma de persistir. |
| **O**CP | `modelo.estado.*`, `servicio.sla.*`, `servicio.asignacion.*`, `servicio.notificacion.*` | Agregar un estado, una politica de SLA, una estrategia de asignacion o un canal de notificacion es CREAR una clase nueva que implemente la interfaz correspondiente. Ninguna clase existente se modifica. |
| **L**SP | Todas las implementaciones de `EstadoTicket`, `EstrategiaAsignacion`, `EstrategiaSla`, `Notificador`, `*Repository` | Cualquier implementacion puede sustituir a su interfaz sin romper quien la usa (por ejemplo, `TicketRepositoryJdbc` podria cambiarse por un `TicketRepositoryEnMemoria` para pruebas, y los Servlets seguirian funcionando igual). |
| **I**SP | `TicketRepository`, `ComentarioRepository`, `UsuarioRepository`, etc. (una interfaz por entidad); y `ConsultaTicketService` + `OperacionesSolicitante` + `OperacionesAgente` + `OperacionesAdministrador` (una interfaz por ROL sobre Ticket) | La lectura (`listarParaUsuario`, `obtenerDetalle`) es comun a los tres roles y vive en `ConsultaTicketService`. La escritura NO: `OperacionesSolicitante` solo tiene `crearTicket/cerrar/reabrir`, `OperacionesAgente` solo `iniciarAtencion/resolver`, `OperacionesAdministrador` solo `cancelar/reasignar`. `TicketServiceImpl` es la unica clase que las implementa las tres (LSP: sigue siendo sustituible por cualquiera de ellas), pero cada Servlet en `AppContextListener` recibe SOLO la interfaz de su rol — un Servlet de agente no puede, ni por error, llamar `cancelar()`. |
| **D**IP | `servicio.TicketServiceImpl` (y los demas Service) | Reciben `TicketRepository`, `UsuarioRepository`, `EstrategiaSla`, etc. por **constructor** — nunca hacen `new TicketRepositoryJdbc()` ellos mismos. Solo `AppContextListener` conoce las clases concretas. |

## 5. Requisitos funcionales y donde viven

| RF | Clase principal |
|---|---|
| RF-01 Autenticacion | `AutenticacionServiceImpl`, `LoginServlet` |
| RF-02 Registrar ticket | `TicketServiceImpl.crearTicket` |
| RF-03 Prioridad automatica | `PrioridadServiceImpl` |
| RF-04 Asignacion de agente | `TicketServiceImpl.crearTicket` + `EstrategiaAsignacion` |
| RF-05 Listar segun rol | `TicketServiceImpl.listarParaUsuario` |
| RF-06 Cambiar estado | `Ticket` + `modelo.estado.*` (patron State) |
| RF-07 Comentarios | `ComentarioServiceImpl` |
| RF-08 Notificaciones | `PublicadorNotificaciones` + `Notificador`. Se notifica al solicitante en TODA transicion, incluidas `cerrar`/`reabrir` que el mismo solicitante origina (queda como constancia), ademas de avisar al agente en esos dos casos. |
| RF-09 SLA | `EstrategiaSla`, columna `Ticket.FechaLimiteSla` |
| RF-10 Reasignar (admin) | `TicketServiceImpl.reasignar` |

## 6. Estructura de paquetes

Igual a la pedida en el enunciado (seccion 11): `modelo`, `modelo.estado`,
`dto`, `mapper`, `repositorio`, `servicio`, `servicio.sla`,
`servicio.asignacion`, `servicio.notificacion`, `web`.

## 7. Decisiones tecnicas y por que

- **Persistencia JDBC "a mano"** (sin pool, `ConexionBD` con `DriverManager`):
  mas simple de explicar en sustentacion que introducir HikariCP; una
  conexion por peticion, cerrada con try-with-resources.
- **Estados como singletons sin datos**: no hay razon para crear una
  instancia de `Asignado` por cada ticket; todos comparten la misma.
- **DTOs siempre planos** (strings ya formateados) para que la JSP no
  necesite logica ni conozca el dominio (regla del enunciado: "prohibido
  poner logica de negocio en la JSP").
