package application.dao;

import java.util.List;

import application.entities.CashAdvance;

public interface CashAdvanceDAO {
	public List<CashAdvance> getAllCashAdvance();
	public void insert(CashAdvance ca);
	public void update(String query);
	public void delete(String query);
}
