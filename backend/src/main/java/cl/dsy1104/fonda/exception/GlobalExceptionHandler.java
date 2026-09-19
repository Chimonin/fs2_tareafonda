//traducen los errores en respuestas http
package cl.dsy1104.fonda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

//rest controller le dice a spring que lo que devuelvan estos metodos
//se convierta automaticamente a json
@RestControllerAdvice
public class GlobalExceptionHandler {

    //errores de @Valid (Bean Validation) -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //response entity representa una respuesta http completa, con body, contenido y cabeceras

    //map string object es el tipo del body
    //un diccionario con strings como llaves y objects como valores

    //MethodArgumentNotValidException ex es la excepcion parametro que recibe el metodo
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        //crea un mapa vacio donde se va guardando nombre del campo y mensaje de error
        Map<String, String> campos = new HashMap<>();

        //getbindingresults trae todos los detalles de que fallo en la validacion

        //fetfielderrors extrae la lista de errores que estan asociados a un campo particular de bean validation

        //for each error ejecuta el codigo de adentro
        //ambos van agregando entradas a campos
        ex.getBindingResult().getFieldErrors().forEach(error ->
                //agrega una entrada a un map
                //getfield da el nombre del campo que fallo
                //getdefaultmessage obtiene el mensaje de descripcion del error
                campos.put(error.getField(), error.getDefaultMessage())
        );
        //error global

        //aqui caen las validaciones como grados de alcohol y azucar por litro especiales
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                campos.put(error.getObjectName(), error.getDefaultMessage())
        );

        //otro mapa
        //body que representa el json final completo con dos llaves
        //error Validacion, y campos con el contenido del error de validacion
        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDACION");
        body.put("campos", campos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    //venta rechazada por reglas de negocio -> 409
    @ExceptionHandler(VentaRechazadaException.class)
    public ResponseEntity<Map<String, Object>> manejarVentaRechazada(VentaRechazadaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMotivo());
        body.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //recurso no encontrado -> 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(NoSuchElementException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "NO_ENCONTRADO");
        body.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}