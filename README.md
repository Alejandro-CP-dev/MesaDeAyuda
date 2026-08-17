# Mesa de Ayuda CIMM — SENA Regional Boyacá

Sistema de tickets de soporte en Java Web (Servlets + JSP) aplicando SOLID,
patron State (ciclo de vida del ticket) y Strategy (SLA, asignación,
notificación). Arquitectura replicada de `tienda-ecommerce`.

Ver **DISENO.md** para el mapeo completo de SOLID a clases y la explicación
de cada patrón (documento pedido en la sección 13 del taller).

## 1. Requisitos

- JDK 11+
- Maven 3.6+
- MySQL 5.7+ / MariaDB (XAMPP funciona perfecto)
- Apache Tomcat 8.5+ (o 9)

## 2. Base de datos

Ejecutar, en este orden, en tu cliente de MySQL (phpMyAdmin, MySQL Workbench,
consola):

```
sql/01_MesaAyudaDb_principal.sql
sql/02_MesaAyudaDb_complemento.sql
```

El primero crea la base `MesaAyudaDb` y las tablas base (`Rol`, `Usuario`,
`Categoria`, `Prioridad`, `Ticket`, `Comentario`). El segundo es el script
que ya tenías: agrega las columnas de fecha, el historial, las
notificaciones y los datos de prueba.

Si tu MySQL no usa `root` sin clave (como XAMPP por defecto), edita:

```
src/main/resources/db.properties
```

## 3. Compilar y empaquetar

```bash
mvn clean package
```

Esto genera `target/mesaayuda.war`.

## 4. Desplegar en Tomcat

Copia `target/mesaayuda.war` a la carpeta `webapps/` de tu Tomcat (o usa el
Tomcat Manager). Arranca Tomcat y entra a:

```
http://localhost:8080/mesaayuda/
```

## 5. Usuarios de prueba

Clave para todos: **123456**

| Correo | Rol |
|---|---|
| ana.ramirez@sena.edu.co | Solicitante |
| luis.torres@sena.edu.co | Solicitante |
| carlos.mendoza@sena.edu.co | Agente |
| diana.ruiz@sena.edu.co | Agente |
| jorge.pinilla@sena.edu.co | Agente |
| osan@sena.edu.co | Administrador |

## 6. Recorrido sugerido para la sustentación

1. Entrar como **solicitante** (ana.ramirez) → crear un ticket con la
   palabra "urgente" o "caído" en la descripción → mostrar que la
   prioridad queda en CRITICA/ALTA automáticamente (RF-03) y que ya
   aparece ASIGNADO a un agente (RF-04).
2. Entrar como ese **agente** → abrir el ticket → "Iniciar atención" →
   "Marcar como resuelto". Mostrar que si se intenta forzar una
   transición inválida (por ejemplo resolver un ticket NUEVO llamando el
   endpoint directamente) salta `TransicionInvalidaException` — así se
   demuestra en vivo el patrón State.
3. Volver como **solicitante** → cerrar o reabrir el ticket resuelto.
4. Entrar como **administrador** (osan) → ver todos los tickets, cancelar
   uno, reasignar otro.
5. Mostrar `AppContextListener` y explicar que cambiar
   `AsignacionPorMenorCarga` por `AsignacionPorTurnoRotativo` (o agregar
   `NotificadorSms` a la lista) es una línea, sin tocar `TicketService`
   (ahí está el OCP).
6. Correr `mvn test` para mostrar las pruebas del patrón State
   (`EstadoTicketTest`) y de la prioridad automática
   (`PrioridadServiceImplTest`).

## 7. Estado de este entregable

Todo el código fuente (modelo, estado, repositorios, servicios, mappers,
DTOs, servlets y tests) se compiló y verificó sin errores antes de la
entrega. El entorno donde se generó este proyecto no tiene salida a Maven
Central, así que esa compilación se hizo contra un stub local de la
Servlet API y de JUnit en vez del `.jar` real — la firma de cada método
usado se verificó contra la Servlet API 4.0.1 real. Aun así, ejecuta
`mvn clean package` con internet antes de tu sustentación para que Maven
descargue las dependencias reales (`javax.servlet-api`, `jstl`,
`mysql-connector-java`, `junit-jupiter`) y confirmar el build definitivo en
tu máquina.
