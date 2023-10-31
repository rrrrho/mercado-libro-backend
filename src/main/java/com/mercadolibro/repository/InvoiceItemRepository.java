package com.mercadolibro.repository;

import com.mercadolibro.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoiceId(Long invoiceId);

    @Query(value = "SELECT id FROM invoice_item ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long findLastInsertedId();

}