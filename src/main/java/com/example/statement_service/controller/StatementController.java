package com.example.statement_service.controller;

import com.example.statement_service.entity.Statement;
import com.example.statement_service.entity.StatementSummaryDTO;
import com.example.statement_service.entity.TransactionDTO;
import com.example.statement_service.repository.StatementRepository;
import com.example.statement_service.service.PdfService;
import com.example.statement_service.service.StatementService;
import com.example.statement_service.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Statements API", description = "Bank Statement Management")
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

    @Operation(summary = "Get statements by account and date range")
    @GetMapping
    public List<StatementSummaryDTO> getStatements(
            @RequestParam String accountId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        return statementRepository
                .findByAccountIdAndFromDateGreaterThanEqualAndToDateLessThanEqual(
                        accountId,
                        LocalDate.parse(fromDate),
                        LocalDate.parse(toDate)
                )
                .stream()
                .map(s -> {
                    StatementSummaryDTO dto = new StatementSummaryDTO();
                    dto.setId(s.getId());
                    dto.setAccountId(s.getAccountId());
                    dto.setFromDate(s.getFromDate());
                    dto.setToDate(s.getToDate());
                    dto.setOpeningBalance(s.getOpeningBalance());
                    dto.setClosingBalance(s.getClosingBalance());
                    dto.setTotalCredit(s.getTotalCredit());
                    dto.setTotalDebit(s.getTotalDebit());
                    return dto;
                })
                .toList();
    }

    @Operation(summary = "Download statement as PDF")
    // Endpoint to download the PDF
    @GetMapping("/download/{statementId}")

    public ResponseEntity<byte[]> downloadPdf(@PathVariable String statementId) {

        try {
            Statement statement = statementRepository.findById(statementId)
                    .orElseThrow(() -> new RuntimeException("Statement not found"));


            List<TransactionDTO> transactions = statement.getTransactions();

            if (transactions == null) {
                transactions = List.of();
            }

            //Prepare the data for the template
            Map<String, Object> data = Map.of(
                    "accountId", statement.getAccountId(),
                    "generatedDate", statement.getGeneratedDate(),
                    "fromDate", statement.getFromDate(),
                    "toDate", statement.getToDate(),
                    "openingBalance", statement.getOpeningBalance(),
                    "closingBalance", statement.getClosingBalance(),
                    "totalCredit", statement.getTotalCredit(),
                    "totalDebit", statement.getTotalDebit(),
                    "transactions", transactions
            );

            //Generate HTML using Handlebars
            String html = templateService.generateHtml(data);
            logger.info("Generated HTML : \n" + html); // debug

            //Generate the PDF in a ByteArrayOutputStream
            byte[] pdf = pdfService.generatePdf(html);


            //Return the PDF to the client
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace(); // Displays the full error message in the console
            return ResponseEntity.status(500)
                    .body(("Error generating the PDF : " + e.getMessage()).getBytes());
        }
    }


}