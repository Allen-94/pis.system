package com.yuyisz.pis.ui.service.infosources.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.infosource.MediaSourceRepository;
import com.yuyisz.pis.ui.module.infosources.MediaSource;
import com.yuyisz.pis.ui.service.infosources.MediaSourceService;
/**
 * 媒体资源库服务类
 * @author mengz
 *
 */
@Service
public class MediaSourceServiceImpl implements MediaSourceService {
	@Autowired
	private MediaSourceRepository mediaSourceRepository;

	@Override
	public List<MediaSource> findAllMediaSource() {
		return mediaSourceRepository.findAll();
	}

	@Override
	public MediaSource saveMediaSource(MediaSource folder) {
		return mediaSourceRepository.saveAndFlush(folder);
	}

	@Override
	public MediaSource findById(String parentId) {
		return mediaSourceRepository.findOne(parentId);
	}

	@Override
	public List<MediaSource> findAllMediaSourceByParentSourceId(String parentFolderId) {
		return mediaSourceRepository.findByParentSourceId(parentFolderId);
	}
	
}
