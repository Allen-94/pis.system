package com.yuyisz.pis.ui.service.alarm;

import java.util.Map;

import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.vo.AlarmForwardConfigVO;

/**
 * 告警转发配置服务
 * @author mengz
 *
 */
public interface AlarmForwardConfigService {

	/**
	 * 告警转发，每个告警转发配置自行决定转发策略
	 * @param alarm  :收到的告警
	 * @param config :转发配置
	 */
	public void forwardAlarm(AlarmForwardConfig config ,Alarm alarm);

	public Map<User, String> forwardAlarm2(AlarmForwardConfigVO vo, String string);
}
