import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sb = new StringBuilder(message);
        StringBuilder answ = new StringBuilder();
        for(int i = whichSlice;i<(sb.length());i+=totalSlices){

            if(Character.isLetter(sb.charAt(i))){
                answ.append(sb.charAt(i)); 
            }

        }
        return answ.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for(int i=0;i<klength;i++){
            String s = sliceString(encrypted,i,klength);
            key[i] = cc.getKey(s);
        }
        return key;
    }

    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> hs = new HashSet<String>();
        for(String s:fr.words()){
            s = s.toLowerCase();
            hs.add(s);
        }
        return hs;
    }

    public int countWords(String message, HashSet<String> dictionary){
        int count = 0;
        String[] arr = message.split("\\W+");
        for(int i=0;i<arr.length;i++){
            if(dictionary.contains(arr[i].toLowerCase())){
                count++;
            }
        }
        return count;
    }

    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        int max = 0;
        String answ = " ";
        int[] key1 = {0};
        for(int i=1;i<100;i++){
            char c = mostCommonCharIn(dictionary);
            int[] key = tryKeyLength(encrypted,i,c);
            VigenereCipher vc = new VigenereCipher(key);
            String s = vc.decrypt(encrypted);
            int count = countWords(s,dictionary);
            if(count>max){
                max = count;
                answ = s;
                key1 = key;
            }
        }
        //System.out.println("key.leng"+key1.length);
        //System.out.println("max valid words"+max);
        //for(int i=0;i<key1.length;i++){

        //  System.out.print(key1[i]+" ");
        //}
        //System.out.println();
        return answ;
    }

    public char mostCommonCharIn(HashSet<String> dictionary){
        HashMap<Character,Integer> al = new HashMap<Character,Integer>();
        for(String s: dictionary){
            for(int i=0;i<s.length();i++){
                if(!al.containsKey(s.charAt(i))){
                    al.put(s.charAt(i),1);
                }
                else{
                    al.put(s.charAt(i),(al.get(s.charAt(i))+1));
                }
            }
        }
        int max = 0;
        char answ = ' ';
        for(Character c : al.keySet()){
            if(al.get(c)>max){
                max = al.get(c);
                answ = c;
            }
        }
        return answ;
    }

    public void breakForAllLangs(String encrypted, HashMap<String,HashSet<String>> languages){
        int max = 0;
        String a = " ";
        HashMap<String,Integer> hm = new HashMap<String,Integer>();
        for(String s:languages.keySet()){
            int count = (countWords(encrypted, languages.get(s)));
            if(count>max){
                max = count;
                hm.put(s,count);
                a = s;
            }

        }
        System.out.println("count of words: "+max);
        
        for(String s:hm.keySet()){
            if(hm.get(s)==max){
                System.out.println("language: "+s);
            }
        }
        
        System.out.println(breakForLanguage(encrypted, languages.get(a)));
    }

    public void breakVigenere () {
        // System.out.println(sliceString("abcdefghijklm", 0, 3));
        // System.out.println(sliceString("abcdefghijklm", 1, 3));
        FileResource file = new FileResource();
        String s = file.asString();
        String[] lang = {"English","Danish","Dutch","French","German","Italian","Portuguese","Spanish"};
        HashMap<String,HashSet<String>> hm = new HashMap<String,HashSet<String>>();
        for(int i=0;i<lang.length;i++){
            FileResource fr = new FileResource("dictionaries/"+lang[i]);
            hm.put(lang[i],readDictionary(fr));
            //
        }
        breakForAllLangs(s,hm);
        /*FileResource eng = new FileResource("dictionaries/English");
        FileResource dan = new FileResource("dictionaries/Danish");
        FileResource dut = new FileResource("dictionaries/Dutch");
        FileResource fre = new FileResource("dictionaries/French");
        FileResource ger = new FileResource("dictionaries/German");
        FileResource ita = new FileResource("dictionaries/Italian");
        FileResource por = new FileResource("dictionaries/Portuguese");
        FileResource spa = new FileResource("dictionaries/Spanish");
        */
        
        //for(String str:hm.keySet()){
        //    hm.put(s,readDictionary(dict));
        //}
        //HashSet<String> hs = ;
        //breakForLanguage(s,hs);
        //System.out.println(breakForLanguage(s,hs));
        /*int[] key = tryKeyLength(s,38,'e');
        VigenereCipher vc = new VigenereCipher(key);
        String s1 = vc.decrypt(s);
        int count = countWords(s1,hs);
        System.out.println(count);

         */

    }

}
