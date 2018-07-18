package com.gepardec.esb.prototype.integration.services.db.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer_order")
@NamedQueries({
        @NamedQuery(name = Order.QUERY_FIND_FOR_CUSTOMER,
                query = "select ord from Order ord inner join fetch ord.customer cust where cust.id = :id"),
        @NamedQuery(name = Order.QUERY_DELETE_ALL_ORDER,
                query = "Delete from Order")
})
public class Order extends AbstractEntity<Long>{

    public static final String QUERY_FIND_FOR_CUSTOMER = "QUERY_FIND_FOR_CUSTOMER";
    public static final String QUERY_DELETE_ALL_ORDER = "QUERY_DELETE_ALL_ORDER";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "order_id_generator", sequenceName = "seq_order_id", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "order_id_generator", strategy = GenerationType.SEQUENCE)
    @Min(0)
    private Long id;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @NotNull
    @Column(name = "delivered_at", updatable = false)
    private LocalDateTime deliveredAt;

    /**
     * Contains data in form of 'item;count;full_price[{,item;count;full_price}]'
     */
    @Column(name = "data")
    @NotNull
    @Size(max = 255)
    private String data;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @PrePersist
    public void prePersist() {
        createdAt = modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
