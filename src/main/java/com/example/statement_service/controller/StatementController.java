package com.example.statement_service.controller;

import com.example.statement_service.entity.Statement;
import com.example.statement_service.entity.TransactionDTO;
import com.example.statement_service.repository.StatementRepository;
import com.example.statement_service.service.PdfService;
import com.example.statement_service.service.StatementService;
import com.example.statement_service.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statements")
public class StatementController {
    private static final Logger logger = LoggerFactory.getLogger(StatementController.class);

    @Autowired
    private StatementService statementService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private StatementRepository statementRepository;


    @GetMapping
    public List<Statement> getStatements(
            @RequestParam String accountId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        return statementRepository
                .findByAccountIdAndFromDateGreaterThanEqualAndToDateLessThanEqual(
                        accountId,
                        LocalDate.parse(fromDate),
                        LocalDate.parse(toDate)
                );
    }


    // Endpoint pour télécharger le PDF
    @GetMapping("/download/{statementId}")

    public ResponseEntity<byte[]> downloadPdf(@PathVariable String statementId) {

        try {
            Statement statement = statementRepository.findById(statementId)
                    .orElseThrow(() -> new RuntimeException("Statement not found"));


            List<TransactionDTO> filtered =
                    statementService.getTransactions(
                            statement.getAccountId(),
                            statement.getFromDate(),
                            statement.getToDate()
                    );

            // 2️⃣ Préparer les données pour le template
            Map<String, Object> data = Map.of(
                    "accountId", statement.getAccountId(),
                    "generatedDate", statement.getGeneratedDate(),
                    "fromDate", statement.getFromDate(),
                    "toDate", statement.getToDate(),
                    "openingBalance", statement.getOpeningBalance(),
                    "closingBalance", statement.getClosingBalance(),
                    "totalCredit", statement.getTotalCredit(),
                    "totalDebit", statement.getTotalDebit(),
                    "transactions", filtered
            );

            // 3️⃣ Générer le HTML via Handlebars
            String html = templateService.generateHtml(data);
            logger.info("HTML généré : \n" + html); // debug

            // 4️⃣ Générer le PDF dans un ByteArrayOutputStream
            byte[] pdf = pdfService.generatePdf(html);


            // 5️⃣ Retourner le PDF au client
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace(); // Affiche l'erreur complète dans la console
            return ResponseEntity.status(500)
                    .body(("Erreur lors de la génération du PDF : " + e.getMessage()).getBytes());
        }
    }


}