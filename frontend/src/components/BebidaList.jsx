import { useState, useEffect } from "react";
import { Table, Form, Alert, Spinner, Button } from "react-bootstrap";
import { listarBebidas } from "../services/api";

export default function BebidaList({ onEditar, refrescoTrigger }) {
    const [bebidas, setBebidas] = useState([]);
    const [nombre, setNombre] = useState("");
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setCargando(true);
        //llamada a la api
        listarBebidas(nombre)
            //then si todo salio bien
            .then((datos) => {
                setBebidas(datos);
                setError(null);
            })
            //catch para atrapar el error
            .catch((err) => setError(err.message))
            //no importa el resultado, se ejecuta
            .finally(() => setCargando(false));
        //nombre en corchetes le dice a react que vuelva a ejecutar el bloque
        //cada vez que nombre cambie, asi, cada vez que el usuario escribe algo en el filtro
        //de dispara una nueva peticion al backend
    }, [nombre, refrescoTrigger]);

    return (
        <div className="mb-4">
            <h2 className="h4">Catalogo</h2>

            <Form.Control
                type="text"
                placeholder="Filtrar por nombre..."
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
                className="mb-3"
            />

            {/**/}
            {/*Si error tiene algun valor, muestra la alerta*/}
            {/*Si error es null, no muestra nada*/}
            {error && <Alert variant="danger">{error}</Alert>}

            {cargando ? (
                <Spinner animation="border" size="sm" />
            ) : (
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Tipo</th>
                            <th>Stock</th>
                            <th>Precio</th>
                            <th>Restringida</th>
                        </tr>
                    </thead>
                    <tbody>
                        {bebidas.map((b) => (
                            <tr key={b.id}>
                                <td>{b.nombre}</td>
                                <td>{b.tipo}</td>
                                <td>{b.stock}</td>
                                <td>${b.precio}</td>
                                <td>{b.ventaRestringida ? "Si" : "No"}</td>
                                <td>
                                    <Button size="sm" variant="outline-primary" onClick={() => onEditar(b)}>
                                        Editar
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
}