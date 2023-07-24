package com.ousl.graduation_completion.controller;


import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.models.Application;
import com.ousl.graduation_completion.models.StudentStatus;
import com.ousl.graduation_completion.repository.ApplicationRepository;
import com.ousl.graduation_completion.repository.StudentStatusRepository;
import com.ousl.graduation_completion.repository.TableRepository;
import com.ousl.graduation_completion.service.TableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
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
    private final TableManagementService tableManagementService;


    public TableManagementController(TableManagementService tableManagementService) {
        this.tableManagementService = tableManagementService;
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



