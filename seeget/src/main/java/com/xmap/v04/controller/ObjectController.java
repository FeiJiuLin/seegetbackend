package com.xmap.v04.controller;

import com.xmap.v04.entity.*;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.service.DataPackageService;
import com.xmap.v04.service.ObjectService;
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
@RequestMapping("api/object")
public class ObjectController {
    Logger logger = LoggerFactory.getLogger(ObjectController.class);

    @Autowired
    ObjectService objectService;

    @Autowired
    DataPackageService dataPackageService;

    @Autowired
    TagService tagService;

    @PostMapping
    public ResponseEntity<Objects> addObject(@RequestBody @Validated Objects object) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        object.setAuthor(user.getId());
        Objects addObject = objectService.add(object);
        logger.info(object.toString());
        return new ResponseEntity<>(addObject, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteObject(@PathVariable("id") int id) throws Exception{
        objectService.delete(id);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteObjectByUuid(@PathVariable("uuid") String uuid) throws Exception{
        objectService.deleteByUuid(uuid);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<List<Objects>> getPublicObjects() throws Exception {
        List<Objects> objects = objectService.getPublicObject();

        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<Objects>> getSelfObjects() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Objects> objects = objectService.getSelfScene(user.getId());

        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Objects> getById(@PathVariable("id") int id) throws Exception{
        Objects object = objectService.getById(id);
        if(object==null)
            throw new NotFoundException("object not found!");
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<Objects> getByUuid(@PathVariable("uuid") String uuid) throws Exception{
        Objects object = objectService.getByUuid(uuid);
        if(object==null)
            throw new NotFoundException("object not found!");
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PatchMapping("{id}/addDp")
    public ResponseEntity<Objects> addDp(@PathVariable("id") int id, @RequestParam int dpId) throws Exception{
        Objects object = objectService.getById(id);
        if(object==null)
            throw new NotFoundException("object not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        objectService.addDp(object.getId(), dpId);
        Objects result = objectService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/addDp")
    public ResponseEntity<Objects> addDpByUuid(@PathVariable("uuid") String uuid, @RequestParam int dpId) throws Exception{
        Objects object = objectService.getByUuid(uuid);
        if(object==null)
            throw new NotFoundException("object not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        objectService.addDp(object.getId(), dpId);
        Objects result = objectService.getByUuid(uuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("{id}/addTag")
    public ResponseEntity<Objects> addTag(@PathVariable("id") int id, @RequestParam int tagId) throws Exception{
        Objects object = objectService.getById(id);
        if(object==null)
            throw new NotFoundException("object not found!");
        Tag tag = tagService.getById(tagId);
        if(tag==null)
            throw new NotFoundException("tag not found!");
        objectService.addTag(object.getId(), tagId);
        Objects result = objectService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/addTag")
    public ResponseEntity<Objects> addTagByUuid(@PathVariable("uuid") String uuid, @RequestParam int tagId) throws Exception{
        Objects object = objectService.getByUuid(uuid);
        if(object==null)
            throw new NotFoundException("object not found!");
        Tag tag = tagService.getById(tagId);
        if(tag==null)
            throw new NotFoundException("tag not found!");
        objectService.addTag(object.getId(), tagId);
        Objects result = objectService.getByUuid(uuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}/deleteDp")
    public ResponseEntity<Objects> deleteDp(@PathVariable("id") int id, @RequestParam int dpId) throws Exception {
        Objects object = objectService.getById(id);
        if(object==null)
            throw new NotFoundException("object not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        objectService.deleteDp(object.getId(), dpId);
        Objects result = objectService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}/deleteDp")
    public ResponseEntity<Objects> deleteDpByUuid(@PathVariable("uuid") String uuid, @RequestParam int dpId) throws Exception {
        Objects object = objectService.getByUuid(uuid);
        if(object==null)
            throw new NotFoundException("object not found!");
        DataPackage dataPackage = dataPackageService.getById(dpId);
        if(dataPackage==null)
            throw new NotFoundException("dataPackage not found!");
        objectService.deleteDp(object.getId(), dpId);
        Objects result = objectService.getByUuid(uuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("{id}/updateInfo")
    public ResponseEntity<Objects> updateInfo(@PathVariable("id") int id, @RequestBody Objects object) throws Exception{
        Objects check = objectService.getById(id);
        if(check==null)
            throw new NotFoundException("scene not found!");
        Objects result = objectService.update(id, object);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/updateInfo")
    public ResponseEntity<Objects> updateInfoByUuid(@PathVariable("uuid") String uuid, @RequestBody Objects object) throws Exception{
        Objects check = objectService.getByUuid(uuid);
        if(check==null)
            throw new NotFoundException("scene not found!");
        Objects result = objectService.update(check.getId(), object);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
