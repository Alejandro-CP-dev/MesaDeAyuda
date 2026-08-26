<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>CIMM Desk — Tickets</title>

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

                    <a href="${pageContext.request.contextPath}/app/notificaciones"
                       class="nav-item">

                        <span class="nav-icon">🔔</span>

                        <span>Notificaciones</span>

                    </a>

                    <c:if test="${sessionScope.usuarioAutenticado.rol == 'ADMINISTRADOR'}">

                        <a href="${pageContext.request.contextPath}/app/reportes"
                           class="nav-item">

                            <span class="nav-icon">📊</span>

                            <span>Reportes</span>

                        </a>

                    </c:if>

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


            <!-- CONTENIDO -->
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


                <div class="page-content">

                    <!-- CABECERA -->
                    <div class="page-heading">

                        <div>

                            <span class="eyebrow">
                                CENTRO DE SOPORTE
                            </span>

                            <h1>

                                <c:choose>

                                    <c:when test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE'}">
                                        Mis tickets
                                    </c:when>

                                    <c:when test="${sessionScope.usuarioAutenticado.rol == 'AGENTE'}">
                                        Tickets asignados
                                    </c:when>

                                    <c:otherwise>
                                        Todos los tickets
                                    </c:otherwise>

                                </c:choose>

                            </h1>

                            <p>
                                Gestiona y consulta las solicitudes de soporte.
                            </p>

                        </div>

                        <c:if test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE'}">

                            <a href="#nuevo-ticket"
                               class="btn btn-primario">

                                <span>+</span>
                                Nuevo ticket

                            </a>

                        </c:if>

                    </div>


                    <!-- MÉTRICAS -->
                    <div class="stats-grid">

                        <div class="stat-card">

                            <div class="stat-icon stat-blue">
                                ▦
                            </div>

                            <div>

                                <span class="stat-label">
                                    Tickets visibles
                                </span>

                                <strong class="stat-value">
                                    ${empty tickets ? 0 : tickets.size()}
                                </strong>

                            </div>

                        </div>


                        <div class="stat-card">

                            <div class="stat-icon stat-purple">
                                ◷
                            </div>

                            <div>

                                <span class="stat-label">
                                    Gestión
                                </span>

                                <strong class="stat-value">
                                    Activa
                                </strong>

                            </div>

                        </div>


                        <div class="stat-card">

                            <div class="stat-icon stat-green">
                                ✓
                            </div>

                            <div>

                                <span class="stat-label">
                                    Plataforma
                                </span>

                                <strong class="stat-value">
                                    Operativa
                                </strong>

                            </div>

                        </div>

                    </div>


                    <c:if test="${not empty error}">

                        <div class="alerta alerta-error">

                            <span class="alert-icon">!</span>

                            <div>${error}</div>

                        </div>

                    </c:if>


                    <!-- NUEVO TICKET -->
                    <c:if test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE'}">

                        <section class="content-card new-ticket-card"
                                 id="nuevo-ticket">

                            <div class="card-heading">

                                <div>

                                    <span class="section-kicker">
                                        NUEVA SOLICITUD
                                    </span>

                                    <h2>Crear ticket</h2>

                                    <p>
                                        Describe el problema y nuestro equipo podrá
                                        comenzar a trabajar en él.
                                    </p>

                                </div>

                                <div class="card-icon">
                                    +
                                </div>

                            </div>


                            <form method="post"
                                  action="${pageContext.request.contextPath}/app/tickets"
                                  class="ticket-form">

                                <div class="form-grid">

                                    <div class="campo campo-full">

                                        <label for="titulo">
                                            Título de la solicitud
                                        </label>

                                        <input type="text"
                                               id="titulo"
                                               name="titulo"
                                               maxlength="150"
                                               placeholder="Ej. No puedo acceder al sistema"
                                               required>

                                    </div>


                                    <div class="campo">

                                        <label for="categoriaId">
                                            Categoría
                                        </label>

                                        <select id="categoriaId"
                                                name="categoriaId"
                                                required>

                                            <option value="">
                                                Selecciona una categoría
                                            </option>

                                            <c:forEach var="categoria"
                                                       items="${categorias}">

                                                <option value="${categoria.id}">
                                                    ${categoria.nombre}
                                                </option>

                                            </c:forEach>

                                        </select>

                                    </div>


                                    <div class="campo campo-full">

                                        <label for="descripcion">
                                            Descripción
                                        </label>

                                        <textarea id="descripcion"
                                                  name="descripcion"
                                                  placeholder="Explica detalladamente qué está ocurriendo..."
                                                  required></textarea>

                                    </div>

                                </div>


                                <div class="form-footer">

                                    <span>
                                        Recibirás seguimiento sobre el estado
                                        de tu solicitud.
                                    </span>

                                    <button type="submit"
                                            class="btn btn-primario">

                                        Crear ticket
                                        <span>→</span>

                                    </button>

                                </div>

                            </form>

                        </section>

                    </c:if>


                    <!-- LISTADO -->
                    <section class="content-card">

                        <div class="card-heading tickets-heading">

                            <div>

                                <span class="section-kicker">
                                    SOLICITUDES
                                </span>

                                <h2>Tickets recientes</h2>

                            </div>

                            <div class="ticket-count">
                                ${empty tickets ? 0 : tickets.size()} registros
                            </div>

                        </div>


                        <c:choose>

                            <c:when test="${empty tickets}">

                                <div class="empty-state">

                                    <div class="empty-icon">
                                        ✓
                                    </div>

                                    <h3>No hay tickets para mostrar</h3>

                                    <p>
                                        Cuando existan solicitudes disponibles,
                                        aparecerán aquí.
                                    </p>

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-wrapper">

                                    <table class="tabla-tickets">

                                        <thead>

                                            <tr>

                                                <th>ID</th>
                                                <th>Solicitud</th>
                                                <th>Categoría</th>
                                                <th>Prioridad</th>
                                                <th>Estado</th>
                                                <th>Solicitante</th>
                                                <th>Agente</th>
                                                <th>Fecha</th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            <c:forEach var="ticket"
                                                       items="${tickets}">

                                                <tr class="ticket-row"
                                                    onclick="window.location = '${pageContext.request.contextPath}/app/ticket?id=${ticket.id}'">

                                                    <td>

                                                        <span class="ticket-id">
                                                            #${ticket.id}
                                                        </span>

                                                    </td>


                                                    <td>

                                                        <div class="ticket-title-cell">

                                                            <strong>
                                                                ${ticket.titulo}
                                                            </strong>

                                                            <span>
                                                                Ver detalles del ticket
                                                            </span>

                                                        </div>

                                                    </td>


                                                    <td>
                                                        ${ticket.categoria}
                                                    </td>


                                                    <td>

                                                        <span class="badge badge-${ticket.prioridad}">
                                                            ${ticket.prioridad}
                                                        </span>

                                                    </td>


                                                    <td>

                                                        <div class="status-stack">

                                                            <span class="badge badge-${ticket.estado}">
                                                                ${ticket.estado}
                                                            </span>

                                                            <c:if test="${ticket.vencido}">

                                                                <span class="badge badge-vencido">
                                                                    SLA vencido
                                                                </span>

                                                            </c:if>

                                                        </div>

                                                    </td>


                                                    <td>
                                                        ${ticket.solicitanteNombre}
                                                    </td>


                                                    <td>

                                                        <c:choose>

                                                            <c:when test="${not empty ticket.agenteNombre}">
                                                                ${ticket.agenteNombre}
                                                            </c:when>

                                                            <c:otherwise>
                                                                <span class="muted">
                                                                    Sin asignar
                                                                </span>
                                                            </c:otherwise>

                                                        </c:choose>

                                                    </td>


                                                    <td>
                                                        ${ticket.fechaCreacion}
                                                    </td>

                                                </tr>

                                            </c:forEach>

                                        </tbody>

                                    </table>

                                </div>

                            </c:otherwise>

                        </c:choose>

                    </section>

                </div>

            </main>

        </div>

    </body>
</html>