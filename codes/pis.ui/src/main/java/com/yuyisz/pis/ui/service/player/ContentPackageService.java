package com.yuyisz.pis.ui.service.player;

import java.util.List;

import com.yuyisz.pis.ui.module.player.ContentObject;
import com.yuyisz.pis.ui.module.player.ContentPackage;

public interface ContentPackageService {

	List<ContentPackage> findAll();

	ContentPackage findById(long contentId);

	void saveContentPackage(ContentPackage cp);

	boolean delContentPackage(long id);

	List<ContentObject> findPackageContentByPackageId(long packageId);
	
	boolean SavePackageContent(ContentObject co);

	ContentObject findOneContentObjectById(String id);
}
