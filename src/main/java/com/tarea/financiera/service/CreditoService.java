/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tarea.financiera.service;

import com.tarea.financiera.model.Credito;
import com.tarea.financiera.repository.CreditoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CreditoService {
    
    private final CreditoRepository creditoRepository;
    public CreditoService (CreditoRepository creditoRepository){
    this.creditoRepository = creditoRepository;
    }
    
    public Credito guardarCredito (Credito credito){
    return creditoRepository.save(credito);
    }
    
    public List<Credito> obtenerTodos(){
    return creditoRepository.findAll();
    }
    
}
