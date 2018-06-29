package com.yuyisz.pis.ui.service.infosources;

import java.util.List;

import com.yuyisz.pis.ui.module.infosources.MediaSource;

public interface MediaSourceService {

	List<MediaSource> findAllMediaSource();

	MediaSource saveMediaSource(MediaSource folder);

	MediaSource findById(String parentId);

	List<MediaSource> findAllMediaSourceByParentSourceId(String parentFolderId);

}
