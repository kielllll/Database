package application.dao;

import java.util.List;

import application.entities.SSS;

public interface SSSDAO {
	public List<SSS> getAllSSS();
	public void insert(SSS sss);
	public void update(String query);
	public void delete(String query);
}
