package app.struts.action;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Controller;

import app.annotation.ActionMethod;
import app.annotation.ActionType;
import app.entries.BusinessLog;
import app.entries.EntryPage;
import app.service.BusinessLogService;

@Controller("businessLogAction")
public class BusinessLogAction extends SupportAction<BusinessLog>{

	private static final long serialVersionUID = -4828277730191074463L;
	
	@Resource(name="businessLogService")
	@Getter @Setter private BusinessLogService service;
	
	@ActionMethod(type=ActionType.BUSINESS,name="展现业务日志")
	public String exec_listlog(){
		EntryPage list = ((BusinessLogService)service).getPage(page, size);
		requestMap.put("entrypage", list);
		return SUCCESS;
	}
	
	@ActionMethod(type=ActionType.BUSINESS,name="ajax返回业务日志")
	public String exec_list(){
		EntryPage list = ((BusinessLogService)service).getPage(page, size);
		printJSON(list.getList());
		return null;
	}
}
