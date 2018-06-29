package pis.ui.com.yuyisz.pis.ui.service.infosources;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.dao.infosource.MediaSourceRepository;
import com.yuyisz.pis.ui.dao.security.UserRepository;
import com.yuyisz.pis.ui.module.infosources.MediaSource;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.infosources.MediaSourceService;

import junit.framework.TestCase;
import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class MediaSourceServiceTest extends TestBase {

	@Autowired
	private MediaSourceService mediaSourceService;
	@Autowired
	private MediaSourceRepository repository;
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testSave() {
		User u = userRepository.findOne(2);
		MediaSource source = new MediaSource();
		source.setId(UUID.randomUUID().toString());
		source.setMediaType(0);
		source.setParentSource(null);
		source.setReferenced(0);
		source.setCreateDate(new Date());
		source.setCreater(u);
		source.setDescribe("测试");
		source.setEffectiveDate(new Date());
		source.setManager(u);
		source.setMediaName("测试文件夹");
		source = repository.saveAndFlush(source);
		TestCase.assertNotNull(source);
	}
	
	@Test
	public void testFindALL() {
		List<MediaSource> results = mediaSourceService.findAllMediaSource();
		TestCase.assertEquals(results.size(), 1);
	}

}
