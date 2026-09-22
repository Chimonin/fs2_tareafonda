import { useState, useEffect } from "react";
import { Table, Alert, Spinner, Badge } from "react-bootstrap";
import { listarVentas } from "../services/api";

export default function VentaHistorial({ refrescoTrigger }) {
  const [ventas, setVentas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setCargando(true);
    listarVentas()
      .then((datos) => {
        setVentas(datos);
        setError(null);
      })
      .catch((err) => setError(err.message))
      .finally(() => setCargando(false));
  }, [refrescoTrigger]);

  return (
    <div className="mb-4">
      <h2 className="h4">Historial de ventas</h2>

      {error && <Alert variant="danger">{error}</Alert>}

      {cargando ? (
        <Spinner animation="border" size="sm" />
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>Bebida</th>
              <th>Unidades</th>
              <th>Total</th>
              <th>Estado</th>
              <th>Motivo</th>
            </tr>
          </thead>
          <tbody>
            {ventas.map((v) => (
              <tr key={v.id}>
                <td>{v.nombre}</td>
                <td>{v.unidades}</td>
                <td>${v.total}</td>
                <td>
                {/*verde si fue autorizada, rojo si fue rechazada */}
                  <Badge bg={v.estado === "AUTORIZADA" ? "success" : "danger"}>
                    {v.estado}
                  </Badge>
                </td>
                {/*si lo de la izquierda es null, usa lo de la derecha*/}
                <td>{v.motivo ?? "-"}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
}