<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<s:include value="/%{#request.getContextPath()}/script/script.jsp"></s:include>
<script type="text/javascript" src="<%=application.getContextPath()%>/script/business/core-loglist.js"></script>
<title>展现业务日志</title>
</head>
<body>
	<div id="toolsbar">
		<div id="pp" class="easyui-pagination"
			data-options="fit:true,showPageList:false,buttons:$('#tb'),pageSize:30,total:<s:property value="#request.entrypage.allRow" />"></div>
		<div id="tb" style="padding: 2px; height: auto; text-align: left">
			<div style="margin-bottom: 2px">
				<input id="topSearchbox" name="topSearchbox"
					class="easyui-searchbox"
					data-options="searcher:doSearch,width: 350, height: 26, prompt: 'search content', menu: '#topSearchboxMenu'" />
				<div id="topSearchboxMenu" style="width: 85px;">
					<div data-options="name:'0', iconCls: 'icon-hamburg-zoom'">SearchType</div>
					<div data-options="name:'1'">操作人</div>
					<div data-options="name:'2'">IP地址</div>
					<div data-options="name:'3'">URL地址</div>
					<div data-options="name:'4'">操作名称</div>
					<div data-options="name:'5'">操作结果</div>
				</div>
			</div>
		</div>
	</div>
	<table id="dd" class="easyui-datagrid"
		data-options="url:'log_list.action',method:'get',toolbar:'#pp',fit:true,border:false,rownumbers:true">
		<thead>
			<tr>
				<th width="5%" data-options="field:'userName'">操作人</th>
				<th width="8%" data-options="field:'address'">IP地址</th>
				<th width="9%" data-options="field:'startTime'">开始时间</th>
				<th width="4%" data-options="field:'type'">日志类型</th>
				<th width="40%" data-options="field:'operationName'">操作名称</th>
				<th width="8%" data-options="field:'operationParam'">操作参数</th>
				<th width="20%" data-options="field:'uri'">URL地址</th>
				<th width="5%" data-options="field:'result'">操作结果</th>
			</tr>
		</thead>
	</table>
</body>
</html>