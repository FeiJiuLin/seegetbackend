package com.xmap.ar.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.Collection;
import com.xmap.ar.entity.M2d;
import com.xmap.ar.entity.Recycle;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.BaseException;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/m2d")
public class  M2dController {
    Logger logger = LoggerFactory.getLogger(M2dController.class);

    @Autowired
    private M2dService m2dService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<M2d> create(@RequestBody M2d marker) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        M2d newMarker = m2dService.addMaker(marker, user.getId());
        return new ResponseEntity<>(newMarker, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<M2d>> getAll() throws Exception{
        List<M2d> markerList = m2dService.selectALL();
        return new ResponseEntity<>(markerList, HttpStatus.OK);
    }

    @GetMapping("v2")
    public ResponseEntity<Page<M2d>> getPage(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer per_page,
                                             @RequestParam(required = false) String sort,
                                             @RequestParam(required = false) Boolean desc
    ) throws Exception {
        Page<M2d> m2dPage = null;
        List<String> sorts = List.of("create_time", "update_time", "file_size");
        if (sort == null) {
            m2dPage = m2dService.selectByPage((long) page, (long) per_page);
        }
        else if(sorts.contains(sort)) {
            m2dPage = m2dService.selectByPageSort((long) page, (long) per_page, sort, desc);
        }else
            throw new BaseException("sort choice '" +sort + "' is illegal");
        return new ResponseEntity<>(m2dPage, HttpStatus.OK);
    }

//    @GetMapping("filter/")
//    public ResponseEntity<Page<M2d>> getPage(@RequestParam int page, @RequestParam int per_page) throws Exception{
////        Page<M2d> markerList = m2dService.selectByPage(0,10);
//        Page<M2d> m2dPage = m2dService.selectByPage((long) page, (long) per_page);
//        return new ResponseEntity<>(m2dPage, HttpStatus.OK);
//    }

    @GetMapping("self")
    public ResponseEntity<List<M2d>> getSelf() throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<M2d> markerList = m2dService.selectByAuthor(user.getId());
        return new ResponseEntity<>(markerList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<M2d> getOne(@PathVariable int id) throws Exception {
        M2d marker = m2dService.selectById(id);
        return new ResponseEntity<>(marker,HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<M2d> getOneByUUid(@PathVariable String uuid) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        return new ResponseEntity<>(marker,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {

        M2d marker = m2dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("Folder not found!");
        marker.setDelete(true);
        m2dService.update(marker, marker.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(marker.getUid());
        recycle.setAuthor(marker.getAuthor());
        recycle.setType(1);
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteByUuid(@PathVariable("uuid") String uuid) throws Exception {

        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("Folder not found!");
        marker.setDelete(true);
        m2dService.update(marker, marker.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(marker.getUid());
        recycle.setType(1);
        recycle.setAuthor(marker.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<M2d> update(@PathVariable("uuid") String uuid, @RequestBody M2d entity) throws Exception {
        M2d old = m2dService.selectByUUid(uuid);
        if (old == null)
            throw new NotFoundException("marker not found!");
        entity.setUid(uuid);
        M2d res = m2dService.update(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<M2d> setTitleByUuid(@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setTitle(title);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<M2d> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setFolder(folderUid);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setDescription")
    public ResponseEntity<M2d> setDescriptionByUuid(@PathVariable("uuid") String uuid, @RequestParam String description) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setDescription(description);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<M2d> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setThumb(cover);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<M2d> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setAddress(address);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTags")
    public ResponseEntity<M2d> setTags(@PathVariable("uuid") String uuid, @RequestBody List<String> tagUidLists) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        m2dService.setTags(marker.getId(), tagUidLists);
        return new ResponseEntity<>(m2dService.selectByUUid(uuid), HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setSrc")
    public ResponseEntity<M2d> setSrc(@PathVariable("uuid") String uuid, @RequestParam String src) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setSrc(src);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setEdited")
    public ResponseEntity<M2d> setEdited(@PathVariable("uuid") String uuid, @RequestParam String edited) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setEdited(edited);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        M2d marker = m2dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("Folder not found!");
        marker.setCollect(collect);
        m2dService.update(marker, marker.getId());
        if(collect) {
            Collection collection = new Collection();
            collection.setUid(marker.getUid());
            collection.setType(1);
            collection.setAuthor(marker.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected",HttpStatus.OK);
        } else {
            collectionService.deleteM2d(marker.getUid(), marker.getAuthor());
            return new ResponseEntity<>("dismiss collection",HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setFolder")
    public ResponseEntity<M2d> setFolder(@PathVariable("id") Integer id, @RequestParam String folderUid) throws Exception {
        M2d marker = m2dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setFolder(folderUid);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setDescription")
    public ResponseEntity<M2d> setDescription (@PathVariable("id") Integer id, @RequestParam String description) throws Exception {
        M2d marker = m2dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setDescription(description);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<M2d> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        M2d marker = m2dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setThumb(cover);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<M2d> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        M2d marker = m2dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setAddress(address);
        M2d res = m2dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setTags")
    public ResponseEntity<M2d> setTags(@PathVariable("id") Integer id, @RequestBody List<String> tagUidLists) throws Exception {
        m2dService.setTags(id, tagUidLists);
        return new ResponseEntity<>(m2dService.selectById(id), HttpStatus.OK);
    }
}
