package com.ecommercecloud.security.passwordreset.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetMailService {

    private final ObjectProvider<JavaMailSender>
            mailSenderProvider;

    private final boolean mailEnabled;

    private final String fromAddress;

    public PasswordResetMailService(
            ObjectProvider<JavaMailSender>
                    mailSenderProvider,

            @Value(
                "${ecommerce.mail.enabled:false}"
            )
            boolean mailEnabled,

            @Value(
                "${ecommerce.mail.from:no-reply@ecommercecloud.mx}"
            )
            String fromAddress
    ) {
        this.mailSenderProvider =
                mailSenderProvider;

        this.mailEnabled = mailEnabled;

        this.fromAddress = fromAddress;
    }

    public void sendPasswordReset(
            String destination,
            String displayName,
            String resetUrl,
            int expirationMinutes
    ) {
        if (!mailEnabled) {
            System.out.println(
                    "[PASSWORD RESET][LOCAL] "
                    + destination
                    + " -> "
                    + resetUrl
            );

            return;
        }

        JavaMailSender mailSender =
                mailSenderProvider.getIfAvailable();

        if (mailSender == null) {
            throw new IllegalStateException(
                    "El correo está habilitado, " +
                    "pero JavaMailSender no está configurado"
            );
        }

        MimeMessage message =
                mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromAddress);

            helper.setTo(destination);

            helper.setSubject(
                    "Restablece tu contraseña | EComerce Cloud"
            );

            helper.setText(
                    buildHtml(
                            displayName,
                            resetUrl,
                            expirationMinutes
                    ),
                    true
            );

            mailSender.send(message);

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "No fue posible enviar " +
                    "el correo de recuperación",
                    exception
            );
        }
    }

    private String buildHtml(
            String displayName,
            String resetUrl,
            int expirationMinutes
    ) {
        String safeName =
                escapeHtml(displayName);

        String safeUrl =
                escapeHtml(resetUrl);

        return """
                <!DOCTYPE html>
                <html lang="es">
                <body style="
                    margin:0;
                    padding:30px;
                    background:#f4f7fb;
                    font-family:Arial,sans-serif;
                    color:#0f172a;
                ">
                    <table role="presentation"
                           width="100%%"
                           cellspacing="0"
                           cellpadding="0">
                        <tr>
                            <td align="center">

                                <table role="presentation"
                                       width="600"
                                       cellspacing="0"
                                       cellpadding="0"
                                       style="
                                           max-width:600px;
                                           background:#ffffff;
                                           border-radius:20px;
                                           overflow:hidden;
                                           box-shadow:
                                             0 18px 45px
                                             rgba(15,23,42,.10);
                                       ">

                                    <tr>
                                        <td style="
                                            padding:28px;
                                            background:
                                              linear-gradient(
                                                135deg,
                                                #172554,
                                                #2563eb
                                              );
                                            color:#ffffff;
                                        ">
                                            <div style="
                                                font-size:20px;
                                                font-weight:bold;
                                            ">
                                                EComerce Cloud
                                            </div>

                                            <div style="
                                                margin-top:6px;
                                                font-size:12px;
                                                color:#dbeafe;
                                            ">
                                                Recuperación segura
                                                de contraseña
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding:34px">

                                            <h1 style="
                                                margin:0;
                                                font-size:25px;
                                            ">
                                                Hola, %s
                                            </h1>

                                            <p style="
                                                margin:18px 0 0;
                                                color:#475569;
                                                line-height:1.7;
                                            ">
                                                Recibimos una solicitud
                                                para restablecer la
                                                contraseña de tu cuenta.
                                            </p>

                                            <p style="
                                                margin:15px 0 0;
                                                color:#475569;
                                                line-height:1.7;
                                            ">
                                                Utiliza el siguiente botón.
                                                El enlace vencerá en
                                                %d minutos y solo puede
                                                utilizarse una vez.
                                            </p>

                                            <div style="
                                                margin:28px 0;
                                                text-align:center;
                                            ">
                                                <a href="%s"
                                                   style="
                                                     display:inline-block;
                                                     padding:14px 22px;
                                                     border-radius:11px;
                                                     background:#2563eb;
                                                     color:#ffffff;
                                                     text-decoration:none;
                                                     font-weight:bold;
                                                   ">
                                                    Crear nueva contraseña
                                                </a>
                                            </div>

                                            <p style="
                                                margin:0;
                                                color:#64748b;
                                                font-size:12px;
                                                line-height:1.6;
                                            ">
                                                Si no solicitaste este
                                                cambio, ignora el mensaje.
                                                Tu contraseña actual
                                                permanecerá sin cambios.
                                            </p>

                                            <p style="
                                                margin:24px 0 0;
                                                color:#94a3b8;
                                                font-size:11px;
                                                overflow-wrap:anywhere;
                                            ">
                                                %s
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                        safeName,
                        expirationMinutes,
                        safeUrl,
                        safeUrl
                );
    }

    private String escapeHtml(
            String value
    ) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
