package com.asdf148.javaproject.domain.project.controller;

import com.asdf148.javaproject.domain.project.dto.CreateProject;
import com.asdf148.javaproject.domain.project.service.ProjectService;
import com.asdf148.javaproject.global.S3Upload;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/project")
@RequiredArgsConstructor
@RestController
public class ProjectController {
    private final ProjectService projectService;
    private final S3Upload s3Upload;

    @PostMapping
    public ResponseEntity<String> createProject(@RequestHeader Map<String, String> header, @ModelAttribute("createProject") CreateProject createProject){
        String imgUrl = "";

        try{
            System.out.println(createProject.getProjectName());
            imgUrl = s3Upload.upload(createProject.getFile(), "project");
        } catch (Exception e){
            System.out.println("S3 Upload: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try{
            projectService.createProject(header.get("authorization").substring(7), imgUrl, createProject);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }
        catch(Exception e){
            System.out.println("projectController /: " + e.getMessage());
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/person")
    public ResponseEntity<String> addPerson(@RequestParam("email") String email, @RequestParam("id") ObjectId projectId){
        try{
            projectService.addPersonnel(email, projectId);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
