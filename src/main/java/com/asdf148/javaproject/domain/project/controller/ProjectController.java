package com.asdf148.javaproject.domain.project.controller;

import com.asdf148.javaproject.domain.project.dto.CreateProject;
import com.asdf148.javaproject.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/")
    public ResponseEntity<String> createProject(@RequestHeader Map<String, String> header, CreateProject createProject){
        try{
            projectService.createProject(header.get("authorization").substring(7), createProject);
            return new ResponseEntity<>("sucess", HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project")
    public ResponseEntity<String> addPerson(@RequestParam("email") String email, @RequestParam("id") ObjectId projectId){
        try{
            projectService.addPersonnel(email, projectId);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
