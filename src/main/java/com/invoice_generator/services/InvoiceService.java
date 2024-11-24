package com.invoice_generator.services;

import com.invoice_generator.dto.InvoiceItemRequest;
import com.invoice_generator.dto.InvoiceRequest;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

@Service
public class InvoiceService {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#.00");

    public static byte[] generateInvoicePDF(InvoiceRequest request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // --- Branding Section ---
        try {
            Image logo = new Image(ImageDataFactory.create("/path/to/logo.png"))
                    .setWidth(100).setHeight(50);
            document.add(logo);
        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }
        document.add(new Paragraph("Company Name")
                .setFontSize(20).setBold().setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("253, Spicy hub sector 39")
                .setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("\n")); // Add some spacing

        // --- Invoice Header Section ---
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 2}));
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.addCell(new Cell().add(new Paragraph("Invoice Number: "+request.getInvoiceNumber()).setBold())
                .setBorder(null));
        headerTable.addCell(new Cell().add(new Paragraph("Date: 2024-11-23").setTextAlignment(TextAlignment.RIGHT))
                .setBorder(null));
        document.add(headerTable);
        document.add(new Paragraph("\n")); // Add some spacing

        // --- Customer Details ---
        document.add(new Paragraph("Customer Details")
                .setFontSize(14).setBold().setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Name: "+request.getCustomerName()));
        document.add(new Paragraph("Address: "+request.getCustomerAddress()));
        document.add(new Paragraph("\n"));

        // --- Table Section ---
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Table Header
        DeviceRgb headerColor = new DeviceRgb(63, 81, 181); // Blue color
        table.addHeaderCell(new Cell().add(new Paragraph("Item Name").setBold())
                .setBackgroundColor(headerColor).setFontColor(DeviceRgb.WHITE).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold())
                .setBackgroundColor(headerColor).setFontColor(DeviceRgb.WHITE).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold())
                .setBackgroundColor(headerColor).setFontColor(DeviceRgb.WHITE).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold())
                .setBackgroundColor(headerColor).setFontColor(DeviceRgb.WHITE).setTextAlignment(TextAlignment.CENTER));

        // Add Item Rows
        double totalAmount = 0;
        for (InvoiceItemRequest item: request.getItems()) {
            double total = item.getQuantity() * item.getPrice();
            table.addCell(new Paragraph(item.getItemName()));
            table.addCell(new Paragraph(String.valueOf(item.getQuantity())).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Paragraph(CURRENCY_FORMAT.format(item.getPrice())).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph(CURRENCY_FORMAT.format(total)).setTextAlignment(TextAlignment.RIGHT));
            totalAmount += total;
        }

        document.add(table);

        // --- Total Amount ---
        document.add(new Paragraph("\n")); // Add spacing
        Paragraph totalParagraph = new Paragraph("Total: " + CURRENCY_FORMAT.format(totalAmount))
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.RIGHT);
        document.add(totalParagraph);

        // --- Footer Section ---
        document.add(new Paragraph("\n")); // Add spacing
        document.add(new Paragraph("Thank you for your business!")
                .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Contact us: support@company.com | +123 456 7890")
                .setFontSize(10).setTextAlignment(TextAlignment.CENTER));

        // Close Document
        document.close();
        return baos.toByteArray();
    }
}
