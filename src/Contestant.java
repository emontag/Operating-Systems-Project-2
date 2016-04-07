import java.util.Random;


public class Contestant extends Thread{
   private int id_num;
   public void msg(String m) { 
      System.out.println("["+(System.currentTimeMillis()-Club.time)+"] "+getName()+": "+m);
   }
   public Contestant(int id) { 
      setName("Contestant-" + id); id_num=id;
   }
   public int getIDNum(){return id_num;}
   public  void run(){
      goToClub();
      msg("Ending Contestant " +id_num);
   }
   public void goToClub(){
      Random r=new Random();
      try {
         sleep(r.nextInt(2000));//allows others to go ahead
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      msg("Enter the club and waiting for SmartPants");
      try {
         Club.smartpants_mutex.acquire();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      Club.arrival_list[getIDNum()]=true;
      msg("Met with SmartPants");
      try{
         for(int date_try=0;date_try<Club.num_rounds;date_try++){
            try {//get everyone a date
               if(r.nextBoolean()) sleep(2000);
            } catch (InterruptedException e1) {
               e1.printStackTrace();
            }
            Club.contestant_wait.my_acquire(this);
         }
         
         boolean permission;
         do{
            permission=Club.contestants_count.tryAcquire();//only add if available
         }while(!permission);
         Club.contestant_done++;
         Club.current_size++;
         if(Club.contestant_done==Club.num_contestants) Club.smartpants_wait.release();//release snartpants at finish
         msg("Waiting outside club");
         sleep(r.nextInt(2000));
         if(Club.contestant_done==Club.num_contestants) Club.group_size=Club.current_size;//ensure group size is updated if less than group_size left
         if(Club.current_size<Club.group_size) {//take care of semaphoresWeird1234
            
            Club.contestants_count.release();
            Club.contestant_finish.acquire();
         }
         else {
            Club.current_size=0;
            Club.contestant_finish.release(Club.group_size-1);
            printing();
            Club.contestants_count.release();
         }
        
         
      }catch(Exception e){
         e.printStackTrace();
      }
           
      
   }
   //check if dates are available
   public boolean noDates(){
      for(int num=0;num<Club.dates.length;num++){
         if(Club.date_available[num]=true) return false;
      }
      return true;
   }
   public void printing(){
      msg("Ending. Numbers are for date(s): ");
      for(int num=0;num<Club.num_of_dates[id_num].length;num++){
         if(Club.num_of_dates[id_num][num]) System.out.print(num +" ");
      }
      System.out.println();
   }

}
