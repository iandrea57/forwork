#### 字符串算法
###### KMP

预处理时间：O(M)

匹配时间复杂度：O(N)

主要过程：通过对字串进行预处理，当发现不能匹配时，可以不进行回溯。

    public class KMP {
    
        public static int find(char[] text, char[] find) {
            if (text == null || find == null || text.length == 0 || find.length == 0) {
                return -1;
            }
            int textLength = text.length;
            int findLength = find.length;
            if (textLength < findLength) {
                return -1;
            }
            PrintUtils.println(find);
            int[] prefix = computePrefixFunction(find);
            PrintUtils.println(prefix);
            int q = -1;
            for (int i = 0; i < textLength; i++) {
                while (q > -1 && find[q+1] != text[i])
                    q = prefix[q];
                if (find[q+1] == text[i])
                    q++;
                if (q == findLength - 1) {
                    return i - q;
                }
            }
            return -1;
        }
    
        private static int[] computePrefixFunction(char[] find) {
            int length = find.length;
            int[] prefix = new int[length];
            prefix[0] = -1;
            int k = -1;
            for (int i = 1; i < length; i++) {
                while(k > -1 && find[k+1] != find[i])
                    k = prefix[k];
                if (find[k+1] == find[i])
                    k++;
                prefix[i] = k;
            }
            return prefix;
        }
    }
    
    
前缀函数结果

     a   b   a   b   a   c   a
    -1  -1   0   1   2  -1   0
    
    
###### BruteForce

暴力搜索，也是最容易想到的方法。

预处理时间 O(0)

匹配时间复杂度O(N*M)

主要过程：从原字符串开始搜索，若出现不能匹配，则从原搜索位置+1继续。

    public class BruteForce {
    
        public static int find(char[] text, char[] find) {
            if (text == null || find == null || text.length == 0 || find.length == 0) {
                return -1;
            }
            int textLength = text.length;
            int findLength = find.length;
            if (textLength < findLength) {
                return -1;
            }
            int s = 0;
            int p = s;
            int q = 0;
            while (p < textLength) {
                if (text[p] == find[q]) {
                    p++;
                    q++;
                } else {
                    s++;
                    p = s;
                    q = 0;
                }
                if (q == findLength) {
                    return p - q;
                }
            }
            return -1;
        }
    }