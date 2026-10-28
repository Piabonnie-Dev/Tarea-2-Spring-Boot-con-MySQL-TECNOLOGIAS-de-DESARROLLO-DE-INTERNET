
package com.tarea.financiera.controller;

// Importamos las clases del modelo.
import com.tarea.financiera.model.Credito;
import com.tarea.financiera.model.MovimientoTarjeta;

// Importamos el servicio que trabaja con MySQL.
import com.tarea.financiera.service.CreditoService;

// Importamos las herramientas de Java.
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importamos las anotaciones y clases de Spring.
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Indicamos que esta clase controla las peticiones web.
@Controller
public class CreditoController {

    // Declaramos el servicio que utilizaremos.
    private final CreditoService creditoService;

    // Spring proporciona automaticamente el servicio.
    public CreditoController(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    // Muestra el formulario principal.
    // Se ejecuta cuando entramos a localhost:8080.
    @GetMapping("/")
    public String mostrarFormulario(Model model) {

        // Creamos un objeto vacio para capturar el credito.
        model.addAttribute("credito", new Credito());

        // Consultamos los movimientos disponibles en MySQL.
        List<MovimientoTarjeta> movimientos =
                creditoService.obtenerMovimientosDisponibles();

        // Enviamos los movimientos a la tabla HTML.
        model.addAttribute("movimientos", movimientos);

        // Mostramos la vista formulario.html.
        return "formulario";
    }

    // Recibe los datos cuando presionamos Aceptar.
    @PostMapping("/guardar")
    public String guardarCredito(
            @ModelAttribute Credito credito,
            @RequestParam Map<String, String> parametros,
            Model model) {

        // Creamos un mapa para almacenar los pagos.
        // La clave es el ID y el valor es la cantidad pagada.
        Map<Long, BigDecimal> pagos = new HashMap<>();

        try {

            // Recorremos los campos recibidos del formulario.
            for (Map.Entry<String, String> entrada
                    : parametros.entrySet()) {

                // Obtenemos el nombre y valor del campo.
                String nombre = entrada.getKey();
                String valor = entrada.getValue();

                // Identificamos los campos llamados pagos[ID].
                if (nombre.matches("pagos\\[\\d+\\]")) {

                    // Extraemos el identificador del movimiento.
                    String textoId =
                            nombre.substring(6, nombre.length() - 1);

                    Long id = Long.parseLong(textoId);

                    // Si el campo esta vacio, usamos cero.
                    BigDecimal cantidad =
                            (valor == null || valor.isBlank())
                            ? BigDecimal.ZERO
                            : new BigDecimal(valor.trim());

                    // Guardamos el ID y el pago correspondiente.
                    pagos.put(id, cantidad);
                }
            }

            // Guardamos el credito junto con sus pagos.
            Credito creditoGuardado =
                    creditoService.guardarCreditoConPagos(
                            credito, pagos);

            // Enviamos el credito guardado a la vista.
            model.addAttribute("credito", creditoGuardado);

            // Consultamos los pagos guardados para ese credito.
            List<MovimientoTarjeta> movimientos =
                    creditoService.obtenerMovimientosDeCredito(
                            creditoGuardado.getId());

            // Enviamos los movimientos a resultado.html.
            model.addAttribute("movimientos", movimientos);

            // Mostramos la pantalla de resultados.
            return "resultado";

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            // Si ocurre un error, mostramos el mensaje.
            model.addAttribute("error", e.getMessage());

            // Recuperamos los movimientos de la base de datos.
            model.addAttribute("movimientos",
                    creditoService.obtenerMovimientosDisponibles());

            // Regresamos al formulario sin guardar pagos invalidos.
            return "formulario";
        }
    }
}
