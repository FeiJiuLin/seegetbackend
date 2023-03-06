package com.xmap.v04.controller;

import com.xmap.v04.entity.DataPackage;
import com.xmap.v04.entity.Folder;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.FolderMapper;
import com.xmap.v04.service.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/folder")
public class FolderController {
    Logger logger = LoggerFactory.getLogger(FolderController.class);

    @Autowired
    private FolderService folderService;

    @PostMapping()
    public ResponseEntity<Folder> addFolder(@RequestBody @Validated Folder folder) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        folder.setAuthor(user.getId());
        logger.info(folder.toString());
        Folder newFolder = folderService.addFolder(folder);
        return new ResponseEntity<>(newFolder, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteFolder(@PathVariable("id") int id) throws Exception{
        Folder folder = folderService.getFolderById(id);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folderService.deleteFolder(id);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteFolderByUuid(@PathVariable("uuid") String uuid) throws Exception{
        Folder folder = folderService.getFolderByUuid(uuid);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folderService.deleteFolder(folder.getId());
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable("id") int id, @RequestBody Folder update) throws Exception{
        Folder folder2 = folderService.getFolderById(id);
        if (folder2 == null)
            throw new NotFoundException("Folder not found!");
        Folder folder = folderService.updateFolder(update, id);
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}")
    public ResponseEntity<Folder> updateFolderByUuid(@PathVariable("uuid") String uuid, @RequestBody Folder update) throws Exception{
        Folder folder2 = folderService.getFolderByUuid(uuid);
        if (folder2 == null)
            throw new NotFoundException("Folder not found!");
        Folder folder = folderService.updateFolder(update, folder2.getId());
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable("id") int id) throws Exception{
        Folder folder = folderService.getFolderById(id);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<Folder> getFolderByUuid(@PathVariable("uuid") String uuid) throws Exception{
        Folder folder = folderService.getFolderByUuid(uuid);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<Folder>> getFolderByUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Folder> folders = folderService.getFolderByAuthor(user.getId());
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @GetMapping("filter")
    public ResponseEntity<List<Folder>> filterFolderByType(@RequestParam(value = "type") String type){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Folder> folders = folderService.filterByType(type, user.getId());
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

}
