package com.gepardec.esb.prototype.integration.services.db.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "seq_customer_id", initialValue = 0, allocationSize = 1)
    @GeneratedValue(generator = "seq_customer_id", strategy = GenerationType.SEQUENCE)
    @Min(0)
    private Long id;

    @Column(name = "first_name")
    @NotNull
    @Size(max = 50)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(max = 100)
    private String lastName;

    @Column(name = "email")
    @NotNull
    @Size(max = 100)
    @Email
    private String email;
}
