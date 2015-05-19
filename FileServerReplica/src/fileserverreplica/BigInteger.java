package passwordreplica;

import java.util.Vector;

/**
 *
 * @author leticia
 */
public class BigInteger {
    Vector<Integer> a = new Vector<Integer>();
    
    BigInteger(String bign){
        for(int i = 0; i < bign.length(); i++){
            this.a.add(bign.charAt(bign.length() -1 -i) - '0');
        }
    }
    
    BigInteger(Vector<Integer> a){
        this.a = a;
    }
    
    public int getIndex(int x){
        return this.a.get(x);
        
    }
    
    public void setIndex(int x, int v){
        this.a.set(x, v);
    }
    public int getSize(){
        return a.size();
    }
    
    public BigInteger multi(BigInteger bigfact2){
        int temp,carry = 0;
        Vector<Integer> vec = new Vector<Integer>();
        for(int i = 0; i < this.a.size() + 1; i++){
            for(int j = 0; j < bigfact2.getSize() + 1; j++){
                vec.add(0);
            }
        }
        for(int i = 0; i < this.a.size() + 1; i++){
            carry = 0;
            for(int j = 0; j < bigfact2.getSize() + 1; j++){
                if (i >= a.size() || j >= bigfact2.getSize())
                {
                    temp = vec.get(i+j) + carry;
                }else
                    temp = (this.getIndex(i)*bigfact2.getIndex(j)) + vec.get(i+j) + carry;
                carry = temp/10;
                vec.set(i+j, temp%10);
            }
        }
        Vector<Integer> ret = new Vector<Integer>();
        
        int ff = 0;
        for(int i = vec.size()-1; i >= 0; i--)
        {
            if ( vec.get(i) != 0)
            {
                ff = i;
                break;
            }
        }
        for(int i = 0; i < ff + 1; i++)
        {
            ret.add(vec.get(i));
        }
        return new BigInteger(ret);
    }
    
    @Override
    public String toString()
    {
        String ret = "";
        for (int i = 0; i < a.size(); ++i)
        {
            ret = Integer.toString(this.a.get(i)) + ret;
        }
        return ret;
    }
    
    public boolean compare(BigInteger a, BigInteger b){
        return (a.toString()).equals(b.toString());
    }
}
