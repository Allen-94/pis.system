package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.Screen;
/**
 * 显示屏dao
 * @author 郭芝辰
 *
 */
@Repository
public interface ScreenRepository extends JpaRepository<Screen,Integer>, JpaSpecificationExecutor<Screen> {
	@Query(value="SELECT *\r\n" + 
			"	FROM public.screen\r\n" + 
			"    where line_id=?1 and station_id=?2 and pc_id=?3 and screen_id =?4",nativeQuery=true)
	public Screen findByLine_idAndStation_idAndPc_idAndScreen_id(int a,int b,int c,int d);
}
