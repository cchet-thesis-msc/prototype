package com.gepardec.esb.prototype.integration.services.db.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer")
@NamedQueries({
        @NamedQuery(name = Customer.QUERY_DELETE_ALL_CUSTOMER,
                query = "Delete from Customer")
})
public class Customer extends AbstractEntity<Long> {

    public static final String QUERY_DELETE_ALL_CUSTOMER = "QUERY_DELETE_ALL_CUSTOMER";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "customer_id_generator", sequenceName = "seq_customer_id", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "customer_id_generator", strategy = GenerationType.SEQUENCE)
    @Min(0)
    private Long id;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    /**
     * Holds name in the form of 'first_name;last_name'
     */
    @Column(name = "full_name")
    @NotNull
    @Size(max = 255)
    private String fullName;

    @Column(name = "email")
    @NotNull
    @Size(max = 100)
    @Email
    private String email;

    @NotNull
    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>(0);


    @PrePersist
    public void prePersist() {
        createdAt = modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
