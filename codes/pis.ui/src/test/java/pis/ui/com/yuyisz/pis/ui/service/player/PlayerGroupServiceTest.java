package pis.ui.com.yuyisz.pis.ui.service.player;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.yuyisz.pis.ui.module.player.PlayerGroup;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.player.PlayerGroupService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.security.UserService;

import junit.framework.TestCase;
import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class PlayerGroupServiceTest extends TestBase {

	@Autowired
	private PlayerGroupService service;
	@Autowired
	private DevResourceService resourceService;
	@Autowired
	private UserService userService;
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	@Rollback(true)
	public void testFindAll() {
		List<PlayerGroup> results = service.findAll();
		TestCase.assertTrue(results.isEmpty());
	}
	@Test
	public void testSavePlayGroup() {
		List<DevResources> players = resourceService.findByType(6);
		PlayerGroup group = new PlayerGroup();
		group.setGroupName("测试分组");
		group.setDescription("测试");
		group.setCreateDate(new Date());
		group.setCreater(userService.findUserById(2));
		Set<DevResources> playser = new HashSet<>();
		playser.addAll(players.subList(0, 2));
		group.setPlayers(playser);
		group = service.savePlayerGroup(group);
		TestCase.assertNotNull(group.getId());
	}

}
