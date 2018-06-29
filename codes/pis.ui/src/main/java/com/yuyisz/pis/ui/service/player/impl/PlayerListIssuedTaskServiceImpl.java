package com.yuyisz.pis.ui.service.player.impl;

import static org.assertj.core.api.Assertions.entry;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.ui.dao.player.PlayerListIssuedTaskReposutory;
import com.yuyisz.pis.ui.module.player.ContentPackage;
import com.yuyisz.pis.ui.module.player.PlayerList;
import com.yuyisz.pis.ui.module.player.PlayerListIssuedTask;
import com.yuyisz.pis.ui.module.player.PlayerRole;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;
import com.yuyisz.pis.ui.service.player.PlayerListIssuedTaskService;
import com.yuyisz.pis.ui.vo.ContentPackageVO;
@Service
public class PlayerListIssuedTaskServiceImpl implements PlayerListIssuedTaskService {

	@Autowired
	private PlayerListIssuedTaskReposutory playerListIssuedTaskReposutory;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Value("${upload.path}")
	private String uploadFilePath;
	
	private DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
	/**
	 * 根据播表和下发时间，创建下发任务
	 */
	@Override
	public List<PlayerListIssuedTask> createIssuedTasks(PlayerList playerList, Date issuedDate) {
		List<PlayerListIssuedTask> tasks = new ArrayList<>();
		/*//创建播表文件及附属文件，保存到文件系统 
		Set<PlayerRole> playerRoles = playerList.getPlayRoles();
		//播表文件夹		
		String rootDirPath = fileUploadService.getUploadFilePath("playerlists");
		File playerListDir = new File(rootDirPath,String.valueOf(playerList.getId()));
		if(!playerListDir.exists()) {
			playerListDir.mkdir();
		}
		File playerListFile = new File(playerListDir,"playerlist.xml");
		//播表文件
		DocumentBuilderFactory domFactory=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = domFactory.newDocumentBuilder();
			Document document = db.newDocument();
			Element root = document.createElement("PlayerList");
			root.setAttribute("id", playerList.getId()+"");
			root.setAttribute("description", playerList.getDescription());
			root.setAttribute("startdate", df.format(playerList.getStartDate()));
			root.setAttribute("enddate", df.format(playerList.getEndDate()));
			root.setAttribute("name", playerList.getPlayListName());
			root.setAttribute("default", playerList.isDefault()?"true":"false");
			//创建播表下发任务
			for(PlayerRole playerRole : playerRoles){
				//分析每一个规则，解析规则中的播控和每个播控要下载的资源，创建任务，并返回
				Element playerRoleNode = document.createElement("playerRole");
				playerRoleNode.setAttribute("id", String.valueOf(playerRole.getId()));
				playerRoleNode.setAttribute("name", playerRole.getPlayRoleName());
				playerRoleNode.setAttribute("monday", playerRole.isMonday()?"true":"false");
				playerRoleNode.setAttribute("tuesday", playerRole.isThursday()?"true":"false");
				playerRoleNode.setAttribute("wednesday", playerRole.isWednesday()?"true":"false");
				playerRoleNode.setAttribute("thursday", playerRole.isThursday()?"true":"false");
				playerRoleNode.setAttribute("friday", playerRole.isFriday()?"true":"false");
				playerRoleNode.setAttribute("saturday", playerRole.isSaturday()?"true":"false");
				playerRoleNode.setAttribute("sunday", playerRole.isSunday()?"true":"false");
				playerRoleNode.setAttribute("startTime", playerRole.getStartTime());
				playerRoleNode.setAttribute("endTime", playerRole.getEndTime());
				playerRoleNode.setAttribute("contentPackage", playerRole.getContentPackage().getContentName()+".xml");
				playerRoleNode.setAttribute("playerFormat", playerRole.getPlayerFormat().getFileObject().getPath()+playerRole.getPlayerFormat().getFileObject().getRealName());
				playerRoleNode.setAttribute("switchPlayerFormat", playerRole.getSwitchPlayerFormat().getFileObject().getPath()+playerRole.getSwitchPlayerFormat().getFileObject().getRealName());
				playerRoleNode.setAttribute("switchTime", playerRole.getSwitchTime()+"");
				root.appendChild(playerRoleNode);
				ContentPackage cp = playerRole.getContentPackage();
				if(cp != null) {
					File contentPackageFile = new File(playerListDir,cp.getContentName()+".xml");
					String content = cp.getContent();
					ObjectMapper mapper=new ObjectMapper();
					JavaType  javaType  = mapper.getTypeFactory().constructParametricType(ArrayList.class, ContentPackageVO.class);
					@SuppressWarnings("unchecked")
					List<ContentPackageVO> lst =  (List<ContentPackageVO>)mapper.readValue(content, javaType);
					DocumentBuilder cpDb = domFactory.newDocumentBuilder();
					Document cpDom = cpDb.newDocument();
					Element cpRoot = cpDom.createElement("ContentPackage");
					for(ContentPackageVO vo : lst) {
						Element cpEle = cpDom.createElement("Content");
						cpEle.setAttribute("name", vo.getContentName());
						cpEle.setAttribute("main", vo.getContent().get("main"));
						cpEle.setAttribute("backup", vo.getContent().get("backup"));
						cpRoot.appendChild(cpEle);
					}
					cpDom.appendChild(cpRoot);
					TransformerFactory  factory=TransformerFactory.newInstance();
					Transformer transformer=factory.newTransformer();
					//transformer.setOutputProperty();
					transformer.transform(new DOMSource(cpDom),new StreamResult(contentPackageFile));
				}
			}
			document.appendChild(root);
			TransformerFactory  factory=TransformerFactory.newInstance();
			Transformer transformer=factory.newTransformer();
			//transformer.setOutputProperty();
			transformer.transform(new DOMSource(document),new StreamResult(playerListFile));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileObject fb1 = new FileObject(playerListFile.getParent(), playerListFile.getName());
		fileUploadService.saveFileObject(fb1);
		//内容包文件
		File contentPackageFile = new File(playerListDir,"contentPackage.xml");
		FileObject fb2 = new FileObject(contentPackageFile.getParent(), contentPackageFile.getName());
		fileUploadService.saveFileObject(fb2);*/
		
		
		//将播表文件也添加到任务序列中
		return tasks;
	}
	
	/**
	 * 获取播表文件夹中的文件路径，如果不存在则创建。
	 * @param playerlistrootdir：播表文件夹的目录
	 * @param childDir ：文件所在的子目录
	 * @return
	 */
	private File createPlayerListFilePath(PlayerList playerList,String childDir, String fileName) {
		String rootDirPath = fileUploadService.getUploadFilePath("playerlists");
		File dir = new File(rootDirPath+File.separator+childDir,String.valueOf(playerList.getId()));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir,fileName);
		return file;
	}
	
	/**
	 * 创建下发描述文件
	 * @param playerList
	 * @return
	 */
	private String createAndSaveManifest(PlayerList playerList) {
		//获取播表涉及到的播控，每个播控分别查询各自的各种资源情况，
		//保存这些数据到数据库，并写入文件。
		
		Set<PlayerRole> roles = playerList.getPlayRoles();
		//按照播控分组播表规则
		Map<DevResources,List<PlayerRole>> mapPlayerRole = new HashMap<DevResources, List<PlayerRole>>();
		//解析规则，按照播控分组
		if(!roles.isEmpty()) {
			List<DevResources> players = new ArrayList<>();
			for(PlayerRole role:roles) {
				for(DevResources dr : role.getPlayers()) {
					if(!players.contains(dr)) {
						players.add(dr);
					}
				}
			}
			
			for(DevResources dr:players) {
				List<PlayerRole> pl = new ArrayList<>();
				for(PlayerRole role:roles) {
					for(DevResources dv : role.getPlayers()) {
						if(dv.getId() == dr.getId()) {
							pl.add(role);
						}
					}
				}
				mapPlayerRole.put(dr, pl);
			}
		}
		
		DocumentBuilderFactory domFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			File manifestFile = createPlayerListFilePath(playerList,"","manifest.xml");
			
			db = domFactory.newDocumentBuilder();
			Document document = db.newDocument();
			Element playersNode = document.createElement("players");
			for(Entry<DevResources, List<PlayerRole>> entry:mapPlayerRole.entrySet()) {
				DevResources player = entry.getKey();
				List<PlayerRole> roleList = entry.getValue();
				Element playerNode = document.createElement("player");
				playerNode.setAttribute("id", player.getId()+"");
				playerNode.setAttribute("name", player.getResourceName());
				Element cpsNode = document.createElement("contentpackages");
				Element fmtsNode = document.createElement("formats");
				Element resourcesNode = document.createElement("resources");
				for(PlayerRole r : roleList) {
					//解析规则，填充子节点
					//内容包
					ContentPackage cp = r.getContentPackage();
					Element cpNode = document.createElement("contentpackage");
					cpNode.setAttribute("id", cp.getId()+"");  //此处需要修改，将内容包保存到文件中，获取文件的id
					cpNode.setAttribute("path", "内容包路径，还没实现");
					cpsNode.appendChild(cpNode);
					
					//版式
					Element fmtNode = document.createElement("format");
					fmtsNode.appendChild(fmtNode);
					
					//资源，从内容包中获取资源，这里设计有问题，系统中引用资源，都应当是资源类型的对象
					 
					
					//创建下发任务列表
					
					
				}
				playerNode.appendChild(cpsNode);
				playerNode.appendChild(fmtsNode);
				playerNode.appendChild(resourcesNode);
				playersNode.appendChild(playerNode);
			}
			TransformerFactory  factory=TransformerFactory.newInstance();
			Transformer transformer=factory.newTransformer();
			//transformer.setOutputProperty();
			transformer.transform(new DOMSource(document),new StreamResult(manifestFile));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return uploadFilePath;
	}
	

	/**
	 * 保存下发任务到数据库
	 */
	@Override
	public void saveTasks(List<PlayerListIssuedTask> tasks) {
		playerListIssuedTaskReposutory.save(tasks);
	}

	/**
	 * 任务下发实现
	 */
	@Override
	public String IssuedTask(List<PlayerListIssuedTask> tasks) {
		return null;
	}
	
	/**
	 * 根据播控和播表id，查找下发任务
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public 	Map findIssuedTaskGroupByPlayer(long playerListId,List<Integer> playerIds) {
		return null;
	}

	/**
	 * 获取IssuedTask
	 */
	@Override
	public List<PlayerListIssuedTask> findIssuedTaskByPlayer(PlayerList playerList) {
		return playerListIssuedTaskReposutory.findByPlayerListOrderByResources(playerList);
	}

	/**
	 * 向指定的播控发送播表下发消息。
	 */
	@Override
	public void IssuedTask(DevResources player, long playerListId, Date issuedDate) {
		// TODO Auto-generated method stub
		System.out.println("下发播表，接收播控:"+player.getResourceName());
	} 
	
}
