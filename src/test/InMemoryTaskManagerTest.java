package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void managerInitialization() {
        manager = new InMemoryTaskManager();
    }
}