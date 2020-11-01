package sample;

public class MakeList {

    public static void main(String[] args) {
        String list[] = {"aa" , "ann" , "an" , "_an" , "_ann" , "_aa" , "a" , "ana" , "nn" , "naa" , "a_nn" , "nana","nan","nnn"};
        int limit = 10;
        for (int i = 0; i <list.length ; i++) {
            for (int j = 0; j < limit; j++) {
                System.out.println(list[i]+j+""+j+""+j);
            }
        }





    }
}
