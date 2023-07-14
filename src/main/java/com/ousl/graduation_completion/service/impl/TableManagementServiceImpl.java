package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.models.Application;
import com.ousl.graduation_completion.models.StudentStatus;
import com.ousl.graduation_completion.repository.ApplicationRepository;
import com.ousl.graduation_completion.repository.StudentStatusRepository;
import com.ousl.graduation_completion.repository.TableRepository;
import com.ousl.graduation_completion.service.TableManagementService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TableManagementServiceImpl implements TableManagementService {


   private final TableRepository tableRepository;
   private final ApplicationRepository applicationRepository;
   private final StudentStatusRepository studentStatusRepository;

    public TableManagementServiceImpl(TableRepository tableRepository, ApplicationRepository applicationRepository, StudentStatusRepository studentStatusRepository) {
        this.tableRepository = tableRepository;
        this.applicationRepository = applicationRepository;
        this.studentStatusRepository = studentStatusRepository;
    }

    @Override
    public List<ALLTables> getAllTables() {
        return tableRepository.findAll();
    }

    public void updateCorrespondingTables(String tableName, Long id, Map<String, Object> data) throws ChangeSetPersister.NotFoundException {
        // Logic to update corresponding tables based on the given tableName and id
        if ("application".equals(tableName)) {
            Application application = applicationRepository.findById(id).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
            if (data.containsKey("name")) {
                application.setName((String) data.get("name"));
            }

            applicationRepository.save(application); // Save the updated entity
        } else if ("student_status".equals(tableName)) {
            StudentStatus student_status = studentStatusRepository.findById(id).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
            // Perform update on Table2
            // ...
            studentStatusRepository.save(student_status); // Save the updated entity
        }
    }



}
