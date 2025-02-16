package com.example.demo.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.mapping.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
