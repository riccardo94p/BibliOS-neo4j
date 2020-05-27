package main.java;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import main.java.models.*;

public class LibraryManager {
	private static EntityManager entityManager;
	private static EntityManagerFactory factory;
	private String loggedUser=null;
	private int privilege;
	private int idNode;
	
	public void setup()	{
		factory = Persistence.createEntityManagerFactory("bibliosDB");
	}
	
	public void exit() {
		factory.close();
	}
	
	public List<String> login(String id) {
		if (loggedUser!=null)
			return null;
		String name = null;
		this.privilege = 0;
		List<String> result = new ArrayList<String>();
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			User user = entityManager.find(User.class, id);
			entityManager.getTransaction().commit();
			loggedUser = user.getId();
			privilege = user.getPrivilege();
			idNode = user.getIdNode();
			
			name = user.getName();
			name = name.concat(" ");
			name = name.concat(user.getSurname());
		}catch (Exception ex) {
    		ex.printStackTrace();
    		System.out.println("A problem occurred with the login.");
    	}
		finally {
			entityManager.close();
		}
		if(loggedUser != null) {
			result.add(name);
            result.add(id);
			result.add(Integer.toString(privilege));
			return result;	
		}
		return null;
	}
	
	public void logout() {
		loggedUser = null;
	}
	
	public boolean isLogged() {
		if(loggedUser != null)
			return true;
		return false;
	}
	
	public int getPrivilege() {
		return this.privilege;
	}
	public int getIdNode() { return this.idNode; }
//User table operations
	public String findUser(String userid)
	{
		String resultStr = null;
		User user = null;
		try
		{
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			
			user = entityManager.find(User.class, userid);
			if (user.getPrivilege() == 0)
				resultStr = user.getName() + " " + user.getSurname();
			
			entityManager.getTransaction().commit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("A problem occurred with the LibraryManager.findUser().");
		} finally
		{
			entityManager.close();
		}
		return resultStr;
	}
}