package com.yuyisz.pis.ui.dao.infosource;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.infosources.MediaSource;

@Repository
public interface MediaSourceRepository extends JpaRepository<MediaSource, String>,JpaSpecificationExecutor<MediaSource> {

	List<MediaSource> findByParentSourceId(String parentFolderId);

}
