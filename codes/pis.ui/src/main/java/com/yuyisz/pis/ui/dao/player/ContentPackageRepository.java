package com.yuyisz.pis.ui.dao.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.player.ContentPackage;
@Repository
public interface ContentPackageRepository extends JpaRepository<ContentPackage, Long>, JpaSpecificationExecutor<ContentPackage> {

}
