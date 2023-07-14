package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.payload.request.DTO_request_gpaCalculation;
import com.ousl.graduation_completion.payload.response.DTO_response_gpaCalculation;
import com.ousl.graduation_completion.service.GpaCalculationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GpaCalculationserviceImpl implements GpaCalculationService {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<DTO_response_gpaCalculation> gpaCalculations(DTO_request_gpaCalculation dtoRequestGpaCalculation) {
        try {
            String sql = "SELECT program_id, application_id,\n" +
                    "CAST(SUM(credit*gpv)/sum(credit)as decimal(7,2)) as GPA\n" +
                    "FROM student WHERE program_id=:programmeId\n" +
                    "AND valid=false\n" +
                    "GROUP BY program_id, application_id";

            Query query = em.createNativeQuery(sql);
            query.setParameter("programmeId", dtoRequestGpaCalculation.getProgrammeId());

            List<Object[]> results = query.getResultList();
            List<DTO_response_gpaCalculation> response = new ArrayList<>();
            for (Object[] result : results) {
                DTO_response_gpaCalculation dtoResponseGpaCalculation = new DTO_response_gpaCalculation();
                dtoResponseGpaCalculation.setProgramme_id(Integer.valueOf(result[0].toString()));
                dtoResponseGpaCalculation.setApplication_id(Integer.valueOf(result[1].toString()));
                dtoResponseGpaCalculation.setGpa(Double.valueOf(result[2].toString()));
                response.add(dtoResponseGpaCalculation);
            }
            return response;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }



}
