/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tarea.financiera.repository;


import com.tarea.financiera.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CreditoRepository extends JpaRepository <Credito, Long> {
    
}
