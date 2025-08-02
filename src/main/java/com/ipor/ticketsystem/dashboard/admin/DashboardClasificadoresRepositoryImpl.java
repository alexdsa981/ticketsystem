package com.ipor.ticketsystem.dashboard.admin;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class DashboardClasificadoresRepositoryImpl implements DashboardClasificadoresRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RecordFactorXConteo> obtenerConteoDinamico(
            String agruparPor,
            LocalDate fechaInicio, LocalDate fechaFin,
            Long idSede, Long idArea, Long idCategoria, Long idSubcategoria,
            Long idTipoIncidencia, Long idTipoUrgencia, Long idUsuario) {


        Set<String> CAMPOS_VALIDOS = Set.of(
                "areaAtencion",
                "categoriaIncidencia",
                "subCategoriaIncidencia",
                "tipoIncidencia",
                "clasificacionUrgencia",
                "sede"
        );

        if (!CAMPOS_VALIDOS.contains(agruparPor)) {
            throw new IllegalArgumentException("Campo de agrupación no permitido: " + agruparPor);
        }

        // Mapear a la ruta real del campo en la entidad
        String campoAgrupacion;
        switch (agruparPor) {
            case "sede":
                campoAgrupacion = "a.areaAtencion.sede.nombre";
                break;
            case "categoriaIncidencia":
                campoAgrupacion = "a.tipoIncidencia.subCategoriaIncidencia.categoriaIncidencia.nombre";
                break;
            case "subCategoriaIncidencia":
                campoAgrupacion = "a.tipoIncidencia.subCategoriaIncidencia.nombre";
                break;
            default:
                campoAgrupacion = "a." + agruparPor + ".nombre";
                break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT new com.ipor.ticketsystem.dashboard.RecordFactorXConteo(")
                .append(campoAgrupacion).append(", COUNT(t.id)) ")
                .append("FROM Ticket t ")
                .append("JOIN t.atencion a ")
                .append("WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        // Filtros por fecha
        if (fechaInicio != null && fechaFin != null) {
            sb.append("AND t.fecha BETWEEN :fechaInicio AND :fechaFin ");
            params.put("fechaInicio", fechaInicio);
            params.put("fechaFin", fechaFin);
        }

        // Filtros opcionales
        if (idSede != null) {
            sb.append("AND a.areaAtencion.sede.id = :idSede ");
            params.put("idSede", idSede);
        }
        if (idCategoria != null) {
            sb.append("AND a.tipoIncidencia.subCategoriaIncidencia.categoriaIncidencia.id = :idCategoria ");
            params.put("idCategoria", idCategoria);
        }
        if (idSubcategoria != null) {
            sb.append("AND a.tipoIncidencia.subCategoriaIncidencia.id = :idSubcategoria ");
            params.put("idSubcategoria", idSubcategoria);
        }
        if (idTipoIncidencia != null) {
            sb.append("AND a.tipoIncidencia.id = :idTipoIncidencia ");
            params.put("idTipoIncidencia", idTipoIncidencia);
        }
        if (idTipoUrgencia != null) {
            sb.append("AND a.clasificacionUrgencia.id = :idTipoUrgencia ");
            params.put("idTipoUrgencia", idTipoUrgencia);
        }
        if (idArea != null) {
            sb.append("AND a.areaAtencion.id = :idArea ");
            params.put("idArea", idArea);
        }
        if (idUsuario != null) {
            sb.append("AND a.usuario.id = :idUsuario ");
            params.put("idUsuario", idUsuario);
        }

        // Agrupamiento y orden
        sb.append("GROUP BY ").append(campoAgrupacion).append(" ");
        sb.append("ORDER BY COUNT(t.id) DESC");

        TypedQuery<RecordFactorXConteo> query = entityManager
                .createQuery(sb.toString(), RecordFactorXConteo.class);

        params.forEach(query::setParameter);


        return query.getResultList();
    }



    @Override
    public List<Long> obtenerIdsTicketsFiltrados(
            LocalDate fechaInicio, LocalDate fechaFin,
            Long idSede, Long idArea, Long idCategoria, Long idSubcategoria,
            Long idTipoIncidencia, Long idTipoUrgencia, Long idUsuario) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.id FROM Ticket t JOIN t.atencion a WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (fechaInicio != null && fechaFin != null) {
            sb.append("AND t.fecha BETWEEN :fechaInicio AND :fechaFin ");
            params.put("fechaInicio", fechaInicio);
            params.put("fechaFin", fechaFin);
        }
        if (idSede != null) {
            sb.append("AND a.areaAtencion.sede.id = :idSede ");
            params.put("idSede", idSede);
        }
        if (idCategoria != null) {
            sb.append("AND a.tipoIncidencia.subCategoriaIncidencia.categoriaIncidencia.id = :idCategoria ");
            params.put("idCategoria", idCategoria);
        }
        if (idSubcategoria != null) {
            sb.append("AND a.tipoIncidencia.subCategoriaIncidencia.id = :idSubcategoria ");
            params.put("idSubcategoria", idSubcategoria);
        }
        if (idTipoIncidencia != null) {
            sb.append("AND a.tipoIncidencia.id = :idTipoIncidencia ");
            params.put("idTipoIncidencia", idTipoIncidencia);
        }
        if (idTipoUrgencia != null) {
            sb.append("AND a.clasificacionUrgencia.id = :idTipoUrgencia ");
            params.put("idTipoUrgencia", idTipoUrgencia);
        }
        if (idArea != null) {
            sb.append("AND a.areaAtencion.id = :idArea ");
            params.put("idArea", idArea);
        }
        if (idUsuario != null) {
            sb.append("AND a.usuario.id = :idUsuario ");
            params.put("idUsuario", idUsuario);
        }

        sb.append("ORDER BY a.fecha DESC");

        TypedQuery<Long> query = entityManager.createQuery(sb.toString(), Long.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }




    public IndicadorResolucionDTO obtenerIndicadoresResolucion(
            LocalDate fechaInicio, LocalDate fechaFin,
            Long idSede, Long idArea, Long idCategoria, Long idSubcategoria,
            Long idTipoIncidencia, Long idTipoUrgencia, Long idUsuario
    ) {
        StringBuilder sbTotal = new StringBuilder("SELECT COUNT(t.id) FROM Ticket t JOIN t.faseTicket f WHERE 1=1 ");
        StringBuilder sbResueltos = new StringBuilder("SELECT COUNT(t.id) FROM Ticket t JOIN t.atencion a WHERE 1=1 ");
        StringBuilder sbErrorUsuario = new StringBuilder("SELECT COUNT(t.id) FROM Ticket t JOIN t.atencion a WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        // Filtros de fechas y fase
        if (fechaInicio != null && fechaFin != null) {
            sbTotal.append("AND t.fecha BETWEEN :fechaInicio AND :fechaFin ");
            sbTotal.append("AND f.id != 4 "); // fase 4 = eliminado

            sbResueltos.append("AND t.fecha BETWEEN :fechaInicio AND :fechaFin ");
            sbErrorUsuario.append("AND t.fecha BETWEEN :fechaInicio AND :fechaFin ");
            params.put("fechaInicio", fechaInicio);
            params.put("fechaFin", fechaFin);
        }

        // Filtros comunes a atencion
        if (idSede != null) {
            sbResueltos.append("AND a.areaAtencion.sede.id = :idSede ");
            sbErrorUsuario.append("AND a.areaAtencion.sede.id = :idSede ");
            params.put("idSede", idSede);
        }
        if (idCategoria != null) {
            sbResueltos.append("AND a.tipoIncidencia.subCategoriaIncidencia.categoriaIncidencia.id = :idCategoria ");
            sbErrorUsuario.append("AND a.tipoIncidencia.subCategoriaIncidencia.categoriaIncidencia.id = :idCategoria ");
            params.put("idCategoria", idCategoria);
        }
        if (idSubcategoria != null) {
            sbResueltos.append("AND a.tipoIncidencia.subCategoriaIncidencia.id = :idSubcategoria ");
            sbErrorUsuario.append("AND a.tipoIncidencia.subCategoriaIncidencia.id = :idSubcategoria ");
            params.put("idSubcategoria", idSubcategoria);
        }
        if (idTipoIncidencia != null) {
            sbResueltos.append("AND a.tipoIncidencia.id = :idTipoIncidencia ");
            sbErrorUsuario.append("AND a.tipoIncidencia.id = :idTipoIncidencia ");
            params.put("idTipoIncidencia", idTipoIncidencia);
        }
        if (idTipoUrgencia != null) {
            sbResueltos.append("AND a.clasificacionUrgencia.id = :idTipoUrgencia ");
            sbErrorUsuario.append("AND a.clasificacionUrgencia.id = :idTipoUrgencia ");
            params.put("idTipoUrgencia", idTipoUrgencia);
        }
        if (idArea != null) {
            sbResueltos.append("AND a.areaAtencion.id = :idArea ");
            sbErrorUsuario.append("AND a.areaAtencion.id = :idArea ");
            params.put("idArea", idArea);
        }
        if (idUsuario != null) {
            sbResueltos.append("AND a.usuario.id = :idUsuario ");
            sbErrorUsuario.append("AND a.usuario.id = :idUsuario ");
            params.put("idUsuario", idUsuario);
        }

        // Filtro específico para error de usuario
        sbErrorUsuario.append("AND a.clasificacionAtencion.id = 10004 ");

        // Ejecutar consultas
        TypedQuery<Long> queryTotal = entityManager.createQuery(sbTotal.toString(), Long.class);
        queryTotal.setParameter("fechaInicio", fechaInicio);
        queryTotal.setParameter("fechaFin", fechaFin);

        TypedQuery<Long> queryResueltos = entityManager.createQuery(sbResueltos.toString(), Long.class);
        TypedQuery<Long> queryErrorUsuario = entityManager.createQuery(sbErrorUsuario.toString(), Long.class);
        params.forEach((k, v) -> {
            queryResueltos.setParameter(k, v);
            queryErrorUsuario.setParameter(k, v);
        });

        Long total = queryTotal.getSingleResult();
        Long resueltos = queryResueltos.getSingleResult();
        Long errorUsuario = queryErrorUsuario.getSingleResult();

        return new IndicadorResolucionDTO(
                total != null ? total : 0,
                resueltos != null ? resueltos : 0,
                errorUsuario != null ? errorUsuario : 0
        );
    }




}
