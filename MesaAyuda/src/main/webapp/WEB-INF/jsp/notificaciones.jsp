<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>CIMM Desk — Notificaciones</title>

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
                       class="nav-item active">

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

                            <h1>Notificaciones</h1>

                            <p>
                                Avisos generados por cada cambio de estado de
                                tus tickets (RF-08).
                            </p>

                        </div>

                    </div>


                    <!-- LISTADO -->
                    <section class="content-card">

                        <div class="card-heading compact">

                            <div>
                                <span class="section-kicker">BANDEJA</span>
                                <h2>Tus notificaciones</h2>
                            </div>

                            <div class="ticket-count">
                                ${empty notificaciones ? 0 : notificaciones.size()} registros
                            </div>

                        </div>


                        <c:choose>

                            <c:when test="${empty notificaciones}">

                                <div class="empty-state">

                                    <div class="empty-icon">
                                        🔔
                                    </div>

                                    <h3>No tienes notificaciones todavia</h3>

                                    <p>
                                        Aqui apareceran los avisos cada vez que
                                        uno de tus tickets cambie de estado.
                                    </p>

                                </div>

                            </c:when>


                            <c:otherwise>

                                <c:forEach var="notificacion" items="${notificaciones}">

                                    <div class="comentario">

                                        <div class="comment-avatar">
                                            🔔
                                        </div>

                                        <div class="comment-content">

                                            <div class="comment-header">
                                                <strong>${notificacion.asunto}</strong>
                                                <span>${notificacion.fechaEnvio} · ${notificacion.canal}</span>
                                            </div>

                                            <p>${notificacion.mensaje}</p>

                                        </div>

                                    </div>

                                </c:forEach>

                            </c:otherwise>

                        </c:choose>

                    </section>

                </div>

            </main>

        </div>

    </body>
</html>
