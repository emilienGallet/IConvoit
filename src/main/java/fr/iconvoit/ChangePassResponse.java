package fr.iconvoit;

import fr.iconvoit.entity.People;
import lombok.Data;

@Data
public class ChangePassResponse {
    private boolean succes;

    public ChangePassResponse(boolean succes) {
        this.succes = succes;
    }

	public boolean isSucces() {
		return succes;
	}

	public void setSucces(boolean succes) {
		this.succes = succes;
	}

    
}
