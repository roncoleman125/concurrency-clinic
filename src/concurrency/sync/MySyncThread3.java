package concurrency.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This class demonstrates how to synchronize threads using
 * notify-wait.
 * @author Ron Coleman
 */
public class MySyncThread3 implements Runnable {
    public final static int MAX_DELAY = 1000;
    public final static int TIMEOUT = 5000;
    
    public final List<Good> queue;
    
    public static void main(String[] args) {
        MySyncThread3 myThread = new MySyncThread3();
        
        new Thread(myThread).start();

        myThread.produce();
    }
    
    public MySyncThread3() {
        this.queue = new ArrayList<>( );
    }
    
    @Override
    public void run() {
        consume();
    }
    
    public void produce() {
        Random ran = new Random();
        for(int i=0; i < 10; i++) {
            long delay = ran.nextInt(MAX_DELAY);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
            
            Good good = new Good();
            
            add(good);
            
            System.out.println("produced "+good);
        }
    }
    
    public void consume() {
        while (true) {
            Good good = remove();
            
            if(good == null)
                break;

            System.out.println("consumed " + good);
        }       
    }
    
    public synchronized void add(Good good) {
        queue.add(good);
        notifyAll();
    }
    
    public synchronized Good remove() {
        try {
            if(queue.isEmpty())
                wait(TIMEOUT);
            
            Good good = queue.remove(0);
            
            return good;
        } catch (InterruptedException ex) {
            return null;
        }
    }
}
