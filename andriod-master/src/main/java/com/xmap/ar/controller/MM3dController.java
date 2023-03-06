package com.xmap.ar.controller;

import com.xmap.ar.entity.Collection;
import com.xmap.ar.entity.MM3d;
import com.xmap.ar.entity.Recycle;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.CollectionService;
import com.xmap.ar.service.MM3dService;
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
@RequestMapping("api/mm3d")
public class MM3dController {
    Logger logger = LoggerFactory.getLogger(MM3dController.class);

    @Autowired
    private MM3dService mm3dService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<MM3d> addMM3d(@RequestBody @Validated MM3d mm3d) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mm3d.setAuthor(user.getId());
        logger.info(mm3d.toString());
        MM3d newMM3d = mm3dService.addMM3d(mm3d);
        return new ResponseEntity<>(newMM3d, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMM3d(@PathVariable("id") int id) throws Exception{
        MM3d mm3d = mm3dService.getMM3dById(id);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setDelete(true);
        mm3dService.updateMM3d(mm3d, mm3d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(mm3d.getUid());
        recycle.setType(7);
        recycle.setAuthor(mm3d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteMM3dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        MM3d mm3d = mm3dService.getMM3dByUuid(uuid);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setDelete(true);
        mm3dService.updateMM3d(mm3d, mm3d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(mm3d.getUid());
        recycle.setType(7);
        recycle.setAuthor(mm3d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<MM3d> getMM3dById(@PathVariable("id") int id) throws Exception{
        MM3d mm3d = mm3dService.getMM3dById(id);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        return new ResponseEntity<>(mm3d, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<MM3d> getMM3dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        MM3d mm3d = mm3dService.getMM3dByUuid(uuid);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        return new ResponseEntity<>(mm3d, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<MM3d>> getMM3dByUser() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MM3d> mm3ds = mm3dService.getMM3dByAuthor(user.getId());
        return new ResponseEntity<>(mm3ds, HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<MM3d> update(@PathVariable("uuid") String uuid, @RequestBody MM3d entity) throws Exception {
        MM3d old = mm3dService.getMM3dByUuid(uuid);
        if (old == null)
            throw new NotFoundException("marker not found!");
        entity.setUid(uuid);
        MM3d res = mm3dService.updateMM3d(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<MM3d> setTitleByUuid (@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        MM3d mm3d = mm3dService.getMM3dByUuid(uuid);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setTitle(title);
        MM3d res = mm3dService.updateMM3d(mm3d, mm3d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<MM3d> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        MM3d mm3d = mm3dService.getMM3dByUuid(uuid);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setThumb(cover);
        MM3d res = mm3dService.updateMM3d(mm3d, mm3d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<MM3d> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        MM3d mm3d = mm3dService.getMM3dByUuid(uuid);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setAddress(address);
        MM3d res = mm3dService.updateMM3d(mm3d, mm3d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<MM3d> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        MM3d marker = mm3dService.getMM3dByUuid(uuid);
        if (marker == null)
            throw new NotFoundException("obj not found!");
        marker.setFolder(folderUid);
        MM3d res = mm3dService.updateMM3d(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        MM3d entity = mm3dService.getMM3dByUuid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        mm3dService.updateMM3d(entity, entity.getId());
        if (collect) {
            Collection collection = new Collection();
            collection.setUid(entity.getUid());
            collection.setType(7);
            collection.setAuthor(entity.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected", HttpStatus.OK);
        } else {
            collectionService.deleteObj(entity.getUid(), entity.getAuthor());
            return new ResponseEntity<>("dismiss collection", HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setTitle")
    public ResponseEntity<MM3d> setTitle (@PathVariable("id") Integer id, @RequestParam String title) throws Exception {
        MM3d mm3d = mm3dService.getMM3dById(id);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setTitle(title);
        MM3d res = mm3dService.updateMM3d(mm3d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<MM3d> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        MM3d mm3d = mm3dService.getMM3dById(id);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setThumb(cover);
        MM3d res = mm3dService.updateMM3d(mm3d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<MM3d> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        MM3d mm3d = mm3dService.getMM3dById(id);
        if (mm3d == null)
            throw new NotFoundException("MM3d not found!");
        mm3d.setAddress(address);
        MM3d res = mm3dService.updateMM3d(mm3d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

