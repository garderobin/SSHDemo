package app.dao;

import java.util.List;

import app.entries.Tree;

public interface TreeDao extends SupportDao<Tree> {
	
	public List<Tree> getTrees(String rootId);

}
