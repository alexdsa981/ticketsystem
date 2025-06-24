package com.ipor.ticketsystem.dashboard.admin;

import com.ipor.ticketsystem.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<Ticket, Long>, DashboardClasificadoresRepository {


}
