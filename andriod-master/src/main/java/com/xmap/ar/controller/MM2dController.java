package com.xmap.ar.controller;

import com.xmap.ar.entity.*;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.CollectionService;
import com.xmap.ar.service.MM2dService;
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
@RequestMapping("api/mm2d")
public class MM2dController {
    Logger logger = LoggerFactory.getLogger(MM2dController.class);

    @Autowired
    private MM2dService mm2dService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<MM2d> addMM2d(@RequestBody @Validated MM2d mm2d) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mm2d.setAuthor(user.getId());
        logger.info(mm2d.toString());
        MM2d newMM2d = mm2dService.addMM2d(mm2d);
        return new ResponseEntity<>(newMM2d, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMM2d(@PathVariable("id") int id) throws Exception{
        MM2d mm2d = mm2dService.getMM2dById(id);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setDelete(true);
        mm2dService.updateMM2d(mm2d, mm2d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(mm2d.getUid());
        recycle.setType(6);
        recycle.setAuthor(mm2d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteMM2dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        MM2d mm2d = mm2dService.getMM2dByUuid(uuid);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setDelete(true);
        mm2dService.updateMM2d(mm2d, mm2d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(mm2d.getUid());
        recycle.setType(6);
        recycle.setAuthor(mm2d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<MM2d> getMM2dById(@PathVariable("id") int id) throws Exception{
        MM2d mm2d = mm2dService.getMM2dById(id);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        return new ResponseEntity<>(mm2d, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<MM2d> getMM2dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        MM2d mm2d = mm2dService.getMM2dByUuid(uuid);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        return new ResponseEntity<>(mm2d, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<MM2d>> getMM2dByUser() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MM2d> mm2ds = mm2dService.getMM2dByAuthor(user.getId());
        return new ResponseEntity<>(mm2ds, HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<MM2d> update(@PathVariable("uuid") String uuid, @RequestBody MM2d entity) throws Exception {
        MM2d old = mm2dService.getMM2dByUuid(uuid);
        if (old == null)
            throw new NotFoundException("marker not found!");
        entity.setUid(uuid);
        MM2d res = mm2dService.updateMM2d(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<MM2d> setTitleByUuid (@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        MM2d mm2d = mm2dService.getMM2dByUuid(uuid);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setTitle(title);
        MM2d res = mm2dService.updateMM2d(mm2d, mm2d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<MM2d> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        MM2d mm2d = mm2dService.getMM2dByUuid(uuid);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setThumb(cover);
        MM2d res = mm2dService.updateMM2d(mm2d, mm2d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<MM2d> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        MM2d mm2d = mm2dService.getMM2dByUuid(uuid);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setAddress(address);
        MM2d res = mm2dService.updateMM2d(mm2d, mm2d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<MM2d> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        MM2d marker = mm2dService.getMM2dByUuid(uuid);
        if (marker == null)
            throw new NotFoundException("obj not found!");
        marker.setFolder(folderUid);
        MM2d res = mm2dService.updateMM2d(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        MM2d entity = mm2dService.getMM2dByUuid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        mm2dService.updateMM2d(entity, entity.getId());
        if (collect) {
            Collection collection = new Collection();
            collection.setUid(entity.getUid());
            collection.setType(6);
            collection.setAuthor(entity.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected", HttpStatus.OK);
        } else {
            collectionService.deleteObj(entity.getUid(), entity.getAuthor());
            return new ResponseEntity<>("dismiss collection", HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setTitle")
    public ResponseEntity<MM2d> setTitle (@PathVariable("id") Integer id, @RequestParam String title) throws Exception {
        MM2d mm2d = mm2dService.getMM2dById(id);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setTitle(title);
        MM2d res = mm2dService.updateMM2d(mm2d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<MM2d> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        MM2d mm2d = mm2dService.getMM2dById(id);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setThumb(cover);
        MM2d res = mm2dService.updateMM2d(mm2d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<MM2d> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        MM2d mm2d = mm2dService.getMM2dById(id);
        if (mm2d == null)
            throw new NotFoundException("MM2d not found!");
        mm2d.setAddress(address);
        MM2d res = mm2dService.updateMM2d(mm2d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

