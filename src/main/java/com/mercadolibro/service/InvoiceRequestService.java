package com.mercadolibro.service;

import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.Book;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface InvoiceRequestService {
    List<InvoiceSearchDTO> findAll();
    PageDTO<InvoiceSearchDTO> findAll(int page, int size);

    InvoiceSearchDTO findById(UUID id);
    PageDTO<InvoiceSearchDTO> findByUserId(Long userId, int page, int size);

    List<BookRespDTO> findBestSellersList();
    PageDTO<BookRespDTO> findBestSellersPage(int page, int size);
    PageDTO<MonthlySaleDTO> getMonthlySales(int page, int size);
    PageDTO<CategorySalesDTO> getSalesByCategory(int page, int size);

    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);

    PaymentRespDTO processPayment(UUID invoiceId, PaymentReqDTO paymentReqDTO);
	
	Double findTotalBooksPriceSell();
    Double calculateAverageTotalPriceByTotalInvoices();
    Integer calculateTotalQuantityOfBooksSell();
    Double calculateAverageQuantityOfBooksSellByTotalInvoices();

}
