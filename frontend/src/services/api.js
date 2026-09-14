// Unico punto del frontend que conoce la direccion del backend.
// Los componentes importan estas funciones y no usan fetch directamente.

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
    throw new Error(`HTTP ${res.status}`);
  }

  return res.status === 204 ? null : res.json();
}

export function listarBebidas(nombre) {
  // TODO: GET /bebidas, agregando ?nombre= cuando venga el filtro.
  // El filtrado lo hace el servidor, no este archivo.
}

export function crearBebida(datos) {
  // TODO: POST /bebidas
}

export function actualizarBebida(id, datos) {
  // TODO: PUT /bebidas/{id}
}

export function eliminarBebida(id) {
  // TODO: DELETE /bebidas/{id}
}

export function restringirVenta(id) {
  // TODO: PATCH /bebidas/{id}/restriccion
}

export function registrarVenta(bebidaId, unidades) {
  // TODO: POST /ventas
}

export function listarVentas() {
  // TODO: GET /ventas
}
