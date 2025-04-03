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
                    " a.attendance_date AS attendanceDate, " +
                    " a.production as production, " +
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

    @Query(value = "select " +
            "    tusm.user_id as userId, " +
            "    tusm.salary_type_id as salaryTypeId, " +
            "    tusm.salary as monthlySalary, " +
            "    count(distinct DATE_FORMAT(attendance_date , \"%d-%m-%Y\")) as workingDays " +
            "from " +
            "    tblt_user_salary_mapping tusm " +
            "left join tblm_attendance ta on " +
            "    tusm.user_id = ta.user_id " +
            "    and tusm.salary_type_id = ta.salary_type_id " +
            "where " +
            "    tusm.user_id in (:userIds) " +
            "    and ta.attendance_date between :startDate and :endDate " +
            "group by  " +
            "    tusm.user_id, " +
            "    tusm.salary_type_id," +
            "    tusm.salary ",
            nativeQuery = true)
    List<UserSalaryAttendanceDTO> findUserSalaryAttendanceByUserId(
            @Param("userIds") Set<Integer> userIds,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );
}
