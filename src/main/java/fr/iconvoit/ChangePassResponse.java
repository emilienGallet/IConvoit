package fr.iconvoit;

import fr.iconvoit.entity.People;
import lombok.Data;

@Data
public class ChangePassResponse {
    private boolean succes;

    public ChangePassResponse(boolean succes) {
        this.succes = succes;
    }

    
}
