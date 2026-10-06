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
}