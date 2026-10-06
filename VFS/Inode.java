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
        
        // Lire la taille à offset + 8.
        byte[] memory = memoryManager.getFilesystemMemory();
    
        int baseOffset = getInodeOffset() + 8;

        int taille = ((memory[baseOffset] & 0xFF) << 24) |
                     ((memory[baseOffset + 1] & 0xFF) << 16) |
                     ((memory[baseOffset + 2] & 0xFF) << 8) |
                     ((memory[baseOffset + 3] & 0xFF));

        return taille;
    }

    public int[] getDirectPointers() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] pointers =
                new int[DIRECT_POINTERS];

        int baseInodeOffset = getInodeOffset();

        // Lire les 10 pointeurs directs.
        for (int i = 0; i < DIRECT_POINTERS; i++) {
            int pointersOffset = baseInodeOffset + 28 + (i * 4);

            int valeurPointeur = ((memory[pointersOffset] & 0xFF) << 24) |
                                 ((memory[pointersOffset + 1] & 0xFF) << 16) |
                                 ((memory[pointersOffset + 2] & 0xFF) << 8) |
                                 ((memory[pointersOffset + 3] & 0xFF));
            pointers[i] = valeurPointeur;

        }
        
        return pointers;
    }

    public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

        byte[] memory =
            memoryManager.getFilesystemMemory();

        int offset = getInodeOffset();

        
        // 1. Numéro d'inode 
        memory[offset]     = (byte) (this.inodeNumber >> 24);
        memory[offset + 1] = (byte) (this.inodeNumber >> 16);
        memory[offset + 2] = (byte) (this.inodeNumber >> 8);
        memory[offset + 3] = (byte) (this.inodeNumber);
    
        offset += 4;

        // 2. Type
        memory[offset]     = (byte) (fileType >> 24);
        memory[offset + 1] = (byte) (fileType >> 16);
        memory[offset + 2] = (byte) (fileType >> 8);
        memory[offset + 3] = (byte) (fileType);

        offset += 4;

        // 3. Taille
        memory[offset]     = (byte) (fileSize >> 24);
        memory[offset + 1] = (byte) (fileSize >> 16);
        memory[offset + 2] = (byte) (fileSize >> 8);
        memory[offset + 3] = (byte) (fileSize);

        offset += 4;

        // 4. Création
        memory[offset]     = (byte) (creationTime >> 56);
        memory[offset + 1] = (byte) (creationTime >> 48);
        memory[offset + 2] = (byte) (creationTime >> 40);
        memory[offset + 3] = (byte) (creationTime >> 32);
        memory[offset + 4] = (byte) (creationTime >> 24);
        memory[offset + 5] = (byte) (creationTime >> 16);
        memory[offset + 6] = (byte) (creationTime >> 8);
        memory[offset + 7] = (byte) (creationTime);

        offset += 8;

        // 5. Modification
        memory[offset]     = (byte) (modificationTime >> 56);
        memory[offset + 1] = (byte) (modificationTime >> 48);
        memory[offset + 2] = (byte) (modificationTime >> 40);
        memory[offset + 3] = (byte) (modificationTime >> 32);
        memory[offset + 4] = (byte) (modificationTime >> 24);
        memory[offset + 5] = (byte) (modificationTime >> 16);
        memory[offset + 6] = (byte) (modificationTime >> 8);
        memory[offset + 7] = (byte) (modificationTime);
        
        offset += 8;

        // 6. 10 pointeurs directs
        for (int i = 0; i < DIRECT_POINTERS; i++) {
            
            memory[offset]     = (byte) (directPointers[i] >> 24);
            memory[offset + 1] = (byte) (directPointers[i] >> 16);
            memory[offset + 2] = (byte) (directPointers[i] >> 8);
            memory[offset + 3] = (byte) (directPointers[i]);

            offset += 4;
        }
        
        // 7. Pointeur indirect
        memory[offset]     = (byte) (indirectPointer >> 24);
        memory[offset + 1] = (byte) (indirectPointer >> 16);
        memory[offset + 2] = (byte) (indirectPointer >> 8);
        memory[offset + 3] = (byte) (indirectPointer);

        offset += 4;

        // 8. Permissions
        memory[offset]     = (byte) (permissions >> 8);
        memory[offset + 1] = (byte) (permissions);

        offset += 2;
        // 9. Nombre de liens
        memory[offset]     = (byte) (linkCount >> 24);
        memory[offset + 1] = (byte) (linkCount >> 16);
        memory[offset + 2] = (byte) (linkCount >> 8);
        memory[offset + 3] = (byte) (linkCount);

        offset += 4;

        }
}
