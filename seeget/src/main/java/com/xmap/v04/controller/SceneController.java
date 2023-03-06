package com.xmap.v04.controller;

import com.xmap.v04.entity.DataPackage;
import com.xmap.v04.entity.Scene;
import com.xmap.v04.entity.Tag;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.service.DataPackageService;
import com.xmap.v04.service.SceneService;
import com.xmap.v04.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/scene")
public class SceneController {
    Logger logger = LoggerFactory.getLogger(SceneController.class);

    @Autowired
    SceneService sceneService;

    @Autowired
    DataPackageService dataPackageService;

    @Autowired
    TagService tagService;

    @PostMapping
    public ResponseEntity<Scene> addScene(@RequestBody @Validated Scene scene) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        scene.setAuthor(user.getId());
        Scene addScene = sceneService.add(scene);
        logger.info(scene.toString());
        return new ResponseEntity<>(addScene, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteScene(@PathVariable("id") int id) throws NotFoundException {
        sceneService.delete(id);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteSceneByUuid(@PathVariable("uuid") String uuid) throws NotFoundException {
        sceneService.deleteByUuid(uuid);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<List<Scene>> getPublicScenes() throws NotFoundException {
        List<Scene> scenes = sceneService.getPublicScene();

        return new ResponseEntity<>(scenes, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<Scene>> getSelfScenes() throws NotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Scene> scenes = sceneService.getSelfScene(user.getId());

        return new ResponseEntity<>(scenes, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Scene> getById(@PathVariable("id") int id) throws Exception{
        Scene scene = sceneService.getById(id);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        return new ResponseEntity<>(scene, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<Scene> getByUuid(@PathVariable("uuid") String uuid) throws Exception{
        Scene scene = sceneService.getByUuid(uuid);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        return new ResponseEntity<>(scene, HttpStatus.OK);
    }

    @PatchMapping("{id}/addDp")
    public ResponseEntity<Scene> addDp(@PathVariable("id") int id, @RequestParam int dpId) throws Exception{
        Scene scene = sceneService.getById(id);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        sceneService.addDp(scene.getId(), dpId);
        Scene result = sceneService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/addDp")
    public ResponseEntity<Scene> addDpByUuid(@PathVariable("uuid") String uuid, @RequestParam int dpId) throws Exception{
        Scene scene = sceneService.getByUuid(uuid);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        sceneService.addDp(scene.getId(), dpId);
        Scene result = sceneService.getByUuid(uuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("{id}/addTag")
    public ResponseEntity<Scene> addTag(@PathVariable("id") int id, @RequestParam int tagId) throws Exception{
        Scene scene = sceneService.getById(id);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        Tag tag = tagService.getById(tagId);
        if(tag==null)
            throw new NotFoundException("tag not found!");
        sceneService.addTag(scene.getId(), tagId);
        Scene result = sceneService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/addTag")
    public ResponseEntity<Scene> addTag(@PathVariable("uuid") String uuid, @RequestParam int tagId) throws Exception{
        Scene scene = sceneService.getByUuid(uuid);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        Tag tag = tagService.getById(tagId);
        if(tag==null)
            throw new NotFoundException("tag not found!");
        sceneService.addTag(scene.getId(), tagId);
        Scene result = sceneService.getByUuid(uuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}/deleteDp")
    public ResponseEntity<Scene> deleteDp(@PathVariable("id") int id, @RequestParam int dpId) throws Exception {
        Scene scene = sceneService.getById(id);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        sceneService.deleteDp(scene.getId(), dpId);
        Scene result = sceneService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}/deleteDp")
    public ResponseEntity<Scene> deleteDpByUuid(@PathVariable("uuid") String uuid, @RequestParam int dpId) throws Exception {
        Scene scene = sceneService.getByUuid(uuid);
        if(scene==null)
            throw new NotFoundException("scene not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        sceneService.deleteDp(scene.getId(), dpId);
        Scene result = sceneService.getByUuid(uuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("{id}/updateInfo")
    public ResponseEntity<Scene> updateInfo(@PathVariable("id") int id, @RequestBody Scene scene) throws Exception{
        Scene check = sceneService.getById(id);
        if(check==null)
            throw new NotFoundException("scene not found!");
        Scene result = sceneService.update(id, scene);
        return new ResponseEntity<>(scene, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/updateInfo")
    public ResponseEntity<Scene> updateInfoByUuid(@PathVariable("uuid") String uuid, @RequestBody Scene scene) throws Exception{
        Scene check = sceneService.getByUuid(uuid);
        if(check==null)
            throw new NotFoundException("scene not found!");
        Scene result = sceneService.update(scene.getId(), scene);
        return new ResponseEntity<>(scene, HttpStatus.OK);
    }
}
