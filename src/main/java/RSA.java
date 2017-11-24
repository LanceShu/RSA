import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

//        while(true){
//            System.out.println("\n\n请选择您要执行的操作：\n1.加密文件\n2.解密文件\n0.退出");
//            System.out.print("请选择：");
//            int choose = input.nextInt();
//            switch (choose){
//                case 1:
////                    System.out.println("请输入文件路径：");
////                    fileName = input.nextLine();
//                    fileName = "E:\\Java\\RSA.txt";
//                    encryptFile(fileName);
//                    break;
//                case 2:
//                    break;
//                case 0:
//                    System.exit(0);
//                    break;
//                default:
//                    System.out.println("功能序号输入有误,请重新输入~");
//                    break;
//            }
//        }
    }

    /**
     * 暂时无用：
     * 计算私钥d；
     * */
    private static BigInteger[] calculateD(){
        BigInteger[] ret = new BigInteger[3];
        BigInteger u  = BigInteger.valueOf(1);
        BigInteger u1 = BigInteger.valueOf(0);
        BigInteger v = BigInteger.valueOf(0);
        BigInteger v1 = BigInteger.valueOf(1);
        if(e.compareTo(t) > 0){
            System.out.println("1111"+e+","+t);
            BigInteger tem = t;
            t = e;
            e = tem;
        }

        while(e.compareTo(BigInteger.valueOf(0)) != 0){
            BigInteger tq = t.divide(e);
            BigInteger tu = u;
            u = u1;
            u1 = tu.subtract(tq.multiply(u1));
            BigInteger tv = v;
            v = v1;
            v1 = tv.subtract(tq.multiply(v1));
            BigInteger td1 = t;
            t = e;
            e = td1.subtract(tq.multiply(e));
            ret[0] = u;
            ret[1] = v;
            ret[2] = t;
            System.out.println("2222"+e+","+t);
        }
        return ret;
    }

    /**
     * 选择一个e，1~t之间的素数;
     * */
    private static void createE(){
        int s = t.intValue();

        while(true){
            e = BigInteger.valueOf((int)(Math.random() * s / 666 - 5000));
            if(e.intValue() > 9999 && e.intValue() < 20000){
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

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String len = "";
        StringBuilder s = new StringBuilder();
        while( (len = br.readLine()) != null){
            s.append(len);
        }
        String ss = new String(s.toString());
        br.close();

        System.out.println("m = " + ss);
        byte[] mtext = ss.getBytes("UTF-8");
        BigInteger m = new BigInteger(mtext);

        System.out.println(m);
        BigInteger c = m.modPow(e,n);

        String cs = c.toString();
        System.out.println("c = " + cs);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:/Java/rsb.txt")));
        bw.write(cs,0,cs.length());
        bw.close();

    }

    /**
     * 解密文件;
     * */
    private static void decryptFile(String fileName)throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String ctext = br.readLine();
        br.close();
        BigInteger c = new BigInteger(ctext);

        System.out.println(c);
        BigInteger m = c.modPow(d,n);
        byte[] mt = m.toByteArray();
        System.out.print("m = ");
        for(int i = 0;i<mt.length;i++){
            System.out.print((char) mt[i]);
        }

    }

}
