package com.invoice_generator.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "invoice_id")  // The foreign key in InvoiceItem
    private Invoice invoice;
}
