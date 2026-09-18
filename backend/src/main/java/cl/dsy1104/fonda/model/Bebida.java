package cl.dsy1104.fonda.model;

import jakarta.persistence.*;

@Entity
public class Bebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoBebida tipo;

    private int volumenML;
    private int stock;

    //con alcohol
    private Double gradosAlcohol;
    private Boolean certificada;

    //sin alcohol
    private Integer azucarPorLitro;

    private boolean ventaRestringida = false;

    //getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoBebida getTipo() { return tipo; }
    public void setTipo(TipoBebida tipo) { this.tipo = tipo; }
    public int getVolumenML() { return volumenML; }
    public void setVolumenML(int volumenML) { this.volumenML = volumenML; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Double getGradosAlcohol() { return gradosAlcohol; }
    public void setGradosAlcohol(Double gradosAlcohol) { this.gradosAlcohol = gradosAlcohol; }
    public Boolean getCertificada() { return certificada; }
    public void setCertificada(Boolean certificada) { this.certificada = certificada; }
    public Integer getAzucarPorLitro() { return azucarPorLitro; }
    public void setAzucarPorLitro(Integer azucarPorLitro) { this.azucarPorLitro = azucarPorLitro; }
    public boolean isVentaRestringida() { return ventaRestringida; }
    public void setVentaRestringida(boolean ventaRestringida) { this.ventaRestringida = ventaRestringida; }
}