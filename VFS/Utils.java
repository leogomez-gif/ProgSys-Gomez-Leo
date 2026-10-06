

public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
       
        memory[offset] = (byte) (value >> 24);
        memory[offset+1] = (byte) (value >> 16);
        memory[offset+2] = (byte) (value >> 8);
        memory[offset+3] = (byte) (value >> 0);
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        int resultat = ((memory[offset] & 0xFF) << 24)
                         | ((memory[offset+1] & 0xFF) << 16)
                         | ((memory[offset+2] & 0xFF) << 8)
                         | (memory[offset+3] & 0xFF);

        return resultat;
        
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        memory[offset] = (byte) (value >> 8);
        memory[offset+1] = (byte) (value >> 0);
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        int resultat = ((memory[offset] & 0xFF) << 8)
                          | (memory[offset+1] & 0xFF);

        return (short) resultat;
    }
    public static int writeLong(byte[] memory, int offset, long value) {
        memory[offset]     = (byte) (value >> 56);
        memory[offset + 1] = (byte) (value >> 48);
        memory[offset + 2] = (byte) (value >> 40);
        memory[offset + 3] = (byte) (value >> 32);
        memory[offset + 4] = (byte) (value >> 24);
        memory[offset + 5] = (byte) (value >> 16);
        memory[offset + 6] = (byte) (value >> 8);
        memory[offset + 7] = (byte) (value);
        
        return 8;
    }

    public static long readLong(byte[] memory, int offset) {
        long resultat = ((memory[offset]     & 0xFFL) << 56)
                      | ((memory[offset + 1] & 0xFFL) << 48)
                      | ((memory[offset + 2] & 0xFFL) << 40)
                      | ((memory[offset + 3] & 0xFFL) << 32)
                      | ((memory[offset + 4] & 0xFFL) << 24)
                      | ((memory[offset + 5] & 0xFFL) << 16)
                      | ((memory[offset + 6] & 0xFFL) << 8)
                      |  (memory[offset + 7] & 0xFFL);
                      
        return resultat;
    }

    public static int writeString(byte[] memory, int offset, String str, int maxLength) {
        byte[] bit = str.getBytes();
        for (int i = 0; i < maxLength; i++) {
            if (i < bit.length) {
                memory[offset + i] = bit[i];
            } else {
                memory[offset + i] = 0;        
            }
        }
        return maxLength;
    }


    public static String readString(
        byte[] memory,
        int offset,
        int maxLength) {

        int length = 0;

            while (length < maxLength && memory[offset + length] != 0) {
                    length++;
            }

        return new String(memory, offset, length);
    } 
}