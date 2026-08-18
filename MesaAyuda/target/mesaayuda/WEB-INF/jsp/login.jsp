<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>        
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>CIMM Desk — Iniciar sesión</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    </head>

    <body class="login-page">

        <div class="login-shell">

            <section class="login-brand-panel">

                <div class="login-brand">
                    <div class="brand-mark large">C</div>
                    <div>
                        <span class="brand-name">CIMM</span>
                        <span class="brand-product">DESK</span>
                    </div>
                </div>

                <div class="login-hero">
                    <span class="eyebrow">IT SERVICE MANAGEMENT</span>

                    <h1>
                        Todo tu soporte.
                        <strong>En un solo lugar.</strong>
                    </h1>

                    <p>
                        Gestiona solicitudes, asignaciones y tiempos de atención
                        desde una plataforma centralizada.
                    </p>
                </div>

                <div class="login-features">
                    <div class="login-feature">
                        <span class="feature-icon">✓</span>
                        <div>
                            <strong>Gestión de tickets</strong>
                            <span>Centraliza todas las solicitudes.</span>
                        </div>
                    </div>

                    <div class="login-feature">
                        <span class="feature-icon">◷</span>
                        <div>
                            <strong>Control de SLA</strong>
                            <span>Supervisa los tiempos de atención.</span>
                        </div>
                    </div>

                    <div class="login-feature">
                        <span class="feature-icon">↗</span>
                        <div>
                            <strong>Seguimiento en tiempo real</strong>
                            <span>Conoce el estado de cada solicitud.</span>
                        </div>
                    </div>
                </div>

                <div class="login-footer-brand">
                    CIMM · SENA Regional Boyacá
                </div>

            </section>


            <section class="login-form-panel">

                <div class="login-form-wrapper">

                    <div class="mobile-brand">
                        <div class="brand-mark">C</div>
                        <span>CIMM <strong>DESK</strong></span>
                    </div>

                    <div class="form-heading">
                        <span class="eyebrow">ACCESO AL SISTEMA</span>
                        <h2>Bienvenido de nuevo</h2>
                        <p>Ingresa tus credenciales para continuar.</p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alerta alerta-error">
                            <span class="alert-icon">!</span>
                            <div>${error}</div>
                        </div>
                    </c:if>

                    <form method="post"
                          action="${pageContext.request.contextPath}/login"
                          class="login-form">

                        <div class="campo">
                            <label for="correo">Correo electrónico</label>

                            <div class="input-wrapper">
                                <span class="input-icon">@</span>
                                <input type="email"
                                       id="correo"
                                       name="correo"
                                       value="${correo}"
                                       placeholder="nombre@empresa.com"
                                       autocomplete="email"
                                       required
                                       autofocus>
                            </div>
                        </div>

                        <div class="campo">
                            <div class="label-row">
                                <label for="password">Contraseña</label>
                            </div>

                            <div class="input-wrapper">
                                <span class="input-icon">●</span>
                                <input type="password"
                                       id="password"
                                       name="password"
                                       placeholder="Ingresa tu contraseña"
                                       autocomplete="current-password"
                                       required>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primario btn-login">
                            <span>Iniciar sesión</span>
                            <span class="btn-arrow">→</span>
                        </button>

                    </form>

                    <div class="demo-access">

                        <div class="demo-header">
                            <span class="demo-icon">i</span>
                            <div>
                                <strong>Entorno de demostración</strong>
                                <span>Credenciales disponibles para pruebas</span>
                            </div>
                        </div>

                        <div class="demo-password">
                            Contraseña:
                            <strong>123456</strong>
                        </div>

                        <div class="demo-users">
                            <div>
                                <span>Solicitante</span>
                                <small>ana.ramirez@sena.edu.co</small>
                            </div>

                            <div>
                                <span>Agente</span>
                                <small>carlos.mendoza@sena.edu.co</small>
                            </div>

                            <div>
                                <span>Administrador</span>
                                <small>osan@sena.edu.co</small>
                            </div>
                        </div>

                    </div>

                </div>

            </section>

        </div>

    </body>
</html>