package cl.dsy1104.fonda.dto;

import jakarta.validation.constraints.*;

public class VentaRequest {

    @NotNull(message = "bebidaId es obligatorio")
    private Long bebidaId;

    @Min(value = 1, message = "debe ser al menos 1 unidad")
    private int unidades;

    public Long getBebidaId() { return bebidaId; }
    public void setBebidaId(Long bebidaId) { this.bebidaId = bebidaId; }
    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }
}