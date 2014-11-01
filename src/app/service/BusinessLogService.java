package app.service;

import app.annotation.ServiceMethod;
import app.annotation.ServiceType;
import app.entries.BusinessLog;
import app.entries.EntryPage;


public interface BusinessLogService extends SupportService<BusinessLog>{

	@ServiceMethod(type=ServiceType.SYSTEM,name="分页获取业务日志记录")
	public EntryPage getPage(int page, int size);
}
