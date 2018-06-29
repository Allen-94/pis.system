jQuery(function($) {
	$.ajax({
		type : "get",
		url : '/subsystem/InstallMode',
		dataType : "json",
		success : function(data) {
			if(data==1){
				alert("该模式属于业主提供的服务器，不支持更换服务器！");
				window.location.href = "/subsystem/serviceStatusList";
			}
		}
	});
	
	var initName="";
	$.ajax({
		type : "get",
		url : '/subsystem/getServerList',
		dataType : "json",
		success : function(data) {
			var str="";
			var cname=data.data;
			initName=cname[0].serverName;
			getVm(initName);
			$(cname).each(function(){ 
				str=str+'</option><option value="'+this.serverName+'">'+this.serverName+'</option>';
			});
			$("#cname").html(str);
		}
	});

	function getVm(name){
		$.ajax({
			type : "get",
			url : '/subsystem/getListByServerName',
			data : {"serverName":name},
			dataType : "json",
			success : function(data) {
				var str="";
				var hstr="";
				var vname=data.data;
				$(vname).each(function(){ 
					str=str+'<div class="form-group" style="width:100%"><label class="col-sm-3 control-label no-padding-right" ';
					str=str+'for="form-field-1" style="width:66%">旧节点:'+this.vmName+'/'+this.vmIp+'&nbsp;&nbsp;新节点:</label>';
					str=str+'<div class="col-xs-12 col-sm-9" style="width:34%"><div class="clearfix"><input class="col-xs-12 col-sm-5 server-info" placeholder="格式:主机名,IP地址" style="width:215px" /></div></div></div>';
					hstr=hstr+'<input type="hidden" value="'+this.clusterName+'" class="clustername"><input type="hidden" value="'+this.vmIp+'" class="vmip"><input type="hidden" value="'+this.nodeType+'" class="nodetype">'; 
				});
				$("#allinfo").html(str);
				$("#hdiv").html(hstr);
			}
		});
	}
	
	$("select#cname").change(function(){
		getVm($(this).val());
	});
	
	$("select#vname").change(function(){
		//alert($(this).val());
	});
	
	$("#save").click(function(){
		
		if ($("#newServerName").val() == "")
		{ 
			$("#newServerName").focus();
			alert("服务器名称不能为空");
			return;
		}
		
		if ($("#newServerIp").val() == "")
        { 
        	$("#newServerIp").focus();
        	alert("IP不能为空");
        	return;
        }
		
		var flag=false;
		$(".server-info").each(function(){
			if($(this).val() == ""){
				$(this).focus();
				alert("新虚拟机节点信息不能为空");
				flag=true;
				return false;
			}
		});
		
		if(flag){
			return;
		}
		
		var reg=/^[0-9a-zA-Z_-]{1,}$/; 
		if (!reg.test($("#newServerName").val())){
			alert("服务器名称格式不正确");
			$("#newServerName").val("");
			$("#newServerName").focus();
			return;
		}
		
		reg=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
		if (!reg.test($("#newServerIp").val())){
			alert("ip地址格式不正确");
			$("#newServerIp").val("");
			$("#newServerIp").focus();
			return;
		}
		
		reg=/^[0-9a-zA-Z_-]{1,},(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
		flag=false;
		$(".server-info").each(function(){
			if (!reg.test($(this).val())){
				alert("格式有误！格式:主机名(只为字母、数子、下划线及减号),(以逗号分隔)IP地址(例如:192.168.1.1)");
				$(this).val("");
				$(this).focus();
				flag=true;
				return false;
			}
		});
		

		if(flag){
			return;
		}
		var len=$(".server-info").length;
		var addCmd='echo -n "">/opt/add_service.txt;';
		var delCmd='echo -n "">/opt/del_service.txt;';
		var ServerInfo=$("#newServerName").val()+','+$("#newServerIp").val();
		for(var i=0;i<len;i++){
			addCmd=addCmd+'echo "'+$(".clustername")[i].value+','+$(".nodetype")[i].value+','+$(".server-info")[i].value+','+ServerInfo+'">>/opt/add_service.txt;';
			delCmd=delCmd+'echo "'+$(".clustername")[i].value+','+$(".nodetype")[i].value+','+$(".vmip")[i].value+'">>/opt/del_service.txt;';
		}
		addCmd=addCmd+'echo $?';
		delCmd=delCmd+'echo $?';
		$.ajax({
			type : "post",
			url : '/subsystem/replaceServerService',
			data : {
					"addCmd":addCmd,
					"delCmd":delCmd
				},
			dataType : "json",
			success : function(data) {
				alert(data.msg);
				if(data.code==200){
					window.location.href="/subsystem/serviceStatusList";
				}
			}
		});
	});
	
	$("#reset").click(function(){
		getVm(initName);
	});

});