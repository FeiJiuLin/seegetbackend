package com.xmap.ar.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
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
@RequestMapping("api/m3d")
public class M3dController {
    Logger logger = LoggerFactory.getLogger(M2dController.class);

    @Autowired
    private M3dService m3dService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<M3d> create(@RequestBody M3d marker) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        M3d newMarker = m3dService.addMaker(marker, user.getId());
        return new ResponseEntity<>(newMarker, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<M3d>> getAll() throws Exception {
        List<M3d> markerList = m3dService.selectALL();
        return new ResponseEntity<>(markerList, HttpStatus.OK);
    }

    @GetMapping("v2")
    public ResponseEntity<Page<M3d>> getPage(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer per_page,
                                             @RequestParam(required = false) String sort,
                                             @RequestParam(required = false) Boolean desc
    ) throws Exception {
        Page<M3d> m3dPage = null;
        List<String> sorts = List.of("create_time", "update_time", "file_size");
        if (sort == null) {
            m3dPage = m3dService.selectByPage((long) page, (long) per_page);
        }
        else if(sorts.contains(sort)) {
            m3dPage = m3dService.selectByPageSort((long) page, (long) per_page, sort, desc);
        }else
            throw new BaseException("sort choice '" +sort + "' is illegal");
        return new ResponseEntity<>(m3dPage, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<M3d>> getSelf() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<M3d> markerList = m3dService.selectByCreator(user.getId());
        return new ResponseEntity<>(markerList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<M3d> getOne(@PathVariable int id) throws Exception {
        M3d marker = m3dService.selectById(id);
        return new ResponseEntity<>(marker,HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<M3d> getOneByUUid(@PathVariable String uuid) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        return new ResponseEntity<>(marker,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {

        M3d marker = m3dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setDelete(true);
        m3dService.update(marker, marker.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(marker.getUid());
        recycle.setType(2);
        recycle.setAuthor(marker.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteByUuid(@PathVariable("uuid") String uuid) throws Exception {

        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setDelete(true);
        m3dService.update(marker, marker.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(marker.getUid());
        recycle.setType(2);
        recycle.setAuthor(marker.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<M3d> update(@PathVariable("uuid") String uuid, @RequestBody M3d entity) throws Exception {
        M3d old = m3dService.selectByUUid(uuid);
        if (old == null)
            throw new NotFoundException("marker not found!");
        entity.setUid(uuid);
        M3d res = m3dService.update(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<M3d> setTitleByUuid(@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setTitle(title);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<M3d> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setFolder(folderUid);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setDescription")
    public ResponseEntity<M3d> setDescriptionByUuid(@PathVariable("uuid") String uuid, @RequestParam String description) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setDescription(description);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<M3d> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setThumb(cover);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<M3d> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setAddress(address);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTags")
    public ResponseEntity<M3d> setTagsByUuid(@PathVariable("uuid") String uuid, @RequestBody List<String> tagUidLists) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        m3dService.setTags(marker.getId(), tagUidLists);
        return new ResponseEntity<>(m3dService.selectByUUid(uuid), HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setContents")
    public ResponseEntity<M3d> setContentsByUuid(@PathVariable("uuid") String uuid, @RequestBody JSON contents) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setContentJson(JSON.toJSONString(contents));
        return new ResponseEntity<>(m3dService.selectByUUid(uuid), HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        M3d marker = m3dService.selectByUUid(uuid);
        if (marker == null)
            throw new NotFoundException("Folder not found!");
        marker.setCollect(collect);
        m3dService.update(marker, marker.getId());
        if(collect) {
            Collection collection = new Collection();
            collection.setUid(marker.getUid());
            collection.setType(2);
            collection.setAuthor(marker.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected",HttpStatus.OK);
        } else {
            collectionService.deleteM3d(marker.getUid(), marker.getAuthor());
            return new ResponseEntity<>("dismiss collection",HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setFolder")
    public ResponseEntity<M3d> setFolder(@PathVariable("id") Integer id, @RequestParam String folderUid) throws Exception {
        M3d marker = m3dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setFolder(folderUid);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setDescription")
    public ResponseEntity<M3d> setDescription (@PathVariable("id") Integer id, @RequestParam String description) throws Exception {
        M3d marker = m3dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setDescription(description);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<M3d> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        M3d marker = m3dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setThumb(cover);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<M3d> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        M3d marker = m3dService.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker not found!");
        marker.setAddress(address);
        M3d res = m3dService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setTags")
    public ResponseEntity<M3d> setTags(@PathVariable("id") Integer id, @RequestBody List<String> tagUidLists) throws Exception {
        m3dService.setTags(id, tagUidLists);
        return new ResponseEntity<>(m3dService.selectById(id), HttpStatus.OK);
    }

    @PatchMapping("{id}/setContents")
    public ResponseEntity<M3d> setContents(@PathVariable("id") Integer id, @RequestBody JSON contents) throws Exception {
        M3d marker = m3dService.selectById(id);
        marker.setContentJson(JSON.toJSONString(contents));
        return new ResponseEntity<>(m3dService.selectById(id), HttpStatus.OK);
    }
}