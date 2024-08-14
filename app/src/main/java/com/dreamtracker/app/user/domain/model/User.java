package com.dreamtracker.app.user.domain.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private UUID uuid;
   private String name;
   private String surname;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of();
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   @Column(nullable = false)
   private String fullName;

   @Column(unique = true, length = 100, nullable = false)
   private String email;
//
   @Column(nullable = false)
   private String password;

   @CreationTimestamp
   @Column(updatable = false, name = "created_at")
   private Date createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private Date updatedAt;
}
