package com.yuyisz.pis.ui.service.player.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.ui.dao.player.PlayRoleRepository;
import com.yuyisz.pis.ui.module.player.ContentObject;
import com.yuyisz.pis.ui.module.player.ContentPackage;
import com.yuyisz.pis.ui.module.player.PlayerRole;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;
import com.yuyisz.pis.ui.service.player.PlayerRoleService;
import com.yuyisz.pis.ui.vo.ContentPackageVO;
@Service
public class PlayerRoleServiceImpl implements PlayerRoleService {

	@Autowired
	private PlayRoleRepository playRoleRepositoy;
	
	private DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Override
	public PlayerRole findById(long playerRoleId) {
		return playRoleRepositoy.findOne(playerRoleId);
	}

	
	@Override
	public PlayerRole saveOrUpdatePlayerRole(PlayerRole role) {
		
		return playRoleRepositoy.saveAndFlush(role);
	}

	@Override
	public List<PlayerRole> findRoleByPlayerListId(long playerListId) {
		return playRoleRepositoy.findByPlayListId(playerListId);
	}

	@Override
	public void delById(long playerRoleId) {
		playRoleRepositoy.delete(playerRoleId);
	}

	/**
	 * 根据播控和规则，创建播表相关文件，放在相应的文件夹
	 */
	@Override
	public FileObject makePlayListFile(String playerDir,DevResources player, PlayerRole re) {
		String playListDirPath = playerDir + File.separator+re.getPlayList().getId();
		File playListDir = new File(playListDirPath);
		if(!playListDir.exists()||!playListDir.isDirectory()) {
			playListDir.mkdirs();
		}
		File roleFile = new File(playListDirPath,re.getPlayRoleName()+".xml");
		DocumentBuilderFactory domFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = domFactory.newDocumentBuilder();
			Document document = db.newDocument();
			Element root = document.createElement("PlayerList");
			root.setAttribute("id", re.getPlayList().getId()+"");
			root.setAttribute("description", re.getPlayList().getDescription());
			root.setAttribute("startdate", df.format(re.getPlayList().getStartDate()));
			root.setAttribute("enddate", df.format(re.getPlayList().getEndDate()));
			root.setAttribute("name", re.getPlayList().getPlayListName());
			root.setAttribute("default", re.getPlayList().isDefault()?"true":"false");
			
			Element playerRoleNode = document.createElement("playerRole");
			playerRoleNode.setAttribute("id", String.valueOf(re.getId()));
			playerRoleNode.setAttribute("name", re.getPlayRoleName());
			playerRoleNode.setAttribute("monday", re.isMonday()?"true":"false");
			playerRoleNode.setAttribute("tuesday", re.isThursday()?"true":"false");
			playerRoleNode.setAttribute("wednesday", re.isWednesday()?"true":"false");
			playerRoleNode.setAttribute("thursday", re.isThursday()?"true":"false");
			playerRoleNode.setAttribute("friday", re.isFriday()?"true":"false");
			playerRoleNode.setAttribute("saturday", re.isSaturday()?"true":"false");
			playerRoleNode.setAttribute("sunday", re.isSunday()?"true":"false");
			playerRoleNode.setAttribute("startTime", re.getStartTime());
			playerRoleNode.setAttribute("endTime", re.getEndTime());
			playerRoleNode.setAttribute("playerFormat", re.getPlayerFormat().getFileObject().getPath()+re.getPlayerFormat().getFileObject().getRealName());
			playerRoleNode.setAttribute("switchPlayerFormat", re.getSwitchPlayerFormat().getFileObject().getPath()+re.getSwitchPlayerFormat().getFileObject().getRealName());
			playerRoleNode.setAttribute("switchTime", re.getSwitchTime()+"");
			root.appendChild(playerRoleNode);
			ContentPackage cp = re.getContentPackage();
			if(cp != null) {
				File contentPackageFile = new File(playListDirPath,cp.getContentName()+".xml");
				List<ContentObject> contents = cp.getContents() ;
				DocumentBuilder cpDb = domFactory.newDocumentBuilder();
				Document cpDom = cpDb.newDocument();
				Element cpRoot = cpDom.createElement("ContentPackage");
				for(ContentObject vo : contents) {
					Element cpEle = cpDom.createElement("Content");
					cpEle.setAttribute("name", vo.getContentName());
					cpEle.setAttribute("main", vo.getMainContent().getValue());
					cpEle.setAttribute("backup", vo.getBackupContent().getValue());
					cpRoot.appendChild(cpEle);
				}
				cpDom.appendChild(cpRoot);
				TransformerFactory  factory=TransformerFactory.newInstance();
				Transformer transformer=factory.newTransformer();
				transformer.transform(new DOMSource(cpDom),new StreamResult(contentPackageFile));
				FileObject fb = new FileObject(contentPackageFile.getAbsolutePath(), contentPackageFile.getName());
				fileUploadService.saveFileObject(fb);
				playerRoleNode.setAttribute("contentPackage", fb.getPath());
			}
			
			document.appendChild(root);
			TransformerFactory  factory=TransformerFactory.newInstance();
			Transformer transformer=factory.newTransformer();
			//transformer.setOutputProperty();
			transformer.transform(new DOMSource(document),new StreamResult(roleFile));
			FileObject roleFb = new FileObject(roleFile.getAbsolutePath(), roleFile.getName());
			return roleFb;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
