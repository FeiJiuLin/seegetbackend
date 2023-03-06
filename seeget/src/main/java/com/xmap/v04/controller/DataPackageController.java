package com.xmap.v04.controller;

import com.xmap.v04.entity.DataBlock;
import com.xmap.v04.entity.DataPackage;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.models.AddDataBlock;
import com.xmap.v04.service.DataBlockService;
import com.xmap.v04.service.DataPackageService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;

@RestController()
@RequestMapping("api/datapackage")
public class DataPackageController {
    Logger logger = LoggerFactory.getLogger(DataPackageController.class);

    @Autowired
    private DataPackageService dataPackageService;

    @Autowired
    private DataBlockService dataBlockService;

    @PostMapping()
    public ResponseEntity<DataPackage> addDataPackage(@RequestBody @Validated DataPackage aPackage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DataPackage dataPackage = dataPackageService.addDataPackage(aPackage, user);
        logger.info(dataPackage.toString());
        return new ResponseEntity<>(dataPackage, HttpStatus.CREATED);
    }

//    @GetMapping()
//    public ResponseEntity<List<DataPackageResponse>> getDataPackageList() {
//        List<DataPackage> dataPackageList = dataPackageService.getAll();
//        return new ResponseEntity<>(Transform(dataPackageList), HttpStatus.OK);
//    }
//

    @GetMapping(value = "self")
    public ResponseEntity<List<DataPackage>> getSelfDataPackages() throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<DataPackage> dataPackageList = dataPackageService.getByAuthor(user.getId());
        return new ResponseEntity<>(dataPackageList, HttpStatus.OK);
    }

    @GetMapping(value = "public")
    public ResponseEntity<List<DataPackage>> getPublicDataPackages() throws Exception{
        List<DataPackage> dataPackageList = dataPackageService.getPublic();
        return new ResponseEntity<>(dataPackageList, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<DataPackage> getDataPackageById(@PathVariable("id") Integer id) throws Exception{
        DataPackage dataPackage = dataPackageService.getById(id);
        if (dataPackage == null)
            throw new NotFoundException("data package not found");
        else
            return new ResponseEntity<>(dataPackage, HttpStatus.OK);
    }

    @GetMapping(value = "uuid/{uuid}")
    public ResponseEntity<DataPackage> getDataPackageByUuid(@PathVariable("uuid") String uuid) throws Exception{
        DataPackage dataPackage = dataPackageService.getByUuid(uuid);
        if (dataPackage == null)
            throw new NotFoundException("data package not found");
        else
            return new ResponseEntity<>(dataPackageService.getById(dataPackage.getId()), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteDataPackageById(@PathVariable("id") Integer id) throws Exception {
        DataPackage dataPackage = dataPackageService.getById(id);
        if (dataPackage == null)
            throw new NotFoundException("data package not found");
        dataPackageService.deleteById(id);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping(value = "uuid/{uuid}")
    public ResponseEntity<String> deleteDataPackageByUuid(@PathVariable("uuid") String uuid) throws Exception {
        DataPackage dataPackage = dataPackageService.getByUuid(uuid);
        if (dataPackage == null)
            throw new NotFoundException("data package not found");
        dataPackageService.deleteById(dataPackage.getId());
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<DataPackage> updateDataPackage(@PathVariable("id") Integer id, @RequestBody DataPackage dataPackage) throws Exception{
        DataPackage dataPackage1 = dataPackageService.getById(id);
        if (dataPackage1 == null)
            throw new NotFoundException("data package not found");

        dataPackage.setId(id);
        DataPackage updateDataPackage = dataPackageService.update(dataPackage);
        return new ResponseEntity<>(dataPackageService.getById(updateDataPackage.getId()), HttpStatus.OK);
    }

    @PutMapping(value = "uuid/{uuid}")
    public ResponseEntity<DataPackage> updateDataPackageByUuid(@PathVariable("uuid") String uuid, @RequestBody DataPackage dataPackage) throws Exception{
        DataPackage dataPackage1 = dataPackageService.getByUuid(uuid);
        if (dataPackage1 == null)
            throw new NotFoundException("data package not found");

        dataPackage.setId(dataPackage.getId());
        DataPackage updateDataPackage = dataPackageService.update(dataPackage);
        return new ResponseEntity<>(dataPackageService.getById(updateDataPackage.getId()), HttpStatus.OK);
    }

    @PatchMapping(value = "{id}/tag")
    public ResponseEntity<DataPackage> addTag(@PathVariable("id") Integer id, @RequestBody List<Integer> tags) throws Exception{
        DataPackage dataPackage1 = dataPackageService.getById(id);
        if (dataPackage1 == null)
            throw new NotFoundException("data package not found");

        dataPackageService.updateTag(id, tags);
        return new ResponseEntity<>(dataPackageService.getById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "uuid/{uuid}/tag")
    public ResponseEntity<DataPackage> addTagByUuid(@PathVariable("uuid") String uuid, @RequestBody List<Integer> tags) throws Exception{
        DataPackage dataPackage1 = dataPackageService.getByUuid(uuid);
        if (dataPackage1 == null)
            throw new NotFoundException("data package not found");

        dataPackageService.updateTag(dataPackage1.getId(), tags);
        return new ResponseEntity<>(dataPackageService.getById(dataPackage1.getId()), HttpStatus.OK);
    }

    @PatchMapping(value = "{id}/datablock")
    public ResponseEntity<DataPackage> addDataBlock(@PathVariable("id") Integer id, @RequestBody @Validated AddDataBlock addDataBlock) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DataPackage dataPackage1 = dataPackageService.getById(id);
        if (dataPackage1 == null)
            throw new NotFoundException("data package not found");

        addDataBlock.setDatapackedId(id);
        dataBlockService.addDataBlock(addDataBlock, user.getId());
        return new ResponseEntity<>(dataPackageService.getById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/uuid/{uuid}/datablock")
    public ResponseEntity<DataPackage> addDataBlock(@PathVariable("uuid") String uuid, @RequestBody @Validated AddDataBlock addDataBlock) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DataPackage dataPackage1 = dataPackageService.getByUuid(uuid);
        if (dataPackage1 == null)
            throw new NotFoundException("data package not found");

        addDataBlock.setDatapackedId(dataPackage1.getId());
        dataBlockService.addDataBlock(addDataBlock, user.getId());
        return new ResponseEntity<>(dataPackageService.getById(dataPackage1.getId()), HttpStatus.OK);
    }
}
