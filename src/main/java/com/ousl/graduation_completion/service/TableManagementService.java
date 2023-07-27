package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.models.Grade;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TableManagementService {

    List<ALLTables> getAllTables();



    List<?> getAllDataFromTable(String table);

    ResponseEntity<String> updateCorrespondingTables(String tableName, List<Map<String, Object>> dataArray) throws ChangeSetPersister.NotFoundException;
}
