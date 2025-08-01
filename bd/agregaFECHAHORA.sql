SELECT  * FROM ticket;
SELECT * FROM recepcion;
SELECT  * FROM atencion;
SELECT * FROM desestimacion;

SELECT * FROM detalle_en_espera;








UPDATE ticket
SET fecha_hora = CAST(fecha AS DATETIME) + CAST(hora AS DATETIME);

UPDATE recepcion
SET fecha_hora = CAST(fecha AS DATETIME) + CAST(hora AS DATETIME);

UPDATE atencion
SET fecha_hora = CAST(fecha AS DATETIME) + CAST(hora AS DATETIME);

UPDATE desestimacion
SET fecha_hora = CAST(fecha AS DATETIME) + CAST(hora AS DATETIME);





UPDATE detalle_en_espera
SET fecha_hora_inicio = 
        CASE 
            WHEN fecha_inicio IS NOT NULL AND hora_inicio IS NOT NULL 
            THEN CAST(fecha_inicio AS DATETIME) + CAST(hora_inicio AS DATETIME) 
            ELSE NULL 
        END,
    fecha_hora_fin = 
        CASE 
            WHEN fecha_fin IS NOT NULL AND hora_fin IS NOT NULL 
            THEN CAST(fecha_fin AS DATETIME) + CAST(hora_fin AS DATETIME) 
            ELSE NULL 
        END;
