jQuery(function($) {
	var initName="";
	var mode="";
	
	$.ajax({
		async:false,
		type : "get",
		url : '/subsystem/InstallMode',
		dataType : "json",
		success : function(data) {
			mode=data;
			if(mode==1){
				$(".model0").hide();
			}
		}
	});
	
	$.ajax({
		type : "get",
		url : '/subsystem/getClusterList',
		dataType : "json",
		success : function(data) {
			var str="";
			var cname=data.data;
			initName=cname[0].clusterName;
			getVm(initName);
			$(cname).each(function(){ 
				str=str+'</option><option value="'+this.clusterName+'">'+this.clusterName+'</option>';
			});
			$("#cname").html(str);
		}
	});
	
	function getVm(name){
		$.ajax({
			type : "get",
			url : '/subsystem/getListByClusterName',
			data : {"clusterName":name},
			dataType : "json",
			success : function(data) {
				var str="";
				var hstr="";
				var i=0;
				var vname=data.data;
				$(vname).each(function(){ 
					str=str+'</option><option value="'+i+'">'+this.vmName+'('+this.vmIp+')</option>';
					hstr=hstr+'<input type="hidden" value="'+this.vmIp+'" class="cn"><input type="hidden" value="'+this.nodeType+'" class="ci">'; 
					i++;
				});
				$("#vname").html(str);
				$("#hdiv").html(hstr);
			}
		});
	}
	
	$("select#cname").change(function(){
		getVm($(this).val());
	});
	
	$("select#vname").change(function(){
		// alert($(this).val());
	});
	
	$("#save").click(function(){

		if ($("#newVmName").val() == "")
		{ 
			$("#newVmName").focus();
			alert("虚拟机名称不能为空");
			return;
		}
		
		if ($("#newVmIp").val() == "")
        { 
        	$("#newVmIp").focus();
        	alert("IP不能为空");
        	return;
        }
		
		if(mode==0){
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
		}
		
		var reg1=/^[0-9a-zA-Z_-]{1,}$/; 
		if (!reg1.test($("#newVmName").val())){
			alert("虚拟机名称格式不正确");
			$("#newVmName").val("");
			$("#newVmName").focus();
			return;
		}
		
		var reg2=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
		if (!reg2.test($("#newVmIp").val())){
			alert("ip地址格式不正确");
			$("#newVmIp").val("");
			$("#newVmIp").focus();
			return;
		}
		
		if(mode==0){
			if (!reg1.test($("#newServerName").val())){
				alert("服务器名称格式不正确");
				$("#newServerName").val("");
				$("#newServerName").focus();
				return;
			}
			
			if (!reg2.test($("#newServerIp").val())){
				alert("ip地址格式不正确");
				$("#newServerIp").val("");
				$("#newServerIp").focus();
				return;
			}
			
			var newServerName=$("#newServerName").val();
			var newServerIp=$("#newServerIp").val();
		}else{
			var newServerName="";
			var newServerIp="";
		}
		
		var nvip=$("#newVmIp").val();
		var nvname=$("#newVmName").val();
		var num=$("#vname").val();
		var cname=$("#cname").val();
		
		var vname="";
		var vtype="";
		var i=0;
		$(".cn").each(function(){
			if(num==i)
				vname=$(this).val();
			i++;
		});
		i=0;
		$(".ci").each(function(){
			if(num==i)
				vtype=$(this).val();
			i++;
		});
		
		$.ajax({
			type : "post",
			url : '/subsystem/replaceService',
			data : {
					"clusterName":cname,
					"nodeType":vtype,
					"vmIp":vname,
					"newVmName":nvname,
					"newVmIp":nvip,
					"newServerName":newServerName,
					"newServerIp":newServerIp
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