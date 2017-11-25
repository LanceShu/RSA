import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

public class RSA {

//    private static final String fileName = "E:\\Java\\RSA.txt";
    private static String fileName = "";

    private static BigInteger p = null;
    private static BigInteger q = null;
    private static BigInteger n = null;
    private static BigInteger t = null;
    private static BigInteger e = null;
    private static BigInteger d = null;

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        long start  = System.currentTimeMillis();
        createPQ();
        System.out.println("p:"+ p+"  ,q:"+q);
        calculateNT();
        System.out.println("n: " + n);
        System.out.println("t: " + t);

        createE();
        System.out.println("公钥e: " + e);
        d = e.modPow(BigInteger.valueOf(-1),t);
        System.out.println("私钥d : " + "" + d);

        fileName = "E:/Java/RSA.txt";
        encryptFile(fileName);

        fileName = "E:/Java/rsb.txt";
        decryptFile(fileName);

        long end = System.currentTimeMillis();
        System.out.println("\n\n总耗时：" + (end - start) + " ms");

    }

    /**
     * 选择一个e，1~t之间的素数;
     * */
    private static void createE(){
        int s = t.intValue();

        while(true){
            e = BigInteger.valueOf((int)(Math.random() * s / 666));
            if(e.intValue() > 9999 && e.intValue() < 100000){
                if(isPrime(e.intValue()) ){
                    break;
                }
            }
        }

    }

    /**
     * 创建p与q，
     * p与q 各为素数；
     * 并且互为互素;
     * */
    private static void createPQ() {

        int s;
        int p1;
        int q1;

        while(true){

            while(true){
                s = (int)(Math.random() * 99999);
                if(s > 9999){
                    if(isPrime(s)){
                        p1= s;
                        break;
                    }
                }
            }

            while(true){
                s = (int)(Math.random() * 99999);
                if( s >9999){
                    if(isPrime(s)){
                        q1 = s;
                        break;
                    }
                }
            }

            if(isCoprime(p1,q1)){
                p = BigInteger.valueOf(p1);
                q = BigInteger.valueOf(q1);
                break;
            }

        }
    }

    /**
     * 通过p与q
     * 计算n与t；
     * */
    private static void calculateNT(){
        n = p.multiply(q);
        t = (p.subtract(BigInteger.valueOf(1)).multiply((q.subtract(BigInteger.valueOf(1)))));
    }

    /**
     * 判断是否为素数;
     * */
    private static boolean isPrime(int s){
        for(int i = 2;i<Math.sqrt(s);i++){
            if(s%i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     *判断是否互素;
     * */
    private static boolean isCoprime(int p,int q){
        int t;
        if(p < q){
            t = q;
            q = p;
            p = t;
        }

        while(p % q == 1){
            t = q;
            q = p%q;
            p = t;
        }

        if(q == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 加密文件;
     * */
    private static void encryptFile(String fileName) throws IOException {

        ArrayList<String> strings = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        File file = new File("E:/Java/rsb.txt");
        if(file.exists()){
            file.delete();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:/Java/rsb.txt",true)));
        String ds = d.toString()+"\n";
        bw.write(ds,0,ds.length());
        String ns = n.toString()+"\n";
        bw.write(ns,0,ns.length());

        String len = "";
        StringBuilder s = new StringBuilder();
        while( (len = br.readLine()) != null){
            s.append(len);
        }
        String ss = new String(s.toString());
        br.close();

        System.out.println("\n解密前明文 m = " + ss);

        int length;
        if(ss.length()%3 == 0){
            length = ss.length() /3;
            for(int i = 0;i<length;i++){
                strings.add(ss.substring(i*3,i*3+3));
            }
        }else {
            int a;
            length = ss.length() / 3 +1;
            for(a = 0;a<length-1;a++){
                strings.add(ss.substring(a*3,a*3+3));
            }
            strings.add(ss.substring(a*3,ss.length()));
        }

        ArrayList<String> ctext = new ArrayList<>();
        for(int i = 0;i<strings.size();i++){
            byte[] mtext = strings.get(i).getBytes("UTF-8");
            BigInteger m = new BigInteger(mtext);
            BigInteger c = m.modPow(e,n);
            String cs = c.toString()+"\n";
            ctext.add(c.toString());

            bw.write(cs,0,cs.length());
        }
        bw.close();

        System.out.print("密文 c = ");
        for(int i = 0 ;i<ctext.size();i++){
            System.out.print(ctext.get(i));
        }

    }

    /**
     * 解密文件;
     * */
    private static void decryptFile(String fileName)throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        System.out.println("\nsss:"+br.readLine());
        d = new BigInteger(br.readLine());
        System.out.println("\ndddd:"+br.readLine());
        n = new BigInteger(br.readLine());
        String ctext = "";

        System.out.print("\n解密后明文 m = ");
        while((ctext = br.readLine()) != null){
            BigInteger c = new BigInteger(ctext);
            BigInteger m = c.modPow(d,n);
            byte[] mt = m.toByteArray();
            for(int i = 0;i<mt.length;i++){
                System.out.print((char) mt[i]);
            }
        }
        br.close();


    }

}
