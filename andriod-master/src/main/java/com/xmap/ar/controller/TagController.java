package com.xmap.ar.controller;

import com.xmap.ar.entity.Tag;
import com.xmap.ar.entity.TagLink;
import com.xmap.ar.entity.User;
import com.xmap.ar.service.TagService;
import io.swagger.annotations.Api;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    private Logger logger = LoggerFactory.getLogger(TagController.class);

    @PostMapping
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tag.setAuthor(user.getId());
        Tag newTag = tagService.addTag(tag);
        return new ResponseEntity<>(newTag, HttpStatus.CREATED);
    }

    @GetMapping("self")
    public ResponseEntity<List<Tag>> getSelfTag(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tag> tags = tagService.getByAuthor(user.getId());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("available")
    public ResponseEntity<List<Tag>> getAvailable(){
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }

    @PostMapping("update/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") int id,  @RequestBody Tag tag) {
        return new ResponseEntity<>(tagService.update(tag, id), HttpStatus.OK);
    }

    @PostMapping("uuid/update/{uuid}")
    public ResponseEntity<Tag> updateTagByUuid(@PathVariable("uuid") String uuid,  @RequestBody Tag tag) {
        Tag tag1 = tagService.selectByUuid(uuid);
        return new ResponseEntity<>(tagService.update(tag, tag1.getId()), HttpStatus.OK);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteTag(@PathVariable("id") int id) {
        tagService.deleteById(id);
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteTagByUuid(@PathVariable("uuid") String uuid) {
        Tag tag = tagService.selectByUuid(uuid);
        tagService.deleteById(tag.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}/projects")
    public ResponseEntity<List<TagLink>> getProjects(@PathVariable("uuid") String  uuid) {
        List<TagLink> tagLinks = tagService.selectLink(uuid);
        return new ResponseEntity<>(tagLinks, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}/remove")
    public ResponseEntity<String> removeTag(@PathVariable("uuid") String uuid) {
        tagService.remove(uuid);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }
}
