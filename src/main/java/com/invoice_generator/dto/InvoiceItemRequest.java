package com.invoice_generator.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoiceItemRequest {
    private String itemName;
    private double price;
    private int quantity;
}
