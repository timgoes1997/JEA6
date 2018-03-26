package entity.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class ReplyMessage extends Message implements Serializable {

    private static final long serialVersionUID = 6864653489060895368L;

    @OneToOne
    @JoinColumn(name = "MESSAGE")
    private Message message;

    public ReplyMessage(){
        super();
    }

    public ReplyMessage(String text){
        super(text);
    }

    public Message getMessage() {
        return message;
    }
}
