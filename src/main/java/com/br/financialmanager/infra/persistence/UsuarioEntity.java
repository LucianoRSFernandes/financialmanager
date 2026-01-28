package com.br.financialmanager.infra.persistence;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String cpf;
  private String nome;
  private LocalDate nascimento;
  private String email;
  private String senha;

  public UsuarioEntity() {}
  public UsuarioEntity(String cpf, String nome, LocalDate nascimento, String email, String senha) {
    this.cpf = cpf;
    this.nome = nome;
    this.nascimento = nascimento;
    this.email = email;
    this.senha = senha;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return senha;
  }

  @Override
  public String getUsername() {
    return cpf;
  }

  @Override
  public boolean isAccountNonExpired() { return true; }

  @Override
  public boolean isAccountNonLocked() { return true; }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() { return true; }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getCpf() { return cpf; }
  public void setCpf(String cpf) { this.cpf = cpf; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public LocalDate getNascimento() { return nascimento; }
  public void setNascimento(LocalDate nascimento) { this.nascimento = nascimento; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getSenha() { return senha; }
  public void setSenha(String senha) { this.senha = senha; }
}
