import { useState } from "react";
import { Form, Button, Alert, Row, Col } from "react-bootstrap";
import { crearBebida, actualizarBebida } from "../services/api";

const BEBIDA_VACIA = {
  nombre: "",
  tipo: "ALCOHOLICA",
  volumenML: "",
  stock: "",
  gradosAlcohol: "",
  certificada: false,
  azucarPorLitro: "",
};

export default function BebidaForm({ bebidaEditar, onGuardado }) {
  const [datos, setDatos] = useState(bebidaEditar ?? BEBIDA_VACIA);
  const [campos, setCampos] = useState({});
  const [errorGeneral, setErrorGeneral] = useState(null);
  const [guardando, setGuardando] = useState(false);

  const esEdicion = Boolean(bebidaEditar);

  function actualizarCampo(campo, valor) {
    setDatos((prev) => ({ ...prev, [campo]: valor }));
  }

  async function manejarSubmit(e) {
    e.preventDefault();
    setGuardando(true);
    setCampos({});
    setErrorGeneral(null);

    const payload = {
      ...datos,
      volumenML: Number(datos.volumenML),
      stock: Number(datos.stock),
      gradosAlcohol: datos.tipo === "ALCOHOLICA" ? Number(datos.gradosAlcohol) : null,
      azucarPorLitro: datos.tipo === "SIN_ALCOHOL" ? Number(datos.azucarPorLitro) : null,
      certificada: datos.tipo === "ALCOHOLICA" ? datos.certificada : null,
    };

    try {
      if (esEdicion) {
        await actualizarBebida(bebidaEditar.id, payload);
      } else {
        await crearBebida(payload);
      }
      setDatos(BEBIDA_VACIA);
      onGuardado?.();
    } catch (err) {
      if (err.body?.error === "VALIDACION") {
        setCampos(err.body.campos ?? {});
      } else {
        setErrorGeneral(err.message);
      }
    } finally {
      setGuardando(false);
    }
  }

  return (
    <Form onSubmit={manejarSubmit} className="mb-4">
      <h2 className="h4">{esEdicion ? "Editar bebida" : "Nueva bebida"}</h2>

      {errorGeneral && <Alert variant="danger">{errorGeneral}</Alert>}

      <Form.Group className="mb-2">
        <Form.Label>Nombre</Form.Label>
        <Form.Control
          value={datos.nombre}
          onChange={(e) => actualizarCampo("nombre", e.target.value)}
          isInvalid={Boolean(campos.nombre)}
        />
        <Form.Control.Feedback type="invalid">{campos.nombre}</Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-2">
        <Form.Label>Tipo</Form.Label>
        <Form.Select
          value={datos.tipo}
          onChange={(e) => actualizarCampo("tipo", e.target.value)}
        >
          <option value="ALCOHOLICA">Alcoholica</option>
          <option value="SIN_ALCOHOL">Sin alcohol</option>
        </Form.Select>
      </Form.Group>

      <Row>
        <Col>
          <Form.Group className="mb-2">
            <Form.Label>Volumen (ml)</Form.Label>
            <Form.Control
              type="number"
              value={datos.volumenML}
              onChange={(e) => actualizarCampo("volumenML", e.target.value)}
              isInvalid={Boolean(campos.volumenML)}
            />
            <Form.Control.Feedback type="invalid">{campos.volumenML}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col>
          <Form.Group className="mb-2">
            <Form.Label>Stock</Form.Label>
            <Form.Control
              type="number"
              value={datos.stock}
              onChange={(e) => actualizarCampo("stock", e.target.value)}
              isInvalid={Boolean(campos.stock)}
            />
            <Form.Control.Feedback type="invalid">{campos.stock}</Form.Control.Feedback>
          </Form.Group>
        </Col>
      </Row>

      {datos.tipo === "ALCOHOLICA" && (
        <Row>
          <Col>
            <Form.Group className="mb-2">
              <Form.Label>Grados de alcohol</Form.Label>
              <Form.Control
                type="number"
                step="0.1"
                value={datos.gradosAlcohol}
                onChange={(e) => actualizarCampo("gradosAlcohol", e.target.value)}
                isInvalid={Boolean(campos.isGradosAlcoholValido)}
              />
              <Form.Control.Feedback type="invalid">
                {campos.isGradosAlcoholValido}
              </Form.Control.Feedback>
            </Form.Group>
          </Col>
          <Col className="d-flex align-items-center">
            <Form.Check
              label="Certificada"
              checked={datos.certificada}
              onChange={(e) => actualizarCampo("certificada", e.target.checked)}
            />
          </Col>
        </Row>
      )}

      {datos.tipo === "SIN_ALCOHOL" && (
        <Form.Group className="mb-2">
          <Form.Label>Azucar por litro (g/L)</Form.Label>
          <Form.Control
            type="number"
            value={datos.azucarPorLitro}
            onChange={(e) => actualizarCampo("azucarPorLitro", e.target.value)}
            isInvalid={Boolean(campos.isAzucarPorLitroValido)}
          />
          <Form.Control.Feedback type="invalid">
            {campos.isAzucarPorLitroValido}
          </Form.Control.Feedback>
        </Form.Group>
      )}

      <Button type="submit" disabled={guardando}>
        {guardando ? "Guardando..." : esEdicion ? "Guardar cambios" : "Crear bebida"}
      </Button>
    </Form>
  );
}