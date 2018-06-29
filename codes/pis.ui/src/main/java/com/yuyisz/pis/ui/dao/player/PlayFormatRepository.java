package com.yuyisz.pis.ui.dao.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.player.PlayerFormat;
@Repository
public interface PlayFormatRepository extends JpaRepository<PlayerFormat, Long>, JpaSpecificationExecutor<PlayerFormat> {

}
