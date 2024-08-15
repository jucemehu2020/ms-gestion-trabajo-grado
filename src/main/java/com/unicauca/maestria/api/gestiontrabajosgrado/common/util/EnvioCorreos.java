package com.unicauca.maestria.api.gestiontrabajosgrado.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class EnvioCorreos {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public boolean enviarCorreoConAnexos(ArrayList<String> correos, String asunto, String mensaje,
            Map<String, Object> documentos) {

        try {
            Map<String, Object> templateModel = new HashMap<>();

            for (String correo : correos) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                String saludo = obtenerSaludo();

                templateModel.put("mensaje_saludo", saludo);
                templateModel.put("mensaje", mensaje);

                Context context = new Context();
                context.setVariables(templateModel);

                String html = templateEngine.process("emailTemplate", context);

                helper.setTo(correo);
                helper.setSubject(asunto);
                helper.setText(html, true);

                for (Map.Entry<String, Object> entry : documentos.entrySet()) {
                    String nombreDocumento = entry.getKey();
                    Object valorDocumento = entry.getValue();

                    if (valorDocumento instanceof String) {
                        String base64Documento = (String) valorDocumento;
                        byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
                        ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes,
                                Constants.aplicacionExtension);
                        helper.addAttachment(nombreDocumento + Constants.extension, dataSource);
                    } else if (valorDocumento instanceof List) {
                        List<String> listaAnexos = (List<String>) valorDocumento;
                        for (int i = 0; i < listaAnexos.size(); i++) {
                            String base64Anexo = listaAnexos.get(i);
                            byte[] anexoBytes = Base64.getDecoder().decode(base64Anexo);
                            ByteArrayDataSource dataSource = new ByteArrayDataSource(anexoBytes,
                                    Constants.aplicacionExtension);
                            helper.addAttachment(nombreDocumento + "_" + (i + 1) + Constants.extension, dataSource);
                        }
                    }
                }

                mailSender.send(message);
            }

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarCorreoEvaluadores(String correo, String asunto, String mensaje,
            Map<String, Object> documentos) {

        try {
            Map<String, Object> templateModel = new HashMap<>();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String saludo = obtenerSaludo();

            templateModel.put("mensaje_saludo", saludo);
            templateModel.put("mensaje", mensaje);

            Context context = new Context();
            context.setVariables(templateModel);

            String html = templateEngine.process("emailTemplate", context);

            helper.setTo(correo);
            helper.setSubject(asunto);
            helper.setText(html, true);

            for (Map.Entry<String, Object> entry : documentos.entrySet()) {
                String nombreDocumento = entry.getKey();
                Object valorDocumento = entry.getValue();

                String extension;
                String mimeType;
                if (nombreDocumento.equals("formatoB") || nombreDocumento.equals("formatoCEv1") ||
                        nombreDocumento.equals("formatoCEv2")) {
                    extension = ".docx";
                    mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                } else {
                    extension = ".pdf";
                    mimeType = "application/pdf";
                }

                if (valorDocumento instanceof String) {
                    String base64Documento = (String) valorDocumento;
                    byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
                    ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes, mimeType);
                    helper.addAttachment(nombreDocumento + extension, dataSource);
                } else if (valorDocumento instanceof List) {
                    List<String> listaAnexos = (List<String>) valorDocumento;
                    for (int i = 0; i < listaAnexos.size(); i++) {
                        String base64Anexo = listaAnexos.get(i);
                        byte[] anexoBytes = Base64.getDecoder().decode(base64Anexo);
                        ByteArrayDataSource dataSource = new ByteArrayDataSource(anexoBytes, mimeType);
                        helper.addAttachment(nombreDocumento + "_" + (i + 1) + extension, dataSource);
                    }
                }
            }

            mailSender.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarCorreosUnico(String correo, String asunto, String mensaje) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String saludo = obtenerSaludo();

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("title", asunto);
            templateModel.put("mensaje_saludo", saludo);
            templateModel.put("mensaje", mensaje);

            Context context = new Context();
            context.setVariables(templateModel);

            String html = templateEngine.process("emailTemplate", context);

            helper.setTo(correo);
            helper.setSubject(asunto);
            helper.setText(html, true);

            mailSender.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarCorreosCorrecion(ArrayList<String> correos, String asunto, String mensaje) {

        try {
            for (String correo : correos) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                String saludo = obtenerSaludo();

                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("title", asunto);
                templateModel.put("mensaje_saludo", saludo);
                templateModel.put("mensaje", mensaje);

                Context context = new Context();
                context.setVariables(templateModel);

                String html = templateEngine.process("emailTemplate", context);

                helper.setTo(correo);
                helper.setSubject(asunto);
                helper.setText(html, true);

                mailSender.send(message);
            }

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String obtenerSaludo() {
        ZonedDateTime nowInColombia = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        LocalTime currentTime = nowInColombia.toLocalTime();

        String saludo;
        if (currentTime.isBefore(LocalTime.NOON)) {
            saludo = "Buenos días";
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            saludo = "Buenas tardes";
        } else {
            saludo = "Buenas noches";
        }
        return saludo;
    }
}
