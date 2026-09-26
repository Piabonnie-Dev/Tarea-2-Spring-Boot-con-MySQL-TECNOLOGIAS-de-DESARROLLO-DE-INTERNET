
package com.tarea.financiera.model;

import jakarta.persistence.*;
import java.time.LocalDate;
        
        @Entity
        @Table(name = "creditos")
        
        
public class Credito {
            
            @Id
            @GeneratedValue (strategy = GenerationType.IDENTITY)
            private Long id;
            
            private LocalDate fecha;
            private String moneda;
            
            
            private Double monto;
            private Integer periodo;
            private Double cuota;
            private Double tea;
            
            @Column(name = "fecha_vencimiento")
            private LocalDate fechaVencimiento;
            
            public Credito(){
        
            }
                  public Long getId() {return id;}
        public void setId (Long id){
        this.id = id;
        }          

        
        public LocalDate getFecha(){
            return fecha;
        }
        
        public void setFecha(LocalDate fecha){
        this.fecha = fecha;
        }

        public String getMoneda(){
        return moneda;}
        
        public void setMoneda (String moneda){
        this.moneda = moneda;
        }
        public Double getMonto (){
        return monto;
        }
        
        public void setMonto (Double monto){
        this.monto = monto;}
        
        public Integer getPeriodo(){
        return periodo;}
        
        public void setPeriodo(Integer periodo){
        this.periodo = periodo;}
        
        public Double getCuota(){
        return cuota;}
        
        public void setCuota (Double cuota){
        this.cuota=cuota;}
        
         public Double getTea() {
        return tea;
    }

    public void setTea(Double tea) {
        this.tea = tea;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
        
}