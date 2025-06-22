package com.kozak.mybookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE order_items SET isDeleted = true where id=?")
@SQLRestriction("is_deleted = false")
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne
    private Order order;
    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne
    private Book book;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Boolean isDeleted;
}
