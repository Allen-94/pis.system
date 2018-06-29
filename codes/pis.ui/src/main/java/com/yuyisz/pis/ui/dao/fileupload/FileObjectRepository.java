package com.yuyisz.pis.ui.dao.fileupload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yuyisz.pis.ui.module.resources.FileObject;

public interface FileObjectRepository extends JpaRepository<FileObject, String>, JpaSpecificationExecutor<FileObject> {

	
}
