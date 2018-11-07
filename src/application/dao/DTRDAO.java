package application.dao;

import java.util.List;

import application.entities.DTR;

public interface DTRDAO {
	public List<DTR> getAllDTR();
	public void insert(DTR dtr);
	public void update(String query);
	public void delete(String query);
}
