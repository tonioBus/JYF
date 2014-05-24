/**
 * 
 */
package com.justyour.food.shared.jpa.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.justyour.food.shared.nutrition.Regime;

/**
 * @author tonio
 * 
 */

@Entity
public class UserID implements IGwtRPC {

	enum Sex {
		MAN,
		WOMAN
	};
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7265353571867005005L;

	boolean isAdmin;

	protected String prenoms;

	protected String nom;

	@Id
	protected String email;

	protected String password;

	protected String uuid;

	@Temporal(TemporalType.DATE)
	protected Date creationDate;

	protected double sizeInCm;
	
	@Temporal(TemporalType.DATE)
	protected Date birthDate;
	
	public double getSizeInCm() {
		return sizeInCm;
	}

	public void setSizeInCm(double sizeInCm) {
		this.sizeInCm = sizeInCm;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public List<Regime> getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(List<Regime> userProfile) {
		this.userProfile = userProfile;
	}

	public String getUuid() {
		return uuid;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	protected Sex sex;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	protected List<Regime> userProfile = new ArrayList<Regime>();

	public UserID() {
	}

	public UserID(String prenoms, String nom, String email, String password) {
		super();
		this.prenoms = prenoms;
		this.nom = nom;
		this.email = email;
		this.password = password;
		creationDate = new Date();
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPrenoms() {
		return prenoms;
	}

	public void setPrenoms(String prenoms) {
		this.prenoms = prenoms;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String isValidate() {
		return null;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

}
