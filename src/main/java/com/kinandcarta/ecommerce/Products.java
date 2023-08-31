package com.kinandcarta.ecommerce;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Builder
@AllArgsConstructor
@Entity
@Table(name = "products")
@Slf4j
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "varchar(200) not null ''", nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "varchar(255) null default ''", unique = true)
    private String productDescription;

    @Column(columnDefinition = "varchar(255) null default ''")
    private String image;

    @NotNull
    @Column(columnDefinition = "decimal(38, 2) not null default 0")
    private BigDecimal unitPrice;

    @CreationTimestamp
    @Column(name="created_dt")
    private Instant createDateTime;

    @UpdateTimestamp
    @Column(name="updated_dt")
    private Instant updateDateTime;
}
