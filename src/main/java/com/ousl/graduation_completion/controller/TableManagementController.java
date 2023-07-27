package com.ousl.graduation_completion.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.models.Application;
import com.ousl.graduation_completion.models.StudentStatus;
import com.ousl.graduation_completion.repository.ApplicationRepository;
import com.ousl.graduation_completion.repository.GradeRepository;
import com.ousl.graduation_completion.repository.StudentStatusRepository;
import com.ousl.graduation_completion.repository.TableRepository;
import com.ousl.graduation_completion.service.TableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/table-management")

public class TableManagementController {
    @Autowired
    TableRepository tableRepository;
    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    StudentStatusRepository studentStatusRepository;

    @Autowired
    GradeRepository gradeRepository;

    private final TableManagementService tableManagementService;
    private final ObjectMapper objectMapper;


    @GetMapping("/downloadtables/{table}")
    public ResponseEntity<String> getAllDataFromTable(@PathVariable String table) {
        List<?> data = tableManagementService.getAllDataFromTable(table);

        if (data == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid table name: " + table);
        }

        try {
            String jsonData = objectMapper.writeValueAsString(data);
            return ResponseEntity.ok(jsonData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON data.");
        }
    }



    public TableManagementController(TableManagementService tableManagementService, ObjectMapper objectMapper) {
        this.tableManagementService = tableManagementService;
        this.objectMapper = objectMapper;
    }


    @GetMapping("/GetAllTables")
    public List<ALLTables> getAllTables() {
        System.out.println("table list   " + tableManagementService.getAllTables());
        return tableManagementService.getAllTables();
    }


    @PostMapping("Updatetables/{tableName}")
    public ResponseEntity<String> updateCorrespondingTables(@PathVariable String tableName, @RequestBody List<Map<String, Object>> dataArray) {
        try {
            System.out.println(tableName);
            if (dataArray != null && dataArray.size() > 0) {
                dataArray.forEach((data) -> {
                    System.out.println(data);
                });
            }
            tableManagementService.updateCorrespondingTables(tableName, dataArray);
            return ResponseEntity.ok().body("Tables updated successfully");
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Table not found");
        }
    }
}



