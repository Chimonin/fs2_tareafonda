// Unico punto del frontend que conoce la direccion del backend.
// Los componentes importan estas funciones y no usan fetch directamente.

//define donde esta el backend
const API = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api";

/** Lanza un error con el cuerpo de la respuesta cuando el status no es 2xx. */
async function pedir(ruta, opciones = {}) {
  const res = await fetch(`${API}${ruta}`, {
    headers: { "Content-Type": "application/json" },
    ...opciones,
  });

  if (!res.ok) {
    // TODO: leer el cuerpo del error (400 trae los campos, 409 trae el motivo)
    // y lanzarlo para que el componente pueda mostrarlo.
    //400 trae { error: "VALIDACION", campos: {...} }
    //409 trae { error: "LIMITE_EXCEDIDO", mensaje: "..." }
    //404 trae { error: "NO_ENCONTRADO", mensaje: "..." }
    const cuerpo = await res.json().catch(() => null);
    const error = new Error(cuerpo?.mensaje ?? `HTTP ${res.status}`);
    error.status = res.status;
    error.body = cuerpo;
    throw error;
  }

  //json() convierte de json a un array de objetos js
  return res.status === 204 ? null : res.json();
}

//estas funciones arman la peticion http

export function listarBebidas(nombre) {
  // TODO: GET /bebidas, agregando ?nombre= cuando venga el filtro.
  // El filtrado lo hace el servidor, no este archivo.
  const query = nombre ? `?nombre=${encodeURIComponent(nombre)}` : "";
  return pedir(`/bebidas${query}`);
}

//stringify convierte el objeto js en un string de texto json
export function crearBebida(datos) {
  // TODO: POST /bebidas
  return pedir("/bebidas", {
    method: "POST",
    body: JSON.stringify(datos),
  });
}

export function actualizarBebida(id, datos) {
  // TODO: PUT /bebidas/{id}
  return pedir(`/bebidas/${id}`, {
    method: "PUT",
    body: JSON.stringify(datos),
  });
}

export function eliminarBebida(id) {
  // TODO: DELETE /bebidas/{id}
  return pedir(`/bebidas/${id}`, {
    method: "DELETE",
  });
}

export function restringirVenta(id) {
  // TODO: PATCH /bebidas/{id}/restriccion
  return pedir(`/bebidas/${id}/restriccion`, {
    method: "PATCH",
  });
}

export function registrarVenta(bebidaId, unidades) {
  // TODO: POST /ventas
  return pedir("/ventas", {
    method: "POST",
    body: JSON.stringify({ bebidaId, unidades }),
  });
}

export function listarVentas() {
  // TODO: GET /ventas
  return pedir("/ventas");
}
