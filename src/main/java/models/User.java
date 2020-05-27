package main.java.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class User implements Serializable {

	@Id
	//The id variable is linked to the column of the table with the name userId 
	//therefore we must use the following annotation.
	@Column(name="idUser")
	private String id;
	
	private String name;
	private String surname;
	private int idNode; //id of the corresponding node in the graphDB
	
	@Column(columnDefinition = "tinyint(4) default 0")
	private int privilege;
	
	public User() {
	}
	
	public String getId() {
		return id;
	}
	
	public int getIdNode() { return this.idNode; }
	
	public void setUserId(String id) {
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname=surname;
	}
	
	public int getPrivilege() {
		return privilege;
	}
	
	@Override
    public boolean equals(Object o) {
        if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
 
        User that = (User) o;
        return id != null && id.equals(that.getId());
    }
 
    @Override
    public int hashCode() {
        return 41;
    }
}