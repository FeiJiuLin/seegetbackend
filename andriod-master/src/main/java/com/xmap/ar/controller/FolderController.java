package com.xmap.ar.controller;

import com.xmap.ar.entity.*;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.CollectionService;
import com.xmap.ar.service.FolderService;
import com.xmap.ar.service.RecycleService;
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

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

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
        folder.setDelete(true);
        folderService.updateFolder(folder, folder.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(folder.getUid());
        recycle.setType(5);
        recycle.setAuthor(folder.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteFolderByUuid(@PathVariable("uuid") String uuid) throws Exception{
        Folder folder = folderService.getFolderByUuid(uuid);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setDelete(true);
        folderService.updateFolder(folder, folder.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(folder.getUid());
        recycle.setType(5);
        recycle.setAuthor(folder.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
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
    public ResponseEntity<List<Folder>> getFolderByUser() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Folder> folders = folderService.getFolderByAuthor(user.getId());
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<Folder> setTitleByUuid (@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        Folder folder = folderService.getFolderByUuid(uuid);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setTitle(title);
        Folder res = folderService.updateFolder(folder, folder.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<Folder> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        Folder folder = folderService.getFolderByUuid(uuid);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setThumb(cover);
        Folder res = folderService.updateFolder(folder, folder.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<Folder> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        Folder folder = folderService.getFolderByUuid(uuid);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setAddress(address);
        Folder res = folderService.updateFolder(folder, folder.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        Folder entity = folderService.getFolderByUuid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        folderService.updateFolder(entity, entity.getId());
        if (collect) {
            Collection collection = new Collection();
            collection.setUid(entity.getUid());
            collection.setType(5);
            collection.setAuthor(entity.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected", HttpStatus.OK);
        } else {
            collectionService.deleteObj(entity.getUid(), entity.getAuthor());
            return new ResponseEntity<>("dismiss collection", HttpStatus.OK);
        }
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<Folder> update (@PathVariable("uuid") String uuid, @RequestBody Folder entity) throws Exception {
        Folder old = folderService.getFolderByUuid(uuid);
        if (old == null)
            throw new NotFoundException("Folder not found!");
        entity.setUid(old.getUid());
        Folder res = folderService.updateFolder(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("{id}/setTitle")
    public ResponseEntity<Folder> setTitle (@PathVariable("id") Integer id, @RequestParam String title) throws Exception {
        Folder folder = folderService.getFolderById(id);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setTitle(title);
        Folder res = folderService.updateFolder(folder, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("{id}/setCover")
    public ResponseEntity<Folder> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        Folder folder = folderService.getFolderById(id);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setThumb(cover);
        Folder res = folderService.updateFolder(folder, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("{id}/setAddress")
    public ResponseEntity<Folder> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        Folder folder = folderService.getFolderById(id);
        if (folder == null)
            throw new NotFoundException("Folder not found!");
        folder.setAddress(address);
        Folder res = folderService.updateFolder(folder, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

