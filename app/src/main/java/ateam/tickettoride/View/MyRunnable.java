package ateam.tickettoride.View;

public class MyRunnable implements Runnable{
    private Object data;

    public MyRunnable(Object data){
        this.data = data;
    }

    public Object getData(){
        return data;
    }

    @Override
    public void run() {

    }
}
