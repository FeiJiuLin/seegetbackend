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
@TableName("object_to_dp")
public class ObjectToDp {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "data_package_id")
    private Integer datapackageId;
    @TableField(value = "object_id")
    private Integer objectId;
}
