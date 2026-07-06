package com.nocta.myown.entity;


import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
@Data
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usuario_id")
	private Integer usuarioId;
	
	@Column(name = "nombre", length = 150)
	private String nombre;
	
	@Column(name = "email", length = 150)
	private String email;
	
	@Column(name = "google_id")
	private String googleId;
	
	@Column(name = "telefono", length = 50)
	private String telefono;
	
	@Column(name = "cuil_cuit", length = 50)
	private String cuilCuit;
	
	@Column(name = "password_hash")
	private String passwordHash;
	
	@Column(name = "localidad", length = 150)
	private String localidad;
	
	@Column(name = "matricula", length = 100)
	private String matricula;
	
	@Column(columnDefinition = "TEXT")
	private String descripcion;
	
	@Column(name = "nombre_comercial", length = 150)
	private String nombreComercial;
	
	@Column(name = "foto_url", length = 500)
	private String fotoUrl;
	
	@Column(name = "perfil_completo")
	private Boolean perfilCompleto;
	
	@Column(name = "plan", length = 50)
	private String plan;
	
	@Column(name = "suscripcion_activa")
	private Boolean suscripcionActiva;

	private Boolean activo;
	
	@Column(name = "fecha_alta")
	private LocalDateTime  fechaAlta;
	
	@Column(name = "ultimo_login")
	private LocalDateTime  ultimoLogin;
	
	@Column(name = "updated_at")
	private LocalDateTime  updatedAt;
	
	@Column(name = "proveedor_auth", length = 20)
	private String proveedorAuth;
	
	@PrePersist
	public void prePersist() {
	    if (activo == null) activo = true;
	    if (perfilCompleto == null) perfilCompleto = false;
	    if (plan == null) plan = "FREE";
	    if (suscripcionActiva == null) suscripcionActiva = false;
	    if (proveedorAuth == null) proveedorAuth = "EMAIL";

	    LocalDateTime ahora = LocalDateTime.now();

	    if (fechaAlta == null) fechaAlta = ahora;
	    if (updatedAt == null) updatedAt = ahora;
	}

	@PreUpdate
	public void preUpdate() {
	    updatedAt = LocalDateTime.now();
	}
}
