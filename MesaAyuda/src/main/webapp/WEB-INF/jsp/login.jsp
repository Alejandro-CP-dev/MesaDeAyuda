<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mesa de Ayuda CIMM — Iniciar sesion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<div class="login-envoltorio">
    <div class="login-tarjeta">
        <h1>Mesa de Ayuda CIMM</h1>
        <p class="subtitulo">SENA Regional Boyaca</p>

        <c:if test="${not empty error}">
            <div class="alerta alerta-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="campo">
                <label for="correo">Correo</label>
                <input type="email" id="correo" name="correo" value="${correo}" required autofocus>
            </div>
            <div class="campo">
                <label for="password">Contrasena</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primario" style="width:100%; justify-content:center;">Ingresar</button>
        </form>

        <div class="credenciales-demo">
            Usuarios de prueba (clave <strong>123456</strong> para todos):<br>
            Solicitante: ana.ramirez@sena.edu.co<br>
            Agente: carlos.mendoza@sena.edu.co<br>
            Admin: osan@sena.edu.co
        </div>
    </div>
</div>
</body>
</html>
