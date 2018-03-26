package entity.message;

import javax.persistence.Entity;

@Entity
public class InitialMessage extends Message {

    private static final long serialVersionUID = 7201507778760836571L;

    public InitialMessage(){
        super();
    }

    public InitialMessage(String text){
        super(text);
    }
}
