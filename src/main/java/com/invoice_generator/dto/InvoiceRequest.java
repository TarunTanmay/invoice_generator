package com.invoice_generator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InvoiceRequest {
    private String invoiceNumber;
    private String customerName;
    private String customerAddress;
    private List<InvoiceItemRequest> items;
    private double totalAmount;
}
