/** 
 * (c) 2013 Aquila Services
 * 
 * File: UnityJXBA.java
 * Package: com.aquilaservices.metareceipe.wget.cooking
 * Date: 18 août 2013
 * @author tonio
 * 
 */

package com.justyour.food.server.unity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.codec.language.DoubleMetaphone;

/** 
 * <b>(c) 2013 Aquila Services</b><br/>
 * <br/>
 * <b>File:</b> UnityJXBA.java<br/>
 * <b>Package:</b> com.aquilaservices.metareceipe.wget.cooking</b><br/>
 * <b>Date:</b> 18 août 2013
 * @author tonio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UnityJXBA {

	@XmlAttribute
	protected String description;

	@XmlAttribute
	protected String name;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name = "WorldList")
	private ArrayList<String> word = new ArrayList<String>();

	@XmlTransient
	public ArrayList<String> soundEx = new ArrayList<String>();

	@XmlTransient
	public ArrayList<String> getWords() {
		return word;
	}

	public void setWords(ArrayList<String> words) {
		this.word = words;
	}

	public String getSound() {
		return name;
	}

	public void setSound(String sound) {
		this.name = sound;
		
	}

	public ArrayList<String> getSoundEx() {
		return soundEx;
	}

	public void setSoundEx(ArrayList<String> soundEx) {
		this.soundEx = soundEx;
	}

	public double getCoherences(String unitySz, DoubleMetaphone doubleMetaphone) {
		for (String string : this.word) {
			if( unitySz.equalsIgnoreCase(string)) return 1.0;
		}
		String currentSound = doubleMetaphone.doubleMetaphone(unitySz);
		if(this.soundEx.size()==0) this.soundEx.add(doubleMetaphone.doubleMetaphone(this.name));
		for (String sound : this.soundEx) {
			if(sound.equals(currentSound)) return 0.5;
		}
		return 0;
	}

	@Override
	public String toString() {
		String ret = "name:"+name+" description:"+description;
		return ret;
	}
}
