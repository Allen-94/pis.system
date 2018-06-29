function delete_user(userId){
	$.post("/security/userdelete",{userId:userId},function(data){
		alert(data);
	})
}
window.onload = function(){
	$('#html').jstree();
}
function mySubwayMap(obj,url,param){
	$(".subway-map").subwayMap({ debug: true });
	$(obj).subwayMap()
}

function createSubwayMapByJSON(obj,jsonObj,option){
	$(obj).subwayMap()
}


