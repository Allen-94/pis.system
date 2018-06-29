$(function() {
	$("#restart").click(function(){
			$.ajax({
				type : "post",
				url : '/subsystem/restartService',
				dataType : "json",
				success : function(data) {
					alert(data.msg);
					window.location.reload();
				},
			});
		});
	$("#stop").click(function(){
		// var obj = [{ "clusterName": "xxxx"},{"clusterName":"xxxssaas"}];
		// alert(obj);
		// alert(obj[0].clusterName);
		// alert(JSON.stringify(obj));
		$.ajax({
			type : "post",
			url : '/subsystem/stopService',
		//  type : "get",
		//  url : '/subsystem/test2',
		//  data: {str:JSON.stringify(obj)},
			dataType : "json",
			success : function(data) {
				alert(data.msg);
		//		alert(data);
		//		alert(data[1].clusterName);
				window.location.reload();
			},
		});
	});
	$("#restart-service").click(function(){
		$.ajax({
			type : "get",
			url : '/subsystem/getClusterList',
			dataType : "json",
			success : function(data) {
				var str='<form action="/subsystem/restartsService" method="post"><div class="widget-box"><div';
				str=str+' class="widget-header"><h4>选择集群</h4><span class="widget-toolbar"><a href="#" ';
				str=str+'data-action="collapse"><i class="icon-chevron-up"></i></a><a href="#" data-action="';
				str=str+'close"><i class="icon-remove"></i></a></span></div><div class="widget-body"><div ';
				str=str+'class="widget-main"><div><label for="form-field-select-2">集群列表</label><select ';
				str=str+'class="form-control" id="form-field-select-2" name="clusterName" multiple="multiple">';
				str=str+'<option value="" selected="selected">所有集群</option>';
				var cname=data.data;
				$(cname).each(function(){ 
					str=str+'</option><option value="'+this.clusterName+'">'+this.clusterName+'</option>';
				}); 
				str=str+'</select></div></div><button type="submit" id="restarts" class="btn btn-sm btn-primary" ';
				str=str+'style="margin-left:55%;">重启该集群服务</button></div></div></form>';
				$("#tck").html(str);
			},
		});
	});
	$("#change-service").click(function(){
		$.ajax({
			type : "get",
			url : '/subsystem/getClusterList',
			dataType : "json",
			success : function(data) {
				var str='<form action="/subsystem/changeService" method="post"><div class="widget-box"><div';
				str=str+' class="widget-header"><h4>选择集群</h4><span class="widget-toolbar"><a href="#" ';
				str=str+'data-action="collapse"><i class="icon-chevron-up"></i></a><a href="#" data-action="';
				str=str+'close"><i class="icon-remove"></i></a></span></div><div class="widget-body"><div ';
				str=str+'class="widget-main"><div><label for="form-field-select-2">集群列表</label><select ';
				str=str+'class="form-control" id="form-field-select-2" name="clusterName" multiple="multiple">';
				var cname=data.data;
				var nu=1;
				$(cname).each(function(){ 
					if(nu==1){
						str=str+'</option><option value="'+this.clusterName+'" selected="selected">'+this.clusterName+'</option>';
					}else{
						str=str+'</option><option value="'+this.clusterName+'">'+this.clusterName+'</option>';
					}
					nu++;
				}); 
				str=str+'</select></div></div><button type="submit" id="restarts" class="btn btn-sm btn-primary" ';
				str=str+'style="margin-left:55%;">该集群主备倒换</button></div></div></form>';
				$("#tck").html(str);
			},
		});
	});
	
	$('#datatable_xs').DataTable({
		"order": [[ 0, "desc" ]],
		"searching":false,
		"stateSave":true,
		"bLengthChange":true,
		"ordering":true,
		"colReorder": true,
		"info":false,
		"paging":false,
		"autoWidth": true,
        "ajax": {
            "url": "/subsystem/getServiceList",
        },
        "columns": [
            {"data": "clusterName"},
            {"data": "nodeType"},
            {"data": "vmName"},
            {"data": "vmIp"},
            {"data": "floatIp"},
            {
            	"data": "status",
        		"render":function(data,type,val,meta){
					if(data == null){
						return "运行异常"
					}else if(data == 0){
						return "运行正常"
					}else if(data == 1){
						return "待接管"
					}else if(data == 2){
						return "服务关闭"
					}else{
						return "异常"
					}
				}
            },
            {"data": "serverName"},
            {"data": "serverIp"},
            {"data": "cpuUtilization"},
            {"data": "memUtilization"},
            {"data": "diskUtilization"}
        ]
    });
	
//	$.ajax({
//				type : "get",
//				url : '/subsystem/getServiceList',
//				dataType : "json",
//				success : function(data) {
//					var grid_data = data.data;
//					jQuery(function($) {
//						var grid_selector = "#grid-table";
//						var pager_selector = "#grid-pager";
//
//						jQuery(grid_selector)
//								.jqGrid(
//										{
//											data : grid_data,
//											datatype : "local",
//											height : 'auto',
//											colNames : [
//														'集群名称',
//														'节点类型',
//														'所在虚拟机名称',
//														'虚拟机IP',
//														'浮动IP',
//														'服务状态',
//														'所在服务器名',
//														'所在服务器IP',
//														'CPU占有率',
//														'内存占有率',
//														'硬盘占有率' ],
//											colModel : [{
//												name : 'clusterName',
//												index : 'clusterName',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'nodeType',
//												index : 'nodeType',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'vmName',
//												index : 'vmName',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'vmIp',
//												index : 'vmIp',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'floatIp',
//												index : 'floatIp',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'status',
//												index : 'status',
//												sortable : false,
//												editable : false,
//												edittype : 'select',
//												formatter : 'select',
//												editoptions : {
//													value : "0:运行正常;1:待接管;2:异常;3:服务关闭"
//												}
//											},
//											{
//												name : 'serverName',
//												index : 'serverName',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'serverIp',
//												index : 'serverIp',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'cpuUtilization',
//												index : 'cpuUtilization',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'memUtilization',
//												index : 'memUtilization',
//												sortable : false,
//												editable : false
//											},
//											{
//												name : 'diskUtilization',
//												index : 'diskUtilization',
//												sortable : false,
//												editable : false
//											}],
//											grouping : true,// 是否分组,默认为false
//											groupingView : {
//											groupField : [ 'clusterName' ], // 按照哪一列进行分组
//											groupColumnShow : [ true ],// 是否显示分组列
//											groupText : [ '<b>{0} - 服务集群</b>' ],// 表头显示的数据以及相应的数据量
//											groupCollapse : false,// 加载数据时是否只显示分组的组信息
//											groupDataSorted : true,// 分组中的数据是否排序
//											groupOrder : [ 'asc' ],// 分组后的排序
//											groupSummary : [ true ],// 是否显示汇总.如果为true需要在colModel中进行配置
//											summaryType : 'max',// 运算类型，可以为max,sum,avg</div>
//											summaryTpl : '<b>Max: {0}</b>',
//											showSummaryOnHide : true//是否在分组底部显示汇总信息并且当收起表格时是否隐藏下面的分组
//											},
//
//											viewrecords : true,
//											altRows : true,
//											// toppager: true,
//
//											multiselect : false,
//
//											loadComplete : function() {
//												var table = this;
//												setTimeout(function() {
//													styleCheckbox(table);
//													updateActionIcons(table);
//													updatePagerIcons(table);
//													enableTooltips(table);
//												}, 0);
//											},
//
//											caption : "系统服务运行状态",
//
//											autowidth : true,
//										});
//
//						function editLink(cellValue, options, rowdata, action) {
//							return "<a href='https://" + rowdata.hostIp
//									+ "' target='_blank'>" + cellValue + "</a>";
//						}
//
//						function aceSwitch(cellvalue, options, cell) {
//							setTimeout(function() {
//								$(cell).find('input[type=checkbox]').wrap(
//										'<label class="inline" />').addClass(
//										'ace ace-switch ace-switch-5').after(
//										'<span class="lbl"></span>');
//							}, 0);
//						}
//						function pickDate(cellvalue, options, cell) {
//							setTimeout(function() {
//								$(cell).find('input[type=text]').datepicker({
//									format : 'yyyy-mm-dd',
//									autoclose : true
//								});
//							}, 0);
//						}
//						// navButtons
//						jQuery(grid_selector)
//								.jqGrid(
//										'navGrid',
//										pager_selector,
//										{ // navbar options
//											edit : false,
//											editicon : 'icon-pencil blue',
//											add : false,
//											addicon : 'icon-plus-sign purple',
//											del : false,
//											delicon : 'icon-trash red',
//											search : false,
//											searchicon : 'icon-search orange',
//											refresh : true,
//											refreshicon : 'icon-refresh green',
//											view : false,
//											viewicon : 'icon-zoom-in grey'
//										},
//										{
//											// edit record form
//											// closeAfterEdit: true,
//											recreateForm : true,
//											beforeShowForm : function(e) {
//												var form = $(e[0]);
//												form
//														.closest('.ui-jqdialog')
//														.find(
//																'.ui-jqdialog-titlebar')
//														.wrapInner(
//																'<div class="widget-header" />')
//												style_edit_form(form);
//											}
//										},
//										{
//											// new record form
//											closeAfterAdd : true,
//											recreateForm : true,
//											viewPagerButtons : false,
//											beforeShowForm : function(e) {
//												var form = $(e[0]);
//												form
//														.closest('.ui-jqdialog')
//														.find(
//																'.ui-jqdialog-titlebar')
//														.wrapInner(
//																'<div class="widget-header" />')
//												style_edit_form(form);
//											}
//										},
//										{
//											// delete record form
//											recreateForm : true,
//											beforeShowForm : function(e) {
//												var form = $(e[0]);
//												if (form.data('styled'))
//													return false;
//
//												form
//														.closest('.ui-jqdialog')
//														.find(
//																'.ui-jqdialog-titlebar')
//														.wrapInner(
//																'<div class="widget-header" />')
//												style_delete_form(form);
//
//												form.data('styled', true);
//											},
//											onClick : function(e) {
//												alert(1);
//											}
//										},
//										{
//											// search form
//											recreateForm : true,
//											afterShowSearch : function(e) {
//												var form = $(e[0]);
//												form
//														.closest('.ui-jqdialog')
//														.find(
//																'.ui-jqdialog-title')
//														.wrap(
//																'<div class="widget-header" />')
//												style_search_form(form);
//											},
//											afterRedraw : function() {
//												style_search_filters($(this));
//											},
//											multipleSearch : true,
//										/**
//										 * multipleGroup:true, showQuery: true
//										 */
//										},
//										{
//											// view record form
//											recreateForm : true,
//											beforeShowForm : function(e) {
//												var form = $(e[0]);
//												form
//														.closest('.ui-jqdialog')
//														.find(
//																'.ui-jqdialog-title')
//														.wrap(
//																'<div class="widget-header" />')
//											}
//										})
//
//						function style_edit_form(form) {
//							// enable datepicker on "sdate" field and switches
//							// for "stock" field
//							form.find('input[name=sdate]').datepicker({
//								format : 'yyyy-mm-dd',
//								autoclose : true
//							}).end().find('input[name=stock]').addClass(
//									'ace ace-switch ace-switch-5').wrap(
//									'<label class="inline" />').after(
//									'<span class="lbl"></span>');
//
//							// update buttons classes
//							var buttons = form.next().find(
//									'.EditButton .fm-button');
//							buttons.addClass('btn btn-sm').find(
//									'[class*="-icon"]').remove();// ui-icon,
//							// s-icon
//							buttons.eq(0).addClass('btn-primary').prepend(
//									'<i class="icon-ok"></i>');
//							buttons.eq(1)
//									.prepend('<i class="icon-remove"></i>')
//
//							buttons = form.next().find('.navButton a');
//							buttons.find('.ui-icon').remove();
//							buttons.eq(0).append(
//									'<i class="icon-chevron-left"></i>');
//							buttons.eq(1).append(
//									'<i class="icon-chevron-right"></i>');
//						}
//
//						function style_delete_form(form) {
//							var buttons = form.next().find(
//									'.EditButton .fm-button');
//							buttons.addClass('btn btn-sm').find(
//									'[class*="-icon"]').remove();// ui-icon,
//							// s-icon
//							buttons.eq(0).addClass('btn-danger').prepend(
//									'<i class="icon-trash"></i>');
//							buttons.eq(1)
//									.prepend('<i class="icon-remove"></i>')
//						}
//
//						function style_search_filters(form) {
//							form.find('.delete-rule').val('X');
//							form.find('.add-rule').addClass(
//									'btn btn-xs btn-primary');
//							form.find('.add-group').addClass(
//									'btn btn-xs btn-success');
//							form.find('.delete-group').addClass(
//									'btn btn-xs btn-danger');
//						}
//						function style_search_form(form) {
//							var dialog = form.closest('.ui-jqdialog');
//							var buttons = dialog.find('.EditTable')
//							buttons.find('.EditButton a[id*="_reset"]')
//									.addClass('btn btn-sm btn-info').find(
//											'.ui-icon').attr('class',
//											'icon-retweet');
//							buttons.find('.EditButton a[id*="_query"]')
//									.addClass('btn btn-sm btn-inverse').find(
//											'.ui-icon').attr('class',
//											'icon-comment-alt');
//							buttons.find('.EditButton a[id*="_search"]')
//									.addClass('btn btn-sm btn-purple').find(
//											'.ui-icon').attr('class',
//											'icon-search');
//						}
//
//						function beforeDeleteCallback(e) {
//							var form = $(e[0]);
//							if (form.data('styled'))
//								return false;
//
//							form.closest('.ui-jqdialog').find(
//									'.ui-jqdialog-titlebar').wrapInner(
//									'<div class="widget-header" />')
//							style_delete_form(form);
//
//							form.data('styled', true);
//						}
//
//						function beforeEditCallback(e) {
//							var form = $(e[0]);
//							form.closest('.ui-jqdialog').find(
//									'.ui-jqdialog-titlebar').wrapInner(
//									'<div class="widget-header" />')
//							style_edit_form(form);
//						}
//
//						// it causes some flicker when reloading or navigating
//						// grid
//						// it may be possible to have some custom formatter to
//						// do this as the grid
//						// is being created to prevent this
//						// or go back to default browser checkbox styles for the
//						// grid
//						function styleCheckbox(table) {
//							/**
//							 * $(table).find('input:checkbox').addClass('ace')
//							 * .wrap('<label />') .after('<span class="lbl
//							 * align-top" />')
//							 * 
//							 * 
//							 * $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
//							 * .find('input.cbox[type=checkbox]').addClass('ace')
//							 * .wrap('<label />').after('<span class="lbl
//							 * align-top" />');
//							 */
//						}
//
//						// unlike navButtons icons, action icons in rows seem to
//						// be hard-coded
//						// you can change them like this in here if you want
//						function updateActionIcons(table) {
//							/**
//							 * var replacement = { 'ui-icon-pencil' :
//							 * 'icon-pencil blue', 'ui-icon-trash' : 'icon-trash
//							 * red', 'ui-icon-disk' : 'icon-ok green',
//							 * 'ui-icon-cancel' : 'icon-remove red' };
//							 * $(table).find('.ui-pg-div
//							 * span.ui-icon').each(function(){ var icon =
//							 * $(this); var $class =
//							 * $.trim(icon.attr('class').replace('ui-icon',
//							 * '')); if($class in replacement)
//							 * icon.attr('class', 'ui-icon
//							 * '+replacement[$class]); })
//							 */
//						}
//
//						// replace icons with FontAwesome icons like above
//						function updatePagerIcons(table) {
//							var replacement = {
//								'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
//								'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
//								'ui-icon-seek-next' : 'icon-angle-right bigger-140',
//								'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
//							};
//							$(
//									'.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon')
//									.each(
//											function() {
//												var icon = $(this);
//												var $class = $.trim(icon.attr(
//														'class').replace(
//														'ui-icon', ''));
//
//												if ($class in replacement)
//													icon
//															.attr(
//																	'class',
//																	'ui-icon '
//																			+ replacement[$class]);
//											})
//						}
//
//						function enableTooltips(table) {
//							$('.navtable .ui-pg-button').tooltip({
//								container : 'body'
//							});
//							$(table).find('.ui-pg-div').tooltip({
//								container : 'body'
//							});
//						}
//
//						// var selr =
//						// jQuery(grid_selector).jqGrid('getGridParam','selrow');
//						$("#dump").click(function() {
//							// jQuery(grid_selector).jqGrid('jqGridExport',{exptype:'jsonstring',});
//							jQuery(grid_selector).jqGridExport({// We are
//																// exporting in
//																// JSON
//								exptype : 'jsonstring',
//								// We inform the grid what should be the name of
//								// property
//								root : 'Settings'
//							})
//						})
//
//					});
//				}
//			});
});
