/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.controllers.Mapper;
import com.mfr.taass.spring.transaction.api.controllers.dto.TransactionDto;
import com.mfr.taass.spring.transaction.api.entities.Transaction;
import java.util.List;
import org.mapstruct.Mapper;

/**
 *
 * @author luca
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDto map(Transaction transaction);
    List<TransactionDto> map (List<Transaction> transactions);
}

