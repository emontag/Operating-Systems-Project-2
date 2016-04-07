import java.util.Random;


public class Date extends Thread{
   private String name;
   private int id_num;
   public void msg(String m) { 
      System.out.println("["+(System.currentTimeMillis()-Club.time)+"] "+getName()+": "+m); 
   }
   public Date(int id) { setName("Date-" + id); id_num=id;}
   public void setThreadName(String n){name=n;}
   public String getThreadName(){ return name;}
   public int getIDNum(){return id_num;}
   static boolean go_home;
   /*
    *  Runs the "Date"
    *  uses semaphore to release contestants
    */
   public  void run(){
      go_home=false;
      try{
         while(!Club.showEnds && !go_home){
            if(!Club.contestant_wait.hasQueuedThreads()) continue;
            setPriority(MAX_PRIORITY);
            Contestant c=(Contestant) Club.contestant_wait.my_release();
            if(c==null) break;//exits when no more contestants
            msg("Meeting Contestant " + c.getIDNum());
            Random r=new Random();
            sleep(r.nextInt(2000));
            if(r.nextBoolean()) Club.num_of_dates[c.getIDNum()][this.getIDNum()]=true;
            setPriority(NORM_PRIORITY);
            Club.contestant_wait.release();
            
         }
      }catch(Exception e){
         e.printStackTrace();
      }
      msg("Going home. ");
    
   }
   /*
    * gives decision whether to give phone number
    */
   public boolean getDecision(){
      Random r=new Random();
      if(r.nextBoolean()==true) {
         return true;
      }
      else return false;
   }
}
