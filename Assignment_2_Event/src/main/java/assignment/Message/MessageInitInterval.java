package assignment.Message;

public class MessageInitInterval {

    private final int MAXL;
    private final int NI;

    public MessageInitInterval(final int MAXL, final int NI){
        this.MAXL = MAXL;
        this.NI = NI;
    }

    public int getMAXL(){
        return this.MAXL;
    }

    public int getNI(){
        return this.NI;
    }
}
