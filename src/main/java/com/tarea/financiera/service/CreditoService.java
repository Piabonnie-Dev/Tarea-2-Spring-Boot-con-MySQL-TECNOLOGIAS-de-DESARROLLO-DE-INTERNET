
package com.tarea.financiera.service;

// Importamos las clases que representan nuestras tablas.
import com.tarea.financiera.model.Credito;
import com.tarea.financiera.model.MovimientoTarjeta;

// Importamos los repositorios para trabajar con MySQL.
import com.tarea.financiera.repository.CreditoRepository;
import com.tarea.financiera.repository.MovimientoTarjetaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// Importamos las herramientas de Spring.
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Indicamos que esta clase contiene la logica del programa.
@Service
public class CreditoService {

    // Repositorio para guardar y consultar creditos.
    private final CreditoRepository creditoRepository;

    // Repositorio para guardar y consultar movimientos.
    private final MovimientoTarjetaRepository movimientoRepository;

    // Constructor que recibe los repositorios automaticamente.
    public CreditoService(
            CreditoRepository creditoRepository,
            MovimientoTarjetaRepository movimientoRepository) {

        this.creditoRepository = creditoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    // Metodo original de la Tarea 2 para guardar creditos.
    public Credito guardarCredito(Credito credito) {

        return creditoRepository.save(credito);
    }

    // Metodo original para consultar todos los creditos.
    public List<Credito> obtenerTodos() {

        return creditoRepository.findAll();
    }

    // Obtiene los movimientos disponibles para el formulario.
    // Son los movimientos que no pertenecen a un credito.
    @Transactional(readOnly = true)
    public List<MovimientoTarjeta> obtenerMovimientosDisponibles() {

        return movimientoRepository
                .findByCreditoIsNullOrderByIdAsc();
    }

    // Guarda un credito junto con los pagos capturados.
    // La transaccion permite guardar todo como una operacion.
    @Transactional
    public Credito guardarCreditoConPagos(
            Credito credito,
            Map<Long, BigDecimal> pagos) {

        // Obtenemos los movimientos originales desde MySQL.
        List<MovimientoTarjeta> movimientos =
                obtenerMovimientosDisponibles();

        // Verificamos que existan movimientos para registrar.
        if (movimientos.isEmpty()) {
            throw new IllegalStateException(
                    "No existen movimientos disponibles.");
        }

        // Guardamos primero el credito en la base de datos.
        Credito creditoGuardado =
                creditoRepository.save(credito);

        // Recorremos cada movimiento de la tabla.
        for (MovimientoTarjeta original : movimientos) {

            // Buscamos el pago ingresado para este movimiento.
            // Si no se ingreso un pago, utilizamos cero.
            BigDecimal pago = pagos.getOrDefault(
                    original.getId(), BigDecimal.ZERO);

            // Evitamos guardar pagos negativos o nulos.
            if (pago == null || pago.signum() < 0) {
                throw new IllegalArgumentException(
                        "El pago no puede ser negativo.");
            }

            // Creamos una copia del movimiento original.
            MovimientoTarjeta nuevo = new MovimientoTarjeta(
                    original.getFecha(),
                    original.getMes(),
                    original.getTarjeta(),
                    original.getTransaccion(),
                    original.getGastos()
            );

            // Asociamos el movimiento con el credito guardado.
            nuevo.setCredito(creditoGuardado);

            // Asignamos el pago que capturo el usuario.
            nuevo.setPagos(pago);

            // Guardamos el movimiento y su pago en MySQL.
            movimientoRepository.save(nuevo);
        }

        // Devolvemos el credito que acabamos de guardar.
        return creditoGuardado;
    }

    // Consulta los movimientos asociados a un credito.
    // Este metodo se utilizara en la pantalla de resultados.
    @Transactional(readOnly = true)
    public List<MovimientoTarjeta> obtenerMovimientosDeCredito(
            Long creditoId) {

        return movimientoRepository
                .findByCredito_IdOrderByIdAsc(creditoId);
    }
}
