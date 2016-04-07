import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;


public class MySemaphore extends Semaphore{
   private BlockingQueue queue=new LinkedBlockingDeque();
   public MySemaphore(int permits) {
      super(permits);
      
   }
   public void my_acquire(Object object){
      try {
         queue.put(object);
         acquire();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
   public Object my_release(){
      //release();
      Object o=null;
      try {
         o=queue.take();
      } catch (InterruptedException e) {
         //e.printStackTrace();
         
      }
      return o;
   }
   public boolean contains(Object o){
      return queue.contains(o);
   }

}
