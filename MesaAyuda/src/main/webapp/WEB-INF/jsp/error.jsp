<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mesa de Ayuda CIMM — Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<div class="login-envoltorio">
    <div class="login-tarjeta">
        <h1>Algo salio mal</h1>
        <p class="subtitulo">No pudimos completar tu solicitud.</p>
        <a class="btn btn-primario" style="width:100%; justify-content:center;"
           href="${pageContext.request.contextPath}/app/tickets">Volver a mis tickets</a>
    </div>
</div>
</body>
</html>
