package cl.dsy1104.fonda.dto;

import cl.dsy1104.fonda.model.TipoBebida;
import jakarta.validation.constraints.*;

public class BebidaRequest {

    @NotBlank(message = "no puede estar vacio")
    private String nombre;

    @NotNull(message = "el tipo es obligatorio")
    private TipoBebida tipo;

    @Min(value = 100, message = "debe estar entre 100 y 3000")
    @Max(value = 3000, message = "debe estar entre 100 y 3000")
    private int volumenML;

    @Min(value = 0, message = "debe ser mayor o igual a cero")
    private int stock;

    private Double gradosAlcohol;
    private Boolean certificada;
    private Integer azucarPorLitro;

    //getters y setters
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

    //validaciones que dependen del tipo, deben iniciar con is

    @AssertTrue(message = "gradosAlcohol es obligatorio y debe estar entre 0.5 y 45 cuando el tipo es ALCOHOLICA, y debe quedar nulo en el otro caso")
    public boolean isGradosAlcoholValido() {
        if (tipo == null) return true; // deja que @NotNull sobre "tipo" reporte ese error aparte
        if (tipo == TipoBebida.ALCOHOLICA) {
            return gradosAlcohol != null && gradosAlcohol >= 0.5 && gradosAlcohol <= 45;
        } else {
            return gradosAlcohol == null;
        }
    }

    @AssertTrue(message = "azucarPorLitro es obligatorio y debe ser mayor o igual a cero cuando el tipo es SIN_ALCOHOL, y debe quedar nulo en el otro caso")
    public boolean isAzucarPorLitroValido() {
        if (tipo == null) return true;
        if (tipo == TipoBebida.SIN_ALCOHOL) {
            return azucarPorLitro != null && azucarPorLitro >= 0;
        } else {
            return azucarPorLitro == null;
        }
    }
}