CREATE VIEW vw_contadorTicker_fase AS
SELECT ft.nombre AS FaseTicket, COUNT(*) AS ContadorTickets
FROM ticket t
INNER JOIN fase_ticket ft ON ft.id = t.id_fase_ticket
GROUP BY ft.nombre;
