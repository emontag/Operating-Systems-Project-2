


public class SmartPants extends Thread{
   public void msg(String m) { System.out.println("["+(System.currentTimeMillis()-Club.time)+"] "+getName()+": "+m); }
   public SmartPants(int id) { 
      setName("Smartpants-" + id);
      available_to_meet=true;
   }
   private volatile boolean available_to_meet;
   public  boolean meetSmartpants() { return available_to_meet; }
  
   public void run(){
      while(Club.moreArrivals()){//meet Contestants
         try {
            sleep(200);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
        //wait for more
         if(!Club.smartpants_mutex.hasQueuedThreads()) continue;
         Club.smartpants_mutex.release();
      }
      //wait until end
      try {
         Club.smartpants_wait.acquire();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      Club.showEnds=true;
      Date.go_home=true;
      for(int num=0;num<Club.num_dates;num++){//tell dates go home
         Club.dates[num].interrupt();
      }
      msg("End smartpants");
   }
   public void talkToArrival(){
      Contestant c=(Contestant) Club.arrival_queue.peek();
      Club.arrival_queue.remove();
      msg("Smartpants has spoken to Contestant " + c.getIDNum());
      Club.arrival_list[c.getIDNum()]=true;
   }
   public boolean show_not_end(){
      if(Club.contestant_done==Club.num_contestants) {
         Club.showEnds=true;
         return false;
      }
      else return true;
     
            
   }
   

}
