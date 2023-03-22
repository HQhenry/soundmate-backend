package com.edu.basaoc.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Setter
    @Column
    /**
     * spotify account id used as username
     */
    private String username;

    @Column
    @Setter
    /**
     * spotify auth credentials used as password
     */
    private String password;

    @Column
    @Setter
    private String accessToken;

    @Column
    @Setter
    private String refreshToken;

    @Column
    @Setter
    private Instant accessExpiresOn;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne(mappedBy = "account")
    private Profile profile;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
