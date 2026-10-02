public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(
            MemoryManager memoryManager,
            int inodeNumber) {

        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
       
        // Calculer l'offset exact de l'inode.
        int offset = 1024 + (this.inodeNumber * INODE_SIZE);
        return offset;
    }

    public int getFileType() {
        
    byte[] memory = memoryManager.getFilesystemMemory();
    
    
    int baseOffset = getInodeOffset() + 4;

    
    int type = ((memory[baseOffset] & 0xFF) << 24) |
               ((memory[baseOffset + 1] & 0xFF) << 16) |
               ((memory[baseOffset + 2] & 0xFF) << 8) |
               ((memory[baseOffset + 3] & 0xFF));

    return type;
    }

    public int getFileSize() {
        // TODO:
        // Lire la taille à offset + 8.
        return 0;
    }

    public int[] getDirectPointers() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] pointers =
                new int[DIRECT_POINTERS];

        // TODO:
        // Lire les 10 pointeurs directs.

        return pointers;
    }
}
