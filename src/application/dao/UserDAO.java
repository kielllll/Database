package application.dao;

import java.util.List;

import application.entities.User;

public interface UserDAO {
	public List<User> getAllUser();
	public void insert(User user);
	public void update(String query);
	public void delete(String query);
}
