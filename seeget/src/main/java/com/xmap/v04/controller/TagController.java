package com.xmap.v04.controller;

import com.xmap.v04.entity.Tag;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.BaseException;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.models.TagModel;
import com.xmap.v04.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "标签接口")
@RequestMapping("api/tag")
public class TagController {
    Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    @PostMapping()
    public ResponseEntity<Tag> addTag(@RequestBody TagModel tagModel) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tagModel.setAuthor(user.getId());
        Tag tag = tagService.add(tagModel);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @DeleteMapping("uuid/{tagUuid}")
    public ResponseEntity<String> deleteTagByUuid(@PathVariable String tagUuid) throws Exception{
        Tag tag = tagService.findTagByUuid(tagUuid);
        if (tag == null) {
            throw new NotFoundException("tag does not exits.");
        } else {
            tagService.deleteTagByUuid(tagUuid);
            return new ResponseEntity<>("tag delete successfully.", HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Integer id) throws Exception{
        Tag tag = tagService.getById(id);
        if (tag == null) {
            throw new NotFoundException("tag does not exits.");
        } else {
            tagService.deleteTag(id);
            return new ResponseEntity<>("tag delete successfully.", HttpStatus.OK);
        }
    }

    @PatchMapping("uuid/{tagUuid}")
    public ResponseEntity<Tag> updateTagByUuid(@PathVariable String tagUuid, @RequestBody Tag tag) throws Exception {
        Tag oldTag = tagService.findTagByUuid(tagUuid);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = user.getId();
        if (oldTag == null) {
            throw new NotFoundException("tag does not exits.");
        } else if (oldTag.getAuthor() != id) {
            throw new BaseException("You does not have authentication.");
        }
        else {
            tag.setUuid(tagUuid);
            Tag newTag = tagService.update(tag);
            return new ResponseEntity<>(newTag, HttpStatus.OK);
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Integer id, @RequestBody Tag tag) throws Exception {
        Tag oldTag = tagService.findTagById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userid = user.getId();
        if (oldTag == null) {
            throw new NotFoundException("tag does not exits.");
        } else if (oldTag.getAuthor() != userid) {
            throw new BaseException("You does not have authentication.");
        }
        else {
            tag.setId(id);
            Tag newTag = tagService.update(tag);
            return new ResponseEntity<>(newTag, HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> findTag(@PathVariable Integer id) throws Exception {
        Tag tag = tagService.findTagById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userid = user.getId();
        if (tag == null) {
            throw new NotFoundException("tag does not exits.");
        } else {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        }
    }

    @GetMapping("uuid/{tagUuid}")
    public ResponseEntity<Tag> findTagByUuid(@PathVariable String tagUuid) throws Exception {
        Tag tag = tagService.findTagByUuid(tagUuid);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = user.getId();
        if (tag == null) {
            throw new NotFoundException("tag does not exits.");
        } else {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        }
    }

    @GetMapping("/self")
    public ResponseEntity<List<Tag>> findSelfTag() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tag> tags = tagService.findTagByAuthor(user.getId());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Tag>> findAvailable() {
        List<Tag> tags = tagService.findAllTag();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

}
