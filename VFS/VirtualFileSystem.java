import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager =
                new MemoryManager();
    }

    private int allocateInode() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        // Parcourir les inodes de 0 à MAX_INODES - 1.
        // Identifier le premier inode libre.
        // Retourner son numéro.
        for (int i = 0; i <= MemoryManager.MAX_INODES - 1; i++){
            Inode inodeActuel = new Inode(this.memoryManager, i);
            int type = inodeActuel.getFileType();
            if (type == 0) {
                return i;
            }
        }
        return -1;
    }

    public boolean createFile(
            String directory,
            String filename) {

        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }

        
        // Construire l'inode.
        // L'initialiser comme fichier vide.

        Inode inodeConstruit = new Inode(this.memoryManager, inodeNum);

        inodeConstruit.writeToMemory(1, 0, 0L, 0L, new int[10], 0, (short)0, 1);

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public boolean writeFile(
        int inodeNum,
        byte[] data) {

    int blocksNeeded =
            (data.length
            + MemoryManager.BLOCK_SIZE - 1)
            / MemoryManager.BLOCK_SIZE;

    if (blocksNeeded > Inode.DIRECT_POINTERS) {
        return false;
    }

    int[] blockPointers =
            new int[Inode.DIRECT_POINTERS];

    // TODO:
    // Allouer blocksNeeded blocs.

    for (int i = 0; i < blocksNeeded; i++) {
        int allocatedBlock = memoryManager.allocateBlock();
        
        if (allocatedBlock == -1) {
            return false; // Pas assez de blocs libres
        }
        
        blockPointers[i] = allocatedBlock;
    }

    byte[] memory =
            memoryManager.getFilesystemMemory();

    int bytesRemaining =
            data.length;

    int dataSrcOffset = 0;
        
    // Pour chaque bloc :
    // - calculer la quantité à copier ;
    // - récupérer le numéro du bloc ;
    // - calculer son offset physique ;
    // - copier les données.

    for (int i = 0; i < blocksNeeded; i++) {
    
        int blockNum = blockPointers[i];
        
        int physicalOffset = blockNum * MemoryManager.BLOCK_SIZE;
        
        int bytesToCopy = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
        
        for (int j = 0; j < bytesToCopy; j++) {
            memory[physicalOffset + j] = data[dataSrcOffset + j];
        }
        
        dataSrcOffset += bytesToCopy;
        bytesRemaining -= bytesToCopy;
    }

    Inode inode = new Inode(memoryManager, inodeNum);
    
    inode.writeToMemory(1, data.length, 0L, 0L, blockPointers, 0, (short) 0, 1);

    return true;
    }

    public byte[] readFile(int inodeNum) {

    Inode inode =
            new Inode(memoryManager, inodeNum);

    int fileSize =
            inode.getFileSize();

    if (fileSize == 0) {
        return new byte[0];
    }

    byte[] fileData =
            new byte[fileSize];

    byte[] memory =
            memoryManager.getFilesystemMemory();

    int[] blockPointers =
            inode.getDirectPointers();

    
    int bytesRemaining = fileSize;
    int dataDestOffset = 0;
    
    int blocksToRead = (fileSize + MemoryManager.BLOCK_SIZE - 1) / MemoryManager.BLOCK_SIZE;

    for (int i = 0; i < blocksToRead; i++) {
        int blockNum = blockPointers[i];
        int physicalOffset = blockNum * MemoryManager.BLOCK_SIZE;
        
        int bytesToRead = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
        
        for (int j = 0; j < bytesToRead; j++) {
            fileData[dataDestOffset + j] = memory[physicalOffset + j];
        }
        
        dataDestOffset += bytesToRead;
        bytesRemaining -= bytesToRead;
    }

    return fileData;
}
}
