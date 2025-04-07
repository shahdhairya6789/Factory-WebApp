package com.example.demo.repository.mapping;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.dto.AdvanceSalaryDTO;
import com.example.demo.models.dto.AttendanceDetailsDTO;
import com.example.demo.models.dto.UserAdvanceSalaryDTO;
import com.example.demo.models.entity.mapping.AdvanceSalary;

@Repository
public interface AdvanceSalaryRepository extends JpaRepository<AdvanceSalary, Integer> {

    @Query(value = "select " +
            "    tuasm.user_id as userId, " +
            "    tuasm.advance_salary as advanceSalaryAmount, " +
            "    tuasm.paid_by_user_id as paidByUserId, " +
            "    UNIX_TIMESTAMP(tuasm.advance_salary_date) as advanceSalaryDate, " +
            "    paid_tu.name as userName, " +
            "    tu.name as paidByUserName " +
            " from " +
            "    tblt_user_advance_salary_mapping tuasm " +
            " join tblm_user tu ON " +
            "    tu.id = tuasm.user_id " +
            " join tblm_user paid_tu on " +
            "    paid_tu.id = tuasm.paid_by_user_id " +
            " where " +
            "    tuasm.user_id = :userId " +
            "    and tuasm.advance_salary_date between :startDate and :endDate",
            nativeQuery = true)
    List<AdvanceSalaryDTO> fetchUserAdvanceSalary(
            @Param("userId") int userId,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);

    @Query(
            value = "select " +
                    "   tuasm.user_id as userId, " +
                    "   sum(tuasm.advance_salary) as advanceSalaryAmount " +
                    "from " +
                    "   tblt_user_advance_salary_mapping tuasm " +
                    "where " +
                    "   tuasm.user_id in (:userIds) " +
                    "   and tuasm.advance_salary_date between :startDate and :endDate " +
                    "group by " +
                    "   user_id ",
            nativeQuery = true
    )
    List<UserAdvanceSalaryDTO> fetchAdvanceSalaryForUsersBetweenStartAndEndDate(
            @Param("userIds") Set<Integer> userIds,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );
}
