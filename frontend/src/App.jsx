import { Container } from "react-bootstrap";
import { useState } from "react";
import BebidaList from "./components/BebidaList";
import BebidaForm from "./components/BebidaForm";
import VentaForm from "./components/VentaForm";
import VentaHistorial from "./components/VentaHistorial";

/**
 * Estructura sugerida de la interfaz. Cada bloque es un componente propio
 * dentro de src/components/:
 *
 *   BebidaList      tabla del catalogo, con filtro por nombre
 *   BebidaForm      alta y edicion de una bebida
 *   VentaForm       registro de una venta
 *   VentaHistorial  listado de ventas con su estado y motivo
 *
 * Ningun componente calcula precios ni decide si una venta se autoriza:
 * esos datos vienen del backend.
 */
export default function App() {

  //null es crear bebida, si ya hay bebida es editar
  const [bebidaSeleccionada, setBebidaSeleccionada] = useState(null);

  //aumenta cada vez para avisarle a la lista de bebidas que recargue
  const [refrescoTrigger, setRefrescoTrigger] = useState(0);

  //aumenta cada vez para avisarle al historial que recargue
  const [refrescoVentasTrigger, setRefrescoVentasTrigger] = useState(0);

  //el boton de editar llama a oneditar, y oneditar llama a manejareditar
  //manejar editar setea la bebida
  function manejarEditar(bebida) {
    setBebidaSeleccionada(bebida);
  }

  //volver la bebida null es para poder volver al form modo crear
  //modificamos el numero de regrescotrigger para que 
  //aparezca la nueva bebida en la tabla, o los cambios de la bebida editada
  function manejarGuardado() {
    setBebidaSeleccionada(null);
    setRefrescoTrigger((prev) => prev + 1);
  }

  function manejarVentaRegistrada() {
    setRefrescoVentasTrigger((prev) => prev + 1);
    setRefrescoTrigger((prev) => prev + 1); // el stock tambien cambio
  }


  return (
    <Container className="py-4">
      <h1 className="mb-1">Fonda San Belarmino</h1>
      <p className="text-muted">Control de bebidas y ventas</p>

      {/* TODO: montar aqui los componentes de la interfaz. */}

      <BebidaForm
        key={bebidaSeleccionada?.id ?? "nuevo"}
        bebidaEditar={bebidaSeleccionada}
        onGuardado={manejarGuardado}
      />

      <BebidaList
        onEditar={manejarEditar}
        refrescoTrigger={refrescoTrigger}
      />

      <VentaForm onRegistrada={manejarVentaRegistrada} />

      <VentaHistorial refrescoTrigger={refrescoVentasTrigger} />


    </Container>
  );
}
