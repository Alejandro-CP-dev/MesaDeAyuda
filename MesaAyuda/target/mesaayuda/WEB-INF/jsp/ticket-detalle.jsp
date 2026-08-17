<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket #${ticket.id} — Mesa de Ayuda CIMM</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<header class="barra">
    <div class="brand">
        <div class="brand-icon">MA</div>
        Mesa de Ayuda CIMM
    </div>
    <div class="usuario-actual">
        <span>${sessionScope.usuarioAutenticado.nombre}</span>
        <span class="rol">${sessionScope.usuarioAutenticado.rol}</span>
        <a class="btn-salir" href="${pageContext.request.contextPath}/logout">Salir</a>
    </div>
</header>

<main class="contenedor">
    <a href="${pageContext.request.contextPath}/app/tickets" style="color:#0f766e; font-weight:600; font-size:0.85rem;">&larr; Volver a la lista</a>

    <h1 style="margin-top:0.8rem;">#${ticket.id} — ${ticket.titulo}</h1>
    <p class="subtitulo">
        <span class="badge badge-${ticket.estado}">${ticket.estado}</span>
        <span class="badge badge-${ticket.prioridad}">${ticket.prioridad}</span>
        <c:if test="${ticket.vencido}"><span class="badge badge-vencido">SLA vencido</span></c:if>
    </p>

    <c:if test="${not empty error}">
        <div class="alerta alerta-error">${error}</div>
    </c:if>

    <div class="panel">
        <p>${ticket.descripcion}</p>
        <div class="meta-ticket">
            <span>Categoria: <strong>${ticket.categoria}</strong></span>
            <span>Solicitante: <strong>${ticket.solicitanteNombre}</strong></span>
            <span>Agente: <strong>${ticket.agenteNombre}</strong></span>
            <span>Creado: <strong>${ticket.fechaCreacion}</strong></span>
            <span>Limite SLA: <strong>${ticket.fechaLimiteSla}</strong></span>
        </div>

        <div class="acciones-ticket">
            <c:if test="${sessionScope.usuarioAutenticado.rol == 'AGENTE' && ticket.estado == 'ASIGNADO'}">
                <form method="post" action="${pageContext.request.contextPath}/app/ticket">
                    <input type="hidden" name="id" value="${ticket.id}">
                    <input type="hidden" name="accion" value="iniciar">
                    <button type="submit" class="btn btn-primario">Iniciar atencion</button>
                </form>
            </c:if>

            <c:if test="${sessionScope.usuarioAutenticado.rol == 'AGENTE' && ticket.estado == 'EN_PROCESO'}">
                <form method="post" action="${pageContext.request.contextPath}/app/ticket">
                    <input type="hidden" name="id" value="${ticket.id}">
                    <input type="hidden" name="accion" value="resolver">
                    <button type="submit" class="btn btn-primario">Marcar como resuelto</button>
                </form>
            </c:if>

            <c:if test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE' && ticket.estado == 'RESUELTO'}">
                <form method="post" action="${pageContext.request.contextPath}/app/ticket">
                    <input type="hidden" name="id" value="${ticket.id}">
                    <input type="hidden" name="accion" value="cerrar">
                    <button type="submit" class="btn btn-primario">Confirmar y cerrar</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/app/ticket">
                    <input type="hidden" name="id" value="${ticket.id}">
                    <input type="hidden" name="accion" value="reabrir">
                    <button type="submit" class="btn btn-secundario">El problema persiste: reabrir</button>
                </form>
            </c:if>

            <c:if test="${sessionScope.usuarioAutenticado.rol == 'ADMINISTRADOR' && ticket.estado != 'CERRADO' && ticket.estado != 'CANCELADO'}">
                <form method="post" action="${pageContext.request.contextPath}/app/ticket"
                      onsubmit="return confirm('¿Cancelar este ticket?');">
                    <input type="hidden" name="id" value="${ticket.id}">
                    <input type="hidden" name="accion" value="cancelar">
                    <button type="submit" class="btn btn-peligro">Cancelar ticket</button>
                </form>
            </c:if>
        </div>

        <c:if test="${sessionScope.usuarioAutenticado.rol == 'ADMINISTRADOR' && not empty agentes}">
            <form method="post" action="${pageContext.request.contextPath}/app/ticket" style="margin-top:1.2rem; max-width:320px;">
                <input type="hidden" name="id" value="${ticket.id}">
                <input type="hidden" name="accion" value="reasignar">
                <label for="nuevoAgenteId">Reasignar a otro agente</label>
                <select id="nuevoAgenteId" name="nuevoAgenteId" required>
                    <c:forEach var="agente" items="${agentes}">
                        <option value="${agente.id}">${agente.nombre}</option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-secundario">Reasignar</button>
            </form>
        </c:if>
    </div>

    <div class="panel">
        <h1 style="font-size:1.1rem; margin-bottom:1rem;">Comentarios</h1>

        <c:choose>
            <c:when test="${empty ticket.comentarios}">
                <p class="vacio">Aun no hay comentarios en este ticket.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="comentario" items="${ticket.comentarios}">
                    <div class="comentario">
                        <span class="autor">${comentario.autorNombre}</span>
                        <span class="fecha">${comentario.fecha}</span>
                        <p class="texto">${comentario.texto}</p>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        <form method="post" action="${pageContext.request.contextPath}/app/ticket" style="margin-top:1.2rem;">
            <input type="hidden" name="id" value="${ticket.id}">
            <input type="hidden" name="accion" value="comentar">
            <div class="campo">
                <label for="texto">Agregar comentario</label>
                <textarea id="texto" name="texto" required></textarea>
            </div>
            <button type="submit" class="btn btn-primario">Comentar</button>
        </form>
    </div>
</main>
</body>
</html>
