package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.dto.PaymentReqDTO;
import com.mercadolibro.dto.PaymentRespDTO;
import com.mercadolibro.entity.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface InvoiceRequestService {

    InvoiceRequestDTO findById(UUID id);
    List<InvoiceRequestDTO> findAll();
    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);
    PageDTO<InvoiceRequestDTO> findAll(int page, int size);
}
