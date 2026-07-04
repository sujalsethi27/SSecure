package com.project2.mailjwt.entities;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class userEntity {


@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Provider provider = Provider.LOCAL;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
private Long id;
  @Column(unique = true)
  private String userId;
private String name;
@Column(unique = true)
private String email;
private String password;
private String verifyOtp;
private boolean isAccountVerified;
private Long verifyOtpExpired;
private String resetOtp;
private Long resetOtpExpiredat;
@CreationTimestamp
@Column(updatable = false) 
private LocalDateTime createdAt;
@UpdateTimestamp
private LocalDateTime updatedAt;
}
