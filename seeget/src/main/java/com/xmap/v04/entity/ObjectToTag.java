package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("object_to_tag")
public class ObjectToTag {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "object_id")
    private Integer objectId;
    @TableField(value = "tag_id")
    private Integer tagId;
}