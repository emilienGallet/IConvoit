package fr.iconvoit;

import lombok.Data;

@Data
public class FormChangePassVue {
    public String getOldPass() {
		return oldPass;
	}
	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}
	public String getNewPass() {
		return newPass;
	}
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	private String oldPass;
    private String newPass;


}
