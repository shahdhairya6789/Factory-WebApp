package com.example.demo.repository.mapping;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.dto.PaymentListingDTO;
import com.example.demo.models.entity.mapping.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query(
            value = "select " +
                    "    tupm.user_id as userId, " +
                    "    tu.name as userName, " +
                    "    sum(tupm.payment_amount) as totalPayableAmount , " +
                    "    sum(tupm.advance_payment) as totalAdvancePaid, " +
                    "    sum(tupm.payment_amount) - sum(tupm.advance_payment) as netPayable, " +
                    "    Max(case when tupm.salary_type_id = 1 then coalesce(tupm.working_days, 0) else 0 end) as singleDayWorkingCount, " +
                    "    Max(case when tupm.salary_type_id = 2 then coalesce(tupm.working_days, 0) else 0 end) as doubleDayWorkingCount, " +
                    "    Max(case when tusm.salary_type_id = 1 then coalesce(tusm.salary , 0) else 0 end) as singleMachineSalary, " +
                    "    Max(case when tusm.salary_type_id = 2 then coalesce(tusm.salary , 0) else 0 end) as doubleMachineSalary " +
                    "from " +
                    "    tblt_user_payment_mapping tupm " +
                    "left join  " +
                    "    tblt_user_salary_mapping tusm on " +
                    "    tupm.user_id = tusm.user_id " +
                    "    and tupm.salary_type_id = tusm.salary_type_id " +
                    "left join " +
                    "   tblm_user tu on " +
                    "   tu.id = tupm.user_id" +
                    "where  " +
                    "    ( tupm.user_id = :userId or tu.manager_id = :managerId )" +
                    "    and tupm.payment_date between :startDate and :endDate " +
                    "    and tupm.is_active = true " +
                    "group by " +
                    "    tupm.user_id ;",
            nativeQuery = true
    )
    List<PaymentListingDTO> getPaymentListingByUserId(
            @Param("userId") long userId,
            @Param("managerId") long managerId,
            @Param("startDate")Timestamp startDate,
            @Param("endDate")Timestamp endDate
    );

    List<Payment> findByUserIdInAndPaymentDateBetween(Set<Integer> userIds, Timestamp paymentDateAfter, Timestamp paymentDateBefore);
}
