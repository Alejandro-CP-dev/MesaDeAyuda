<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>CIMM Desk — Reportes</title>

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
                       class="nav-item">

                        <span class="nav-icon">▦</span>

                        <span>Tickets</span>

                    </a>

                    <a href="${pageContext.request.contextPath}/app/notificaciones"
                       class="nav-item">

                        <span class="nav-icon">🔔</span>

                        <span>Notificaciones</span>

                    </a>

                    <a href="${pageContext.request.contextPath}/app/reportes"
                       class="nav-item active">

                        <span class="nav-icon">📊</span>

                        <span>Reportes</span>

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

                            <h1>Reportes</h1>

                            <p>
                                Metricas generales de los tickets: por estado,
                                por agente y los que ya vencieron su SLA.
                            </p>

                        </div>

                    </div>


                    <!-- MÉTRICAS PRINCIPALES -->
                    <div class="stats-grid">

                        <div class="stat-card">

                            <div class="stat-icon stat-blue">
                                ▦
                            </div>

                            <div>

                                <span class="stat-label">
                                    Total de tickets
                                </span>

                                <strong class="stat-value">
                                    ${metricas.totalTickets}
                                </strong>

                            </div>

                        </div>


                        <div class="stat-card">

                            <div class="stat-icon stat-purple">
                                👤
                            </div>

                            <div>

                                <span class="stat-label">
                                    Agentes en el sistema
                                </span>

                                <strong class="stat-value">
                                    ${metricas.porAgente.size()}
                                </strong>

                            </div>

                        </div>


                        <div class="stat-card">

                            <div class="stat-icon ${metricas.ticketsVencidos > 0 ? 'stat-blue' : 'stat-green'}"
                                 style="${metricas.ticketsVencidos > 0 ? 'color:#dc2626;background:#fee2e2;' : ''}">
                                ${metricas.ticketsVencidos > 0 ? '!' : '✓'}
                            </div>

                            <div>

                                <span class="stat-label">
                                    SLA vencidos
                                </span>

                                <strong class="stat-value">
                                    ${metricas.ticketsVencidos}
                                </strong>

                            </div>

                        </div>

                    </div>


                    <!-- TICKETS POR ESTADO -->
                    <section class="content-card">

                        <div class="card-heading compact">

                            <div>

                                <span class="section-kicker">
                                    CICLO DE VIDA
                                </span>

                                <h2>Tickets por estado</h2>

                            </div>

                        </div>


                        <div class="table-wrapper">

                            <table class="tabla-tickets">

                                <thead>

                                    <tr>
                                        <th>Estado</th>
                                        <th>Cantidad</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <c:forEach var="fila" items="${metricas.porEstado}">

                                        <tr>

                                            <td>
                                                <span class="badge badge-${fila.estado}">
                                                    ${fila.estado}
                                                </span>
                                            </td>

                                            <td>${fila.cantidad}</td>

                                        </tr>

                                    </c:forEach>

                                </tbody>

                            </table>

                        </div>

                    </section>


                    <!-- TICKETS POR AGENTE -->
                    <section class="content-card">

                        <div class="card-heading compact">

                            <div>

                                <span class="section-kicker">
                                    CARGA DE TRABAJO
                                </span>

                                <h2>Tickets por agente</h2>

                            </div>

                        </div>


                        <c:choose>

                            <c:when test="${empty metricas.porAgente}">

                                <div class="empty-state">

                                    <div class="empty-icon small">
                                        👤
                                    </div>

                                    <h3>No hay agentes registrados todavia</h3>

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-wrapper">

                                    <table class="tabla-tickets">

                                        <thead>

                                            <tr>
                                                <th>Agente</th>
                                                <th>Asignados (total)</th>
                                                <th>Abiertos ahora</th>
                                            </tr>

                                        </thead>

                                        <tbody>

                                            <c:forEach var="fila" items="${metricas.porAgente}">

                                                <tr>
                                                    <td>${fila.nombreAgente}</td>
                                                    <td>${fila.cantidadAsignados}</td>
                                                    <td>${fila.cantidadAbiertos}</td>
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
