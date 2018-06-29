package com.yuyisz.pis.ui.service.player.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.player.ContentObjectRepository;
import com.yuyisz.pis.ui.dao.player.ContentPackageRepository;
import com.yuyisz.pis.ui.module.player.ContentObject;
import com.yuyisz.pis.ui.module.player.ContentPackage;
import com.yuyisz.pis.ui.service.player.ContentPackageService;
@Service
public class ContentPackageServiceImpl implements ContentPackageService {

	@Autowired
	private ContentPackageRepository contentPackageRepository;
	
	@Autowired
	private ContentObjectRepository contentObjectRepository;
	
	@Override
	public List<ContentPackage> findAll() {
		return contentPackageRepository.findAll();
	}
	@Override
	public ContentPackage findById(long contentId) {
		return contentPackageRepository.findOne(contentId);
	}
	@Override
	public void saveContentPackage(ContentPackage cp) {
		contentPackageRepository.saveAndFlush(cp);
	}
	@Override
	public boolean delContentPackage(long id) {
		try{
			contentPackageRepository.delete(id);
		}catch(IllegalArgumentException e) {
			return false;
		}
		return true;
	}
	@Override
	public List<ContentObject> findPackageContentByPackageId(long packageId) {
		ContentPackage cp = contentPackageRepository.findOne(packageId);
		return cp.getContents();
	}
	@Override
	public boolean SavePackageContent(ContentObject co) {
		try {
			if(co.getId()==null||co.getId().trim().equals("")) {
				co.setId(UUID.randomUUID().toString());
			}
			ContentObject cObject = contentObjectRepository.save(co);
			return cObject != null?true:false;
		}catch(Exception e) {
			return false;
		}
	}
	@Override
	public ContentObject findOneContentObjectById(String id) {
		return contentObjectRepository.findOne(id);
	}

}
