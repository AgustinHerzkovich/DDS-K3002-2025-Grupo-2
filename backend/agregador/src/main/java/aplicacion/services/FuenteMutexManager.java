package aplicacion.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@NoArgsConstructor
@Service
public class FuenteMutexManager {
    private final Logger logger = LoggerFactory.getLogger(FuenteMutexManager.class);
    private final ConcurrentHashMap<String, ReentrantLock> fuentesMutex = new ConcurrentHashMap<>();
    public ReentrantLock getMutex(String key){
        return fuentesMutex.computeIfAbsent(key, id -> new ReentrantLock());
    }
    public void lock(String key){
        getMutex(key).lock();
    }
    public void unlock(String key){
        getMutex(key).unlock();
    }
    public void lockAll(Set<String> keys) {
        logger.debug("Locking: " + keys);
        // Ordenar las claves para evitar deadlocks
        String[] sortedKeys = keys.toArray(new String[0]);
        Arrays.sort(sortedKeys);
        List<ReentrantLock> tomados = new ArrayList<>();
        try {
            for (String key : sortedKeys) {
                if (key == null) {
                    throw new IllegalArgumentException("La clave no puede ser null");
                }
                ReentrantLock lock = getMutex(key);
                lock.lock();
                tomados.add(lock);
            }
        } catch (Exception e) {
            for (ReentrantLock lock : tomados) {
                lock.unlock();
            }
            throw e;
        }
        logger.debug("Finished locking: " + keys);
    }
    public void unlockAll(Set<String> locks) {
        System.out.println("Unlocking: " + locks);
        for (String lock : locks) {
            if (getMutex(lock).isHeldByCurrentThread()) {
                getMutex(lock).unlock();
            }
        }
        System.out.println("Finished Unlocking: " + locks);
    }
}
