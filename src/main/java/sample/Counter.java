package sample;

/**
 * Created by Max on 5/12/2016.
 */
public class Counter {

        private int c;

        public Counter() {
            this.c = 0;
        }

        public void increment(){
            this.c++;
        }

        public void decrement(){
            this.c--;
        }

        public String toString(){
            return String.valueOf(c);
        }
}
