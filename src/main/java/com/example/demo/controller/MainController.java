package com.example.demo.controller;

import com.example.demo.DTO.CatDTO;
import com.example.demo.entity.Cat;
import com.example.demo.repository.CatRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "main_methods")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    @Autowired
    private final CatRepo catRepo;
    private final ObjectMapper objectMapper;

    @Operation (
            summary = "кладёт нового котика в базу",
            description = "получает DTO кота и билдером собирает и кладет котика в базу"
    )
    @PostMapping("/api/add")
    public void addCat(@RequestBody CatDTO catDTO) {

        log.info(
                "New row" + catRepo.save(
                        Cat.builder()
                            .name(catDTO.getName())
                            .age(catDTO.getAge())
                            .weight(catDTO.getWeight())
                            .build()));
    }

    @SneakyThrows
    @GetMapping("/api/all")
    public String getAll() {
    List<Cat>cats = catRepo.findAll();
    return objectMapper.writeValueAsString(cats);
    }

    @GetMapping("/api")
    public Cat getCat(@RequestParam int id) {
        return catRepo.findById(id).orElseThrow();
    }

    @DeleteMapping("/api")
    public void deleteCat(@RequestParam int id) {
        catRepo.deleteById(id);
    }

    @PutMapping("/api/add")
    public String changeCat(@RequestBody Cat cat) {
        if(!catRepo.existsById(cat.getId())) {
            return "No such row";
        }
        return catRepo.save(cat).toString();
    }
}
