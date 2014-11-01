package app.struts.action;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import app.annotation.ActionMethod;
import app.annotation.ActionType;
import app.entries.EntryPage;
import app.entries.TaskLog;
import app.service.TaskLogService;

@Controller("taskLogAction")
@Scope("prototype")
public class TaskLogAction extends SupportAction<TaskLog>{

	private static final long serialVersionUID = -1340415028000783230L;
	
	@Resource(name="taskLogService")
	@Getter @Setter private TaskLogService service;
	

	@ActionMethod(type=ActionType.BUSINESS,name="展现定时任务日志")
	public String exec_listlog(){
		EntryPage entryPage = service.getPage(page, size);
		requestMap.put("entrypage", entryPage);
		return SUCCESS;
	}
	
	@ActionMethod(type=ActionType.BUSINESS,name="ajax返回时任务日志")
	public String exec_list(){
		EntryPage list = ((TaskLogService)service).getPage(page, size);
		printJSON(list.getList());
		return null;
	}
}
