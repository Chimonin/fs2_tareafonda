package cl.dsy1104.fonda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bebida_id")
    private Bebida bebida;

    private int unidades;
    private int total;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    private String motivo; // null si fue autorizada

    private LocalDateTime fecha = LocalDateTime.now();

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bebida getBebida() { return bebida; }
    public void setBebida(Bebida bebida) { this.bebida = bebida; }
    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public EstadoVenta getEstado() { return estado; }
    public void setEstado(EstadoVenta estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}