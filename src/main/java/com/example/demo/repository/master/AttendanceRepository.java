package com.example.demo.repository.master;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.dto.AttendanceDetailsDTO;
import com.example.demo.models.dto.UserSalaryAttendanceDTO;
import com.example.demo.models.entity.master.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByUser_IdAndAttendanceDateBetweenAndIsActive(int userId, Timestamp attendanceDateAfter, Timestamp attendanceDateBefore, boolean active);

    @Query(value =
            "SELECT " +
                    " a.id AS attendanceId, " +
                    " UNIX_TIMESTAMP(a.attendance_date) AS attendanceDate, " +                    " a.production as production, " +
                    " a.dhaga as dhaga, " +
                    " a.frames as frames, " +
                    " u.id AS userId, " +
                    " u.name AS userName, " +
                    " s.id AS shiftId, " +
                    " s.name AS shiftName, " +
                    " st.id AS salaryTypeId," +
                    " st.name AS salaryType, " +
                    " m.id AS machineId, " +
                    " m.name AS machineName " +
                    " FROM tblm_attendance a " +
                    " JOIN tblm_user u ON a.user_id = u.id " +
                    " JOIN tbls_shift s ON a.shift_id = s.id " +
                    " JOIN tbls_salary_type st ON a.salary_type_id = st.id " +
                    " JOIN tblm_machine m ON a.machine_id = m.id " +
                    " WHERE a.is_active = true " +
                    " AND a.attendance_date BETWEEN :startDate AND :endDate and a.user_id = :userId", // Example condition to filter active attendance
            nativeQuery = true)
    List<AttendanceDetailsDTO> fetchAttendanceDetails(
            @Param("userId") int userId,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);

    @Query(value = """
            WITH ranked_attendance AS (
                SELECT
                    user_id,
                    DATE(attendance_date) AS att_date,
                    salary_type_id,
                    ROW_NUMBER() OVER (
                        PARTITION BY user_id, DATE(attendance_date)
                        ORDER BY salary_type_id DESC
                    ) AS rn
                FROM
                    tblm_attendance
                WHERE
                    attendance_date BETWEEN :startDate AND :endDate
                    AND user_id in (:userIds)
            ),
            filtered_attendance AS (
                SELECT
                    user_id,
                    att_date,
                    salary_type_id
                FROM
                    ranked_attendance
                WHERE
                    rn = 1
            )
            SELECT
                fa.user_id AS userId,
                fa.salary_type_id AS salaryTypeId,
                tusm.salary AS monthlySalary,
                COUNT(*) AS working_days
            FROM
                filtered_attendance fa
            JOIN
                tblt_user_salary_mapping tusm
                ON fa.user_id = tusm.user_id
                AND fa.salary_type_id = tusm.salary_type_id
            GROUP BY
                fa.user_id,
                fa.salary_type_id,
                tusm.salary
            """,
            nativeQuery = true)
    List<UserSalaryAttendanceDTO> findUserSalaryAttendanceByUserId(
            @Param("userIds") Set<Integer> userIds,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );
}
