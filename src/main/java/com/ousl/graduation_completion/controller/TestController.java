package com.ousl.graduation_completion.controller;




import com.ousl.graduation_completion.dto.DataObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class TestController {
    @GetMapping("/getName")
    public String getname(){
        return "pramod";
    }

    @GetMapping("/filter")
    public List<DataObject> filterData(@RequestParam("ids") List<String> ids) {
        // Logic to filter the dataset based on the given array of IDs
        List<DataObject> filteredData = getDataFromDataSource(); // Replace with your data retrieval logic

        List<DataObject> result = filteredData.stream()
                .filter(data -> !ids.contains(data.getId()))
                .collect(Collectors.toList());

        return result;
    }

    private List<DataObject> getDataFromDataSource() {
        // Logic to retrieve the dataset from the data source (e.g., database, API, etc.)
        // Replace this method with your own implementation
        DataObject d1 = new DataObject();
        DataObject d2 = new DataObject();
        DataObject d3 = new DataObject();
        DataObject d4 = new DataObject();
        DataObject d5 = new DataObject();
        // Example code to generate a dummy dataset
        List<DataObject> data = new ArrayList<>();
        d1.setId("1");
        d2.setId("2");
        d3.setId("3");
        d4.setId("4");
        d5.setId("5");
        data.add(d1);
        data.add(d2);
        data.add(d3);
        data.add(d4);
        data.add(d5);

        return data;
    }
}
