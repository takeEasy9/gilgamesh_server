package com.gilgamesh.persistence.entity.gilgamesh.sys;

import com.gilgamesh.persistence.entity.base.VersionControlGenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 岗位信息
 * @createDate 2025/1/26 14:17
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_post")
public class SysPost extends VersionControlGenericBaseEntity {
    @Serial
    private static final long serialVersionUID = -7070763792423091491L;
    /**
     * 岗位中文名称
     */
    @Column(name = "post_cn_name")
    private String postCnName;

    /**
     * 岗位英文名称
     */
    @Column(name = "post_en_name")
    private String postEnName;

    /**
     * 岗位描述
     */
    @Column(name = "post_description", nullable = true)
    private String postDescription;

    public String getPostCnName() {
        return postCnName;
    }

    public void setPostCnName(String postCnName) {
        this.postCnName = postCnName;
    }

    public String getPostEnName() {
        return postEnName;
    }

    public void setPostEnName(String postEnName) {
        this.postEnName = postEnName;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    @Override
    public String toString() {
        return "SysPost{" +
                "super=" + super.toString() +
                ", postCnName='" + postCnName + '\'' +
                ", postEnName='" + postEnName + '\'' +
                ", postDescription='" + postDescription + '\'' +
                '}';
    }
}