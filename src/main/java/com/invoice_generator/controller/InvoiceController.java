package com.invoice_generator.controller;

import com.invoice_generator.dto.InvoiceRequest;
import com.invoice_generator.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Endpoint to generate invoice PDF
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateInvoice(@RequestBody InvoiceRequest invoice) {
        try {
            byte[] pdfData = invoiceService.generateInvoicePDF(invoice);

            // Set PDF response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "inline; filename=invoice.pdf");

            // Return the generated PDF
            return ResponseEntity.ok().headers(headers).body(pdfData);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
