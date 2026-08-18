<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>
            Ticket #${ticket.id} — CIMM Desk
        </title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/estilo.css">

    </head>


    <body class="app-page">

        <div class="app-layout">

            <!-- SIDEBAR -->
            <aside class="sidebar">

                <div class="sidebar-brand">

                    <div class="brand-mark">
                        C
                    </div>

                    <div class="sidebar-brand-text">
                        <strong>CIMM</strong>
                        <span>DESK</span>
                    </div>

                </div>


                <div class="sidebar-section-title">
                    PRINCIPAL
                </div>


                <nav class="sidebar-nav">

                    <a href="${pageContext.request.contextPath}/app/tickets"
                       class="nav-item active">

                        <span class="nav-icon">▦</span>

                        <span>Tickets</span>

                    </a>

                </nav>


                <div class="sidebar-spacer"></div>


                <div class="sidebar-user">

                    <div class="avatar">
                        ${sessionScope.usuarioAutenticado.nombre.substring(0,1)}
                    </div>

                    <div class="sidebar-user-info">

                        <strong>
                            ${sessionScope.usuarioAutenticado.nombre}
                        </strong>

                        <span>
                            ${sessionScope.usuarioAutenticado.rol}
                        </span>

                    </div>

                </div>


                <a href="${pageContext.request.contextPath}/logout"
                   class="sidebar-logout">

                    <span>↪</span>
                    Cerrar sesión

                </a>

            </aside>


            <!-- MAIN -->
            <main class="app-main">

                <header class="topbar">

                    <div class="mobile-menu-brand">

                        <div class="brand-mark">
                            C
                        </div>

                        <strong>CIMM DESK</strong>

                    </div>


                    <div class="topbar-right">

                        <div class="status-indicator">

                            <span></span>
                            Sistema operativo

                        </div>


                        <div class="topbar-user">

                            <div class="avatar avatar-small">
                                ${sessionScope.usuarioAutenticado.nombre.substring(0,1)}
                            </div>

                            <div>

                                <strong>
                                    ${sessionScope.usuarioAutenticado.nombre}
                                </strong>

                                <span>
                                    ${sessionScope.usuarioAutenticado.rol}
                                </span>

                            </div>

                        </div>

                    </div>

                </header>


                <div class="page-content detail-page">

                    <!-- BREADCRUMB -->

                    <a href="${pageContext.request.contextPath}/app/tickets"
                       class="back-link">

                        <span>←</span>
                        Volver a tickets

                    </a>


                    <!-- HEADER DEL TICKET -->

                    <div class="ticket-detail-header">

                        <div>

                            <div class="ticket-number">
                                TICKET #${ticket.id}
                            </div>

                            <h1>
                                ${ticket.titulo}
                            </h1>

                            <div class="ticket-status-line">

                                <span class="badge badge-${ticket.estado}">
                                    ${ticket.estado}
                                </span>

                                <span class="badge badge-${ticket.prioridad}">
                                    ${ticket.prioridad}
                                </span>

                                <c:if test="${ticket.vencido}">

                                    <span class="badge badge-vencido">
                                        SLA vencido
                                    </span>

                                </c:if>

                            </div>

                        </div>

                    </div>


                    <c:if test="${not empty error}">

                        <div class="alerta alerta-error">

                            <span class="alert-icon">!</span>

                            <div>
                                ${error}
                            </div>

                        </div>

                    </c:if>


                    <div class="detail-grid">


                        <!-- COLUMNA PRINCIPAL -->

                        <div class="detail-main">


                            <!-- DESCRIPCIÓN -->

                            <section class="content-card">

                                <div class="card-heading">

                                    <div>

                                        <span class="section-kicker">
                                            DESCRIPCIÓN
                                        </span>

                                        <h2>
                                            Detalles de la solicitud
                                        </h2>

                                    </div>

                                </div>


                                <div class="ticket-description">

                                    <p>
                                        ${ticket.descripcion}
                                    </p>

                                </div>

                            </section>


                            <!-- COMENTARIOS -->

                            <section class="content-card">

                                <div class="card-heading">

                                    <div>

                                        <span class="section-kicker">
                                            ACTIVIDAD
                                        </span>

                                        <h2>
                                            Conversación
                                        </h2>

                                        <p>
                                            Historial de comentarios del ticket.
                                        </p>

                                    </div>

                                    <div class="comment-count">

                                        <c:choose>

                                            <c:when test="${empty ticket.comentarios}">
                                                0
                                            </c:when>

                                            <c:otherwise>
                                                ${ticket.comentarios.size()}
                                            </c:otherwise>

                                        </c:choose>

                                    </div>

                                </div>


                                <div class="comments-list">

                                    <c:choose>

                                        <c:when test="${empty ticket.comentarios}">

                                            <div class="empty-comments">

                                                <div class="empty-icon small">
                                                    …
                                                </div>

                                                <p>
                                                    Aún no hay comentarios.
                                                </p>

                                                <span>
                                                    La actividad del ticket aparecerá aquí.
                                                </span>

                                            </div>

                                        </c:when>


                                        <c:otherwise>

                                            <c:forEach var="comentario"
                                                       items="${ticket.comentarios}">

                                                <article class="comentario">

                                                    <div class="comment-avatar">

                                                        ${comentario.autorNombre.substring(0,1)}

                                                    </div>


                                                    <div class="comment-content">

                                                        <div class="comment-header">

                                                            <strong>
                                                                ${comentario.autorNombre}
                                                            </strong>

                                                            <span>
                                                                ${comentario.fecha}
                                                            </span>

                                                        </div>


                                                        <p>
                                                            ${comentario.texto}
                                                        </p>

                                                    </div>

                                                </article>

                                            </c:forEach>

                                        </c:otherwise>

                                    </c:choose>

                                </div>


                                <!-- NUEVO COMENTARIO -->

                                <form method="post"
                                      action="${pageContext.request.contextPath}/app/ticket"
                                      class="comment-form">

                                    <input type="hidden"
                                           name="id"
                                           value="${ticket.id}">

                                    <input type="hidden"
                                           name="accion"
                                           value="comentar">


                                    <div class="campo">

                                        <label for="texto">
                                            Añadir comentario
                                        </label>

                                        <textarea id="texto"
                                                  name="texto"
                                                  placeholder="Escribe una actualización o comentario..."
                                                  required></textarea>

                                    </div>


                                    <div class="comment-form-footer">

                                        <span>
                                            Tu comentario quedará registrado en la actividad.
                                        </span>

                                        <button type="submit"
                                                class="btn btn-primario">

                                            Enviar comentario
                                            <span>→</span>

                                        </button>

                                    </div>

                                </form>

                            </section>

                        </div>


                        <!-- SIDEBAR DEL TICKET -->

                        <aside class="detail-sidebar">


                            <!-- INFORMACIÓN -->

                            <section class="content-card">

                                <div class="card-heading compact">

                                    <div>

                                        <span class="section-kicker">
                                            INFORMACIÓN
                                        </span>

                                        <h2>
                                            Detalles
                                        </h2>

                                    </div>

                                </div>


                                <div class="info-list">

                                    <div class="info-item">

                                        <span>Categoría</span>

                                        <strong>
                                            ${ticket.categoria}
                                        </strong>

                                    </div>


                                    <div class="info-item">

                                        <span>Solicitante</span>

                                        <strong>
                                            ${ticket.solicitanteNombre}
                                        </strong>

                                    </div>


                                    <div class="info-item">

                                        <span>Agente</span>

                                        <strong>

                                            <c:choose>

                                                <c:when test="${not empty ticket.agenteNombre}">
                                                    ${ticket.agenteNombre}
                                                </c:when>

                                                <c:otherwise>
                                                    Sin asignar
                                                </c:otherwise>

                                            </c:choose>

                                        </strong>

                                    </div>


                                    <div class="info-item">

                                        <span>Fecha de creación</span>

                                        <strong>
                                            ${ticket.fechaCreacion}
                                        </strong>

                                    </div>


                                    <div class="info-item">

                                        <span>Límite SLA</span>

                                        <strong>
                                            ${ticket.fechaLimiteSla}
                                        </strong>

                                    </div>

                                </div>

                            </section>


                            <!-- ACCIONES -->

                            <section class="content-card">

                                <div class="card-heading compact">

                                    <div>

                                        <span class="section-kicker">
                                            ACCIONES
                                        </span>

                                        <h2>
                                            Gestionar ticket
                                        </h2>

                                    </div>

                                </div>


                                <div class="ticket-actions">


                                    <c:if test="${sessionScope.usuarioAutenticado.rol == 'AGENTE' && ticket.estado == 'ASIGNADO'}">

                                        <form method="post"
                                              action="${pageContext.request.contextPath}/app/ticket">

                                            <input type="hidden"
                                                   name="id"
                                                   value="${ticket.id}">

                                            <input type="hidden"
                                                   name="accion"
                                                   value="iniciar">

                                            <button type="submit"
                                                    class="btn btn-primario btn-block">

                                                Iniciar atención
                                                <span>→</span>

                                            </button>

                                        </form>

                                    </c:if>


                                    <c:if test="${sessionScope.usuarioAutenticado.rol == 'AGENTE' && ticket.estado == 'EN_PROCESO'}">

                                        <form method="post"
                                              action="${pageContext.request.contextPath}/app/ticket">

                                            <input type="hidden"
                                                   name="id"
                                                   value="${ticket.id}">

                                            <input type="hidden"
                                                   name="accion"
                                                   value="resolver">

                                            <button type="submit"
                                                    class="btn btn-primario btn-block">

                                                Marcar como resuelto
                                                <span>✓</span>

                                            </button>

                                        </form>

                                    </c:if>


                                    <c:if test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE' && ticket.estado == 'RESUELTO'}">

                                        <form method="post"
                                              action="${pageContext.request.contextPath}/app/ticket">

                                            <input type="hidden"
                                                   name="id"
                                                   value="${ticket.id}">

                                            <input type="hidden"
                                                   name="accion"
                                                   value="cerrar">

                                            <button type="submit"
                                                    class="btn btn-primario btn-block">

                                                Confirmar y cerrar
                                                <span>✓</span>

                                            </button>

                                        </form>


                                        <form method="post"
                                              action="${pageContext.request.contextPath}/app/ticket">

                                            <input type="hidden"
                                                   name="id"
                                                   value="${ticket.id}">

                                            <input type="hidden"
                                                   name="accion"
                                                   value="reabrir">

                                            <button type="submit"
                                                    class="btn btn-secundario btn-block">

                                                El problema persiste

                                            </button>

                                        </form>

                                    </c:if>


                                    <c:if test="${sessionScope.usuarioAutenticado.rol == 'ADMINISTRADOR' && ticket.estado != 'CERRADO' && ticket.estado != 'CANCELADO'}">

                                        <form method="post"
                                              action="${pageContext.request.contextPath}/app/ticket"
                                              onsubmit="return confirm('¿Cancelar este ticket?');">

                                            <input type="hidden"
                                                   name="id"
                                                   value="${ticket.id}">

                                            <input type="hidden"
                                                   name="accion"
                                                   value="cancelar">

                                            <button type="submit"
                                                    class="btn btn-peligro btn-block">

                                                Cancelar ticket

                                            </button>

                                        </form>

                                    </c:if>


                                </div>

                            </section>


                            <!-- REASIGNACIÓN -->

                            <c:if test="${sessionScope.usuarioAutenticado.rol == 'ADMINISTRADOR' && not empty agentes}">

                                <section class="content-card">

                                    <div class="card-heading compact">

                                        <div>

                                            <span class="section-kicker">
                                                ADMINISTRACIÓN
                                            </span>

                                            <h2>
                                                Reasignar
                                            </h2>

                                        </div>

                                    </div>


                                    <form method="post"
                                          action="${pageContext.request.contextPath}/app/ticket">

                                        <input type="hidden"
                                               name="id"
                                               value="${ticket.id}">

                                        <input type="hidden"
                                               name="accion"
                                               value="reasignar">


                                        <div class="campo">

                                            <label for="nuevoAgenteId">
                                                Agente responsable
                                            </label>

                                            <select id="nuevoAgenteId"
                                                    name="nuevoAgenteId"
                                                    required>

                                                <c:forEach var="agente"
                                                           items="${agentes}">

                                                    <option value="${agente.id}">
                                                        ${agente.nombre}
                                                    </option>

                                                </c:forEach>

                                            </select>

                                        </div>


                                        <button type="submit"
                                                class="btn btn-secundario btn-block">

                                            Reasignar ticket

                                        </button>

                                    </form>

                                </section>

                            </c:if>


                        </aside>

                    </div>

                </div>

            </main>

        </div>

    </body>
</html>