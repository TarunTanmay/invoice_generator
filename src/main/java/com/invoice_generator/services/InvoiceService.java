package com.invoice_generator.services;

import com.invoice_generator.dto.InvoiceItemRequest;
import com.invoice_generator.dto.InvoiceRequest;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class InvoiceService {

    public byte[] generateInvoicePDF(InvoiceRequest invoice) throws IOException {
        // ByteArrayOutputStream to capture the PDF in memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create a PdfWriter instance
        PdfWriter writer = new PdfWriter(baos);

        // Create a PdfDocument instance for iText 7
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Create a Document instance (iText 7)
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

        // Add invoice title
        document.add(new Paragraph("Invoice"));

        // Add customer details
        document.add(new Paragraph("Customer: " + invoice.getCustomerName()));
        document.add(new Paragraph("Address: " + invoice.getCustomerAddress()));
        document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));

        // Add items table
        Table table = new Table(4); // 4 columns (Item Name, Quantity, Price, Total)
        table.addCell(new Cell().add(new Paragraph("Item Name").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().add(new Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().add(new Paragraph("Price").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().add(new Paragraph("Total").setTextAlignment(TextAlignment.CENTER)));

        // Add each item from the invoice
        for (InvoiceItemRequest item : invoice.getItems()) {
            table.addCell(item.getItemName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf(item.getPrice()));
            table.addCell(String.valueOf(item.getPrice() * item.getQuantity()));
        }

        // Add the table to the document
        document.add(table);

        // Add total amount
        document.add(new Paragraph("Total Amount: " + invoice.getTotalAmount()));

        // Close the document
        document.close();

        // Return the PDF byte array
        return baos.toByteArray();
    }
}
