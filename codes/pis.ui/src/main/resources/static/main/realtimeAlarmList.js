var $path_base = "/";
//设置条件
var condition={}
//确认
function confirmAlarm(parm){
	  $.post("/alarm/confirmAlarm",
			  {"alarmId":parm,"userId":"1","isConfirm":true},function(data){
				  if(data == 'true'){
	  					$("#grid-table").jqGrid('setGridParam',{
	  	  				  	datatype: "json",
	  	  				  	postData:condition,
	  	  				  	mtype: 'POST'
	  	  			  }).trigger("reloadGrid");
	  				}else{
	  					alert("确认时发生错误。。。")
	  				}
			  })
};
//取消确认
function redoConfirmAlarm(parm){
	  $.post("/alarm/confirmAlarm",
			  {"alarmId":parm,"userId":"1","isConfirm":false},function(data){
				  if(data == 'true'){
	  					$("#grid-table").jqGrid('setGridParam',{
	  	  				  	datatype: "json",
	  	  				  	postData:condition,
	  	  				  	mtype: 'POST'
	  	  			  }).trigger("reloadGrid");
	  				}else{
	  					alert("确认时发生错误。。。")
	  				}
			  })
};
//清除
function cleanAlarm(parm){
	  $.post("/alarm/cleanAlarm",
			  {"alarmId":parm,"userId":"1","isClean":true},function(data){
				  if(data == 'true'){
	  					$("#grid-table").jqGrid('setGridParam',{
	  	  				  	datatype: "json",
	  	  				  	postData:condition,
	  	  				  	mtype: 'POST'
	  	  			  }).trigger("reloadGrid");
	  				}else{
	  					alert("取消清除时发生错误。。。")
	  				}
			  })
};
//跳转
function direct(parm){
	 alert("跳转")
};
//备注
function remarkAlarm(parm){
	
	var id=parm;
	var rowData = $("#grid-table").jqGrid('getRowData',id);
	var alarmId = rowData.alarmId;
	var remark = rowData.remark;
 	$("#alarmId").val(alarmId);
	$("#remarkfiled").val(remark);
	$( "#dialog-form" ).dialog( "open" );
};

jQuery(function($) {
  var grid_selector = "#grid-table";
  var pager_selector = "#grid-pager";
  $(".chosen-select").chosen(); 
  
  {
	  //初始化条件
	  var gradeform = $("#gradeform").val();
	  var confirmstatusform = $("#confirmstatusform").val();
	  var cleanstatusform = $("#cleanstatusform").val();
	  var receiptTime = $("#receiptTime").val();
	  condition.grade=gradeform;
	  condition.confirmStatus=confirmstatusform;
	  condition.cleanStatus=cleanstatusform;
	  condition.receiptTime=receiptTime;
  }
  
  $("#conditionbtn").click(function(){
	  var gradeform = $("#gradeform").val();
	  var confirmstatusform = $("#confirmstatusform").val();
	  var cleanstatusform = $("#cleanstatusform").val();
	  var receiptTime = $("#receiptTime").val();
	  condition.grade=gradeform;
	  condition.confirmStatus=confirmstatusform;
	  condition.cleanStatus=cleanstatusform;
	  condition.receiptTime=receiptTime;
  })
$('#form_field_select_5_chosen').css("width","150px")
$('#form_field_select_6_chosen').css("width","150px")

  jQuery(grid_selector).jqGrid({
    //direction: "rtl",
   jsonReader : { 
	      root: "content", 
	      total: "totalPages", 
	      records: "totalElements", 
	      repeatitems: false
	   },
    url: "/alarm/findRealtimeAlarms",
    datatype: "json",
    postData:condition,
    mtype: 'POST',
    colNames:['alarmId','级别','名称','告警源类型', '告警源', '具体定位','告警时间','确认状态','清除状态','确认人','清除人','操作','备注'],
    colModel:[
      {name:'alarmId', index:'alarmId', width:60, editable:false, hidden:true},
      {name:'grade',index:'grade',formatter:imgFormat},
      {name:'alarmName',index:'alarmName'},
      {name:'sourceType',index:'sourceType'},
      {name:'source',index:'source'},
      {name:'location',index:'location'},
      {name:'receiptTime',index:'receiptTime'},
      {name:'confirmStatus',index:'confirmStatus'},
      {name:'cleanStatus',index:'cleanStatus'},
      {name:'confirmUser.account',index:'confirmUser.account'},
      {name:'cleanUser.account',index:'cleanUser.account'},
      {name:'myac',index:'',editable:false,
    	    formatter: function (value, grid, rows, state) {
    	        return "<a onclick=\"direct("+rows.alarmId+")\">跳转   </a>"
    	        		+"<a onclick=\"confirmAlarm("+rows.alarmId+")\">确认  </a>"
    	        		+"<a onclick=\"cleanAlarm("+rows.alarmId+")\">清除  </a>"
    	        		+"<a onclick=\"redoConfirmAlarm("+rows.alarmId+")\">取消确认  </a>"
    	        		+"<a onclick=\"remarkAlarm("+grid.rowId+")\">备注  </a>";
    	    }
        },
      {name:'remark',index:'remark'}
      ],
    autowidth:true,
    height:"100%",
    viewrecords : true,
    rowNum:5,
    rowList:[5,10,20,30],
    pager : pager_selector,
    altRows: true,
    rownumbers: true,
    multiselect: true,
    //multikey: "ctrlKey",
    multiboxonly: true,

    loadComplete : function() {
      var table = this;
      setTimeout(function(){
        styleCheckbox(table);
        updateActionIcons(table);
        updatePagerIcons(table);
        enableTooltips(table);
      }, 0);
    },

    editurl: $path_base+"/dummy.html",//nothing is saved
    caption: "实时告警列表"
  });

  function imgFormat(value, grid, rows, state){
	  var basePath = '/images/' 
	  var img,text;
	  switch(value){
	  case 'URGENT':
		  img = "urgent.png";
		  text="紧急"
		  break;
	  case 'IMPORTANT':
		  img = "important.png";
		  text="重要"
		  break;
	  case 'GENERAL':
		  img = "general.png";
		  text="一般"
		  break;
	  case 'HINT':
		  img = "hint.png";
		  text="提示"
		  break;
	  default:
		  img = "hint.png"
		  text="提示"
	  }
	  return "<img alt='"+text+"' src='"+basePath+img+"' /><span>"+text+"</span>"
  };
  //enable search/filter toolbar
  //jQuery(grid_selector).jqGrid('filterToolbar',{defaultSearch:true,stringResult:true})

  //switch element when editing inline
  function aceSwitch( cellvalue, options, cell ) {
    setTimeout(function(){
      $(cell) .find('input[type=checkbox]')
          .wrap('<label class="inline" />')
        .addClass('ace ace-switch ace-switch-5')
        .after('<span class="lbl"></span>');
    }, 0);
  }
  //enable datepicker
  function pickDate( cellvalue, options, cell ) {
    setTimeout(function(){
      $(cell) .find('input[type=text]')
          .datepicker({format:'yyyy-mm-dd' , autoclose:true});
    }, 0);
  }


/*  //navButtons
  jQuery(grid_selector).jqGrid('navGrid',pager_selector,
    { 	//navbar options
      edit: false,
      editicon : 'icon-pencil blue',
      add: false,
      addicon : 'icon-plus-sign purple',
      del: false,
      delicon : 'icon-trash red',
      search: false,
      searchicon : 'icon-search orange',
      refresh: true,
      refreshicon : 'icon-refresh green',
      view: true,
      viewicon : 'icon-zoom-in grey',
    },
    {
      //view record form
      recreateForm: true,
      beforeShowForm: function(e){
        var form = $(e[0]);
        form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
      }
    }
  )*/



  function style_edit_form(form) {
    //enable datepicker on "sdate" field and switches for "stock" field
    form.find('input[name=sdate]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
      .end().find('input[name=stock]')
          .addClass('ace ace-switch ace-switch-5').wrap('<label class="inline" />').after('<span class="lbl"></span>');

    //update buttons classes
    var buttons = form.next().find('.EditButton .fm-button');
    buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
    buttons.eq(0).addClass('btn-primary').prepend('<i class="icon-ok"></i>');
    buttons.eq(1).prepend('<i class="icon-remove"></i>')

    buttons = form.next().find('.navButton a');
    buttons.find('.ui-icon').remove();
    buttons.eq(0).append('<i class="icon-chevron-left"></i>');
    buttons.eq(1).append('<i class="icon-chevron-right"></i>');
  }

  function style_delete_form(form) {
    var buttons = form.next().find('.EditButton .fm-button');
    buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
    buttons.eq(0).addClass('btn-danger').prepend('<i class="icon-trash"></i>');
    buttons.eq(1).prepend('<i class="icon-remove"></i>')
  }

  function style_search_filters(form) {
    form.find('.delete-rule').val('X');
    form.find('.add-rule').addClass('btn btn-xs btn-primary');
    form.find('.add-group').addClass('btn btn-xs btn-success');
    form.find('.delete-group').addClass('btn btn-xs btn-danger');
  }
  function style_search_form(form) {
    var dialog = form.closest('.ui-jqdialog');
    var buttons = dialog.find('.EditTable')
    buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'icon-retweet');
    buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'icon-comment-alt');
    buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'icon-search');
  }

  function beforeDeleteCallback(e) {
    var form = $(e[0]);
    if(form.data('styled')) return false;

    form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
    style_delete_form(form);

    form.data('styled', true);
  }

  function beforeEditCallback(e) {
    var form = $(e[0]);
    form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
    style_edit_form(form);
  }



  //it causes some flicker when reloading or navigating grid
  //it may be possible to have some custom formatter to do this as the grid is being created to prevent this
  //or go back to default browser checkbox styles for the grid
  function styleCheckbox(table) {
  /**
    $(table).find('input:checkbox').addClass('ace')
    .wrap('<label />')
    .after('<span class="lbl align-top" />')


    $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
    .find('input.cbox[type=checkbox]').addClass('ace')
    .wrap('<label />').after('<span class="lbl align-top" />');
  */
  }


  //unlike navButtons icons, action icons in rows seem to be hard-coded
  //you can change them like this in here if you want
  function updateActionIcons(table) {
    /**
    var replacement =
    {
      'ui-icon-pencil' : 'icon-pencil blue',
      'ui-icon-trash' : 'icon-trash red',
      'ui-icon-disk' : 'icon-ok green',
      'ui-icon-cancel' : 'icon-remove red'
    };
    $(table).find('.ui-pg-div span.ui-icon').each(function(){
      var icon = $(this);
      var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
      if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
    })
    */
  }

  //replace icons with FontAwesome icons like above
  function updatePagerIcons(table) {
    var replacement =
    {
      'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
      'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
      'ui-icon-seek-next' : 'icon-angle-right bigger-140',
      'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
      var icon = $(this);
      var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

      if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
    })
  }

  function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
  }

  //var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
  //批量确定
  $("#confirms").click(function(){
	  var alarmIds = '';
	  var ids=$(grid_selector).jqGrid('getGridParam','selarrrow');
	  $.each(ids,function(k,v){
		  var rowData = $(grid_selector).jqGrid('getRowData',v);
		  var alarmId = rowData.alarmId;
		  alarmIds+=alarmId
		  alarmIds+=','
	  })
	  alarmIds=alarmIds.substring(0,alarmIds.length-1)
  	  $.post("/alarm/confirmAlarm",
  			{"alarmId":alarmIds,"isConfirm":true},function(data){
  				if(data){
  					$(grid_selector).jqGrid('setGridParam',{
  	  				  	datatype: "json",
  	  				  	mtype: 'POST'
  	  			  }).trigger("reloadGrid");
  				}else{
  					alert("确认时发生错误。。。")
  				}	
		  })
  })
  $("#redoConfirms").click(function(){
	  var alarmIds = '';
	  var ids=$(grid_selector).jqGrid('getGridParam','selarrrow');
	  $.each(ids,function(k,v){
		  var rowData = $(grid_selector).jqGrid('getRowData',v);
		  var alarmId = rowData.alarmId;
		  alarmIds+=alarmId
		  alarmIds+=','
	  })
	  alarmIds=alarmIds.substring(0,alarmIds.length-1)
  	  $.post("/alarm/confirmAlarm",
  			{"alarmId":alarmIds,"isConfirm":false},function(data){
  				if(data == 'true'){
  					$(grid_selector).jqGrid('setGridParam',{
  	  				  	datatype: "json",
  	  				  	mtype: 'POST'
  	  			  }).trigger("reloadGrid");
  				}else{
  					alert("取消确认时发生错误。。。")
  				}	
		  })
  })
    $("#cleans").click(function(){
    	var alarmIds = '';
  	  var ids=$(grid_selector).jqGrid('getGridParam','selarrrow');
  	  $.each(ids,function(k,v){
  		  var rowData = $(grid_selector).jqGrid('getRowData',v);
  		  var alarmId = rowData.alarmId;
  		  alarmIds+=alarmId
  		  alarmIds+=','
  	  })
  	  alarmIds=alarmIds.substring(0,alarmIds.length-1)
    	  $.post("/alarm/cleanAlarm",
    			{"alarmId":alarmIds,"isClean":true},function(data){
    				if(data == 'true'){
    					$(grid_selector).jqGrid('setGridParam',{
    	  				  	datatype: "json",
    	  				  	mtype: 'POST'
    	  			  }).trigger("reloadGrid");
    				}else{
    					alert("取消确认时发生错误。。。")
    				}	
  		  })
  })
    $("#dump").click(function(){
        var url = "/alarm/download";
        var form = $("<form></form>").attr("action", url).attr("method", "post");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "type").attr("value", "realtime"));
        $.each(condition,function(k,v){
        	form.append($("<input></input>").attr("type", "hidden").attr("name", k).attr("value", v));
        })
        form.appendTo('body').submit().remove();
    	
  })
    $("#scrollLock").click(function(){
	  alert("滚动锁定")
  })
 
  $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 300,
      width: 350,
      modal: true,
      buttons: {
        "确定": function() {
        	var alarmId = $("#alarmId").val();
        	var remark = $("#remarkfiled").val();
        	$.post("/alarm/remarkAlarm",
    			{"alarmId":alarmId,"remark":remark},function(data){
    				if(data==true){
    					$(grid_selector).jqGrid('setGridParam',{
    	  				  	datatype: "json",
    	  				  	mtype: 'POST'
    	  			  }).trigger("reloadGrid");
    				}else{
    					alert("写入备注发生错误。。。")
    				}
    			})
    			$( this ).dialog( "close" );
        },
        Cancel: function() {
          $( this ).dialog( "close" );
        }
      },
      close: function() {
    	  $( this ).dialog( "close" );
      }
    });

});
