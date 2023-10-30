package com.mercadolibro.repository;

import com.mercadolibro.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInfoRepository extends JpaRepository<Invoice, Long> {



}
