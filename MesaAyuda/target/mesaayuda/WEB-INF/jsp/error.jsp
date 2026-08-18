<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>CIMM Desk — Error</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/estilo.css">

    </head>


    <body class="error-page">

        <div class="error-container">

            <div class="error-brand">

                <div class="brand-mark">
                    C
                </div>

                <span>
                    CIMM <strong>DESK</strong>
                </span>

            </div>


            <div class="error-content">

                <div class="error-code">
                    500
                </div>

                <div class="error-icon">
                    !
                </div>

                <span class="eyebrow">
                    ALGO NO SALIÓ COMO ESPERÁBAMOS
                </span>

                <h1>
                    No pudimos completar tu solicitud
                </h1>

                <p>
                    Se produjo un inconveniente al procesar la operación.
                    Puedes volver al centro de tickets e intentarlo nuevamente.
                </p>


                <a href="${pageContext.request.contextPath}/app/tickets"
                   class="btn btn-primario">

                    <span>←</span>
                    Volver a mis tickets

                </a>

            </div>


            <div class="error-footer">
                CIMM Desk · Sistema de gestión de soporte
            </div>

        </div>

    </body>

</html>