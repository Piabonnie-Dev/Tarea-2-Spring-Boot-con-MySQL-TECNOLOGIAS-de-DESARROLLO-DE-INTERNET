
package com.tarea.financiera.repository;

import com.tarea.financiera.model.MovimientoTarjeta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoTarjetaRepository
        extends JpaRepository<MovimientoTarjeta, Long> {

    List<MovimientoTarjeta> findByCreditoIsNullOrderByIdAsc();

    List<MovimientoTarjeta> findByCredito_IdOrderByIdAsc(Long creditoId);
}
