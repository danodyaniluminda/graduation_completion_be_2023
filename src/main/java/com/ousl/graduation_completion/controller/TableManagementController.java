package com.ousl.graduation_completion.controller;


import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.service.TableManagementService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/table-management")

public class TableManagementController {

    private final TableManagementService tableManagementService;


    public TableManagementController(TableManagementService tableManagementService) {
        this.tableManagementService = tableManagementService;
    }


    @GetMapping("/GetAllTables")
    public List<ALLTables> getAllTables() {
        System.out.println("table list   "+tableManagementService.getAllTables());
        return tableManagementService.getAllTables();
    }

    @PostMapping("/Updatetables")
    public <TableData> void receiveTableData(@RequestBody TableData tableData) {
        // Process the received table data as needed
        // You can define a separate class (e.g., TableData) to represent the data structure
    }

    @PostMapping("Updatetables/{tableName}/{id}")
    public ResponseEntity<String> updateCorrespondingTables(@PathVariable String tableName, @PathVariable Long id, @RequestBody Map<String, Object> data) throws ChangeSetPersister.NotFoundException {
        tableManagementService.updateCorrespondingTables(tableName, id, data);
        return ResponseEntity.ok("Tables updated successfully");
    }
}



