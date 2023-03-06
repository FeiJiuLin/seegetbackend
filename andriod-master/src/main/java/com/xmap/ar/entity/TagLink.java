package com.xmap.ar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tag_link")
@Data@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class TagLink {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String tag;

    private String project;

}
