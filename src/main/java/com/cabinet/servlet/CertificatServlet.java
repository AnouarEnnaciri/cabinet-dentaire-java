package com.cabinet.servlet;

import com.cabinet.dao.CertificatDAO;
import com.cabinet.dao.ConsultationDAO;
import com.cabinet.dao.PatientDAO;
import com.cabinet.model.Certificat;
import com.cabinet.model.Consultation;
import com.cabinet.model.Patient;
import com.cabinet.model.Utilisateur;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/certificat")
public class CertificatServlet extends HttpServlet {

    private final PatientDAO patientDAO = new PatientDAO();
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final CertificatDAO certificatDAO = new CertificatDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        String action = request.getParameter("action");
        if (!"save".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        if (role == null || (!role.equals("medecin") && !role.equals("admin"))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String patientIdStr = request.getParameter("patientId");
        String consultationIdStr = request.getParameter("consultationId");
        String type = request.getParameter("type");

        if (patientIdStr == null || consultationIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        if (type == null || type.isEmpty()) {
            type = "standard";
        }

        long patientId;
        long consultationId;

        try {
            patientId = Long.parseLong(patientIdStr);
            consultationId = Long.parseLong(consultationIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return;
        }

        Patient patient = patientDAO.findById(patientId);
        Consultation consultation = consultationDAO.getById(consultationId);
        Utilisateur user = (Utilisateur) session.getAttribute("user");

        if (patient == null || consultation == null || user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Data not found");
            return;
        }

        String content = buildContent(patient, consultation, user, type);

        Certificat certificat = new Certificat(
                (int) patientId,
                patient.getName(),
                (int) consultationId,
                type,
                content
        );

        certificatDAO.save(certificat);
        generatePDF(response, patient, consultation, user, content);
    }

    private String buildContent(Patient patient, Consultation consultation, Utilisateur user, String type) {
        StringBuilder sb = new StringBuilder();

        sb.append("Patient: ").append(patient.getName()).append("\n");

        if (patient.getAge() > 0) {
            sb.append("Âge: ").append(patient.getAge()).append(" ans\n");
        }

        sb.append("Consultation du: ").append(consultation.getDate()).append("\n");
        sb.append("Motif: ").append(consultation.getDiagnostic() != null ? consultation.getDiagnostic()
                : "Consultation dentaire").append("\n\n");

        switch (type) {
            case "arret-travail":
                sb.append("Arrêt de travail médical justifié.\n");
                break;
            case "scolaire":
                sb.append("Certificat pour usage scolaire.\n");
                break;
            case "sport":
                sb.append("Aptitude à la pratique sportive confirmée.\n");
                break;
            default:
                sb.append("État de santé compatible avec les activités normales.\n");
        }

        sb.append("\nFait à Tanger, le ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return sb.toString();
    }

    private void generatePDF(HttpServletResponse response,
                             Patient patient,
                             Consultation consultation,
                             Utilisateur user,
                             String content) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=certificat_" + patient.getId() + ".pdf");

        try (OutputStream out = response.getOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Title
            Paragraph title = new Paragraph("CERTIFICAT MÉDICAL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Doctor info
            Paragraph doctor = new Paragraph("Dr. " + user.getNom(), boldFont);
            doctor.setSpacingAfter(10);
            document.add(doctor);

            // Date
            Paragraph date = new Paragraph("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont);
            date.setSpacingAfter(15);
            document.add(date);

            // Content
            Paragraph body = new Paragraph(content, normalFont);
            body.setSpacingAfter(20);
            document.add(body);

            // Signature
            Paragraph signature = new Paragraph("Signature du médecin", boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            signature.setSpacingBefore(30);
            document.add(signature);

            document.close();

        } catch (DocumentException e) {
            throw new IOException("PDF generation error", e);
        }
    }
}