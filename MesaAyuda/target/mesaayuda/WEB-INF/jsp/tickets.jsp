<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM — Mis tickets</title>
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
    <h1>
        <c:choose>
            <c:when test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE'}">Mis tickets</c:when>
            <c:when test="${sessionScope.usuarioAutenticado.rol == 'AGENTE'}">Tickets asignados a mi</c:when>
            <c:otherwise>Todos los tickets</c:otherwise>
        </c:choose>
    </h1>
    <p class="subtitulo">Consulta el estado de tus solicitudes de soporte.</p>

    <c:if test="${not empty error}">
        <div class="alerta alerta-error">${error}</div>
    </c:if>

    <c:if test="${sessionScope.usuarioAutenticado.rol == 'SOLICITANTE'}">
        <div class="panel">
            <h1 style="font-size:1.1rem; margin-bottom:1rem;">Registrar un ticket nuevo</h1>
            <form method="post" action="${pageContext.request.contextPath}/app/tickets">
                <div class="campo">
                    <label for="titulo">Titulo</label>
                    <input type="text" id="titulo" name="titulo" maxlength="150" required>
                </div>
                <div class="campo">
                    <label for="descripcion">Descripcion</label>
                    <textarea id="descripcion" name="descripcion" required></textarea>
                </div>
                <div class="campo">
                    <label for="categoriaId">Categoria</label>
                    <select id="categoriaId" name="categoriaId" required>
                        <c:forEach var="categoria" items="${categorias}">
                            <option value="${categoria.id}">${categoria.nombre}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primario">Crear ticket</button>
            </form>
        </div>
    </c:if>

    <div class="panel">
        <c:choose>
            <c:when test="${empty tickets}">
                <p class="vacio">Todavia no hay tickets para mostrar aqui.</p>
            </c:when>
            <c:otherwise>
                <table class="tabla-tickets">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Titulo</th>
                        <th>Categoria</th>
                        <th>Prioridad</th>
                        <th>Estado</th>
                        <th>Solicitante</th>
                        <th>Agente</th>
                        <th>Creado</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="ticket" items="${tickets}">
                        <tr onclick="window.location='${pageContext.request.contextPath}/app/ticket?id=${ticket.id}'" style="cursor:pointer;">
                            <td>#${ticket.id}</td>
                            <td class="titulo-ticket">${ticket.titulo}</td>
                            <td>${ticket.categoria}</td>
                            <td><span class="badge badge-${ticket.prioridad}">${ticket.prioridad}</span></td>
                            <td>
                                <span class="badge badge-${ticket.estado}">${ticket.estado}</span>
                                <c:if test="${ticket.vencido}"><span class="badge badge-vencido">Vencido</span></c:if>
                            </td>
                            <td>${ticket.solicitanteNombre}</td>
                            <td>${ticket.agenteNombre}</td>
                            <td>${ticket.fechaCreacion}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
