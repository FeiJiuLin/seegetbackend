package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@TableName("default_record")
public class DefaultRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull
    private String owner;

//    @TableField("data_package")
//    private String dataPackage;
    @NotNull
    private String scene;
    @NotNull
    private String object;
    @NotNull
    private String folder;
}
