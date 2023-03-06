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
@TableName("scene_to_dp")
public class SceneToDP {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "data_package_id")
    private Integer datapackageId;
    @TableField(value = "scene_id")
    private Integer sceneId;
}