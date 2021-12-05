package com.asdf148.javaproject.domain.project.controller;

import com.asdf148.javaproject.domain.project.dto.*;
import com.asdf148.javaproject.domain.project.service.ProjectService;
import com.asdf148.javaproject.global.S3Upload;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@RequestMapping("/project")
@RequiredArgsConstructor
@RestController
public class ProjectController {
    private final ProjectService projectService;
    private final S3Upload s3Upload;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    @PostMapping
    public ResponseEntity<String> createProject(@RequestHeader Map<String, String> header, HttpServletRequest request, @ModelAttribute("createProject") CreateProject createProject){
        Set<String> keySet = request.getParameterMap().keySet();
        for(String key: keySet) {
            System.out.println(key + ": " + request.getParameter(key));
        }

        System.out.println();

        String imgUrl = "";

        try{
            imgUrl = s3Upload.upload(createProject.getFile(), "project");
        } catch (Exception e){
            logger.error("S3 Upload: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(
                    projectService.createProject(header.get("authorization").substring(7), imgUrl, createProject),
                    HttpStatus.CREATED
            );
        }
        catch(Exception e){
            logger.error("projectController /: " + e.getMessage());
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/Image")
    public ResponseEntity<Object> uploadImage(@RequestPart MultipartFile file){
        String imgUrl = "";

        try{
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getContentType());
            imgUrl = s3Upload.upload(file, "project");
        } catch (Exception e){
            logger.error("S3 Upload: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(Image.builder().image(imgUrl).build(), HttpStatus.OK);
    }

    @PostMapping("/body")
    public ResponseEntity<Object> createProjectBody(@RequestHeader Map<String, String> header,@RequestBody ProjectBody projectBody){
        try{
            System.out.println(projectBody.getEmails().get(0));

            return new ResponseEntity<>(ProjectId.builder().projectId(projectService.testCreateProject(header.get("authorization").substring(7), projectBody)).build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@RequestHeader Map<String, String> header, @PathVariable("id") ObjectId projectId){
        try{
            return new ResponseEntity<>(projectService.deleteProject(header.get("authorization").substring(7), projectId), HttpStatus.OK);
        }catch (Exception e){
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

    @PutMapping("/{id}")
    public ResponseEntity<String> modifyProject(@RequestHeader Map<String, String> header, @PathVariable("id") ObjectId id, @ModelAttribute("modifyProject") ModifyProject modifyProject){
        String imgUrl = "";

        try{
            imgUrl = s3Upload.upload(modifyProject.getFile(), "project");
        } catch (Exception e){
            logger.error("ProjectController modifyProject S3 Upload: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(projectService.modifyProject(header.get("authorization").substring(7), id, imgUrl, modifyProject), HttpStatus.OK);
        }catch (Exception e){
            logger.error("ProjectController modifyProject: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("close/{id}")
    public ResponseEntity<String> closeProject(@RequestHeader Map<String, String> header, @PathVariable("id") ObjectId id){
        try{
            return new ResponseEntity<>(projectService.closeProject(header.get("authorization").substring(7), id), HttpStatus.OK);
        } catch (Exception e){
            logger.error("ProjectController closeProject: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
