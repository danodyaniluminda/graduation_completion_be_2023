package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.models.ALLTables;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Map;

public interface TableManagementService {

    List<ALLTables> getAllTables();

    void updateCorrespondingTables(String tableName, Long id, Map<String, Object> data) throws ChangeSetPersister.NotFoundException;
}
