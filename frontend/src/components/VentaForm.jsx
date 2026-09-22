import { useState, useEffect } from "react";
import { Form, Button, Alert } from "react-bootstrap";
import { listarBebidas, registrarVenta } from "../services/api";

export default function VentaForm({ onRegistrada }) {
    const [bebidas, setBebidas] = useState([]);
    const [bebidaId, setBebidaId] = useState("");
    const [unidades, setUnidades] = useState(1);
    const [resultado, setResultado] = useState(null);
    const [error, setError] = useState(null);
    const [enviando, setEnviando] = useState(false);

    useEffect(() => {
        //si la carga de la lista de bebidas falla el dropdown se mostrara vacio
        //el catch no hace nada a proposito
        listarBebidas().then(setBebidas).catch(() => { });
    }, []);

    async function manejarSubmit(e) {
        e.preventDefault();
        setEnviando(true);
        setError(null);
        setResultado(null);

        try {
            const venta = await registrarVenta(Number(bebidaId), Number(unidades));
            setResultado(venta);
            setBebidaId("");
            setUnidades(1);
            //llama a para avisar que terminó con exito
            onRegistrada?.();
        } catch (err) {
            setError(err.body?.mensaje ?? err.message);
            if (err.status === 409) {
                onRegistrada?.();
            }
        } finally {
            setEnviando(false);
        }
    }

    return (
        <Form onSubmit={manejarSubmit} className="mb-4">
            <h2 className="h4">Registrar venta</h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {resultado && (
                <Alert variant="success">
                    Venta autorizada: {resultado.unidades} x {resultado.nombre} = ${resultado.total}
                </Alert>
            )}

            <Form.Group className="mb-2">
                <Form.Label>Bebida</Form.Label>
                <Form.Select
                    value={bebidaId}
                    onChange={(e) => setBebidaId(e.target.value)}
                    required
                >
                    <option value="">Seleccione una bebida...</option>
                    {bebidas.map((b) => (
                        <option key={b.id} value={b.id}>
                            {b.nombre} (stock: {b.stock})
                        </option>
                    ))}
                </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Unidades</Form.Label>
                <Form.Control
                    type="number"
                    min="1"
                    //contenido actual del campo de texto
                    value={unidades}
                    onChange={(e) => setUnidades(e.target.value)}
                    required
                />
            </Form.Group>

            {/**/}
            {/*este or es true si cualquiera de las dos opciones es true*/}
            {/*es false si enviando es false y bebidaid tiene un id*/}
            {/*es true si enviando es true y si hay bebidaid*/}
            {/*es true si enviando es falso pero no hay bebida elegida*/}
            {/*es true si enviando es true y no tiene bebida elegida*/}
            <Button type="submit" disabled={enviando || !bebidaId}>
                {enviando ? "Registrando..." : "Registrar venta"}
            </Button>
        </Form>
    );
}