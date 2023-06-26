package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static manager.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file;
    @BeforeEach
    void loadFileManager() throws IOException {
        file = new File("C:\\Users\\Данисимо\\dev\\java-kanban\\src\\test\\historyTest.csv");
        if (file.createNewFile()) {
            manager = loadFromFile(file);
        }
    }

    @AfterEach
    public void deleteFileManager() {
        file.delete();
    }

    @Test
    void checkingFileUploadsAndSaves() throws IOException {
        File fileDownload = new File("C:\\Users\\Данисимо\\dev\\java-kanban\\src\\test\\fileBackedTestDownload.csv");

        ArrayList<Integer> idSubtaskForEpic = new ArrayList<>();
        idSubtaskForEpic.add(2);
        Subtask subtask = new Subtask("Subtask1", "Description", TasksStatus.NEW, 2, 1,
                TaskType.SUBTASK, LocalDateTime.of(2023,6,25,10,30), 600);
        Epic epic = new Epic("Epic1", "Description", TasksStatus.NEW, 1, idSubtaskForEpic,
                TaskType.EPIC, LocalDateTime.of(2023,6,25,10,30), 600);
        Task task = new Task("Task1", "Description", TasksStatus.NEW, 3, TaskType.TASK,
                LocalDateTime.of(2023,10,30,10,30), 600);
        Task task2 = new Task("Task2", "Description", TasksStatus.NEW, 4, TaskType.TASK);

        manager.createSubtask(subtask);
        manager.createEpic(epic);
        manager.createTask(task);
        manager.createTask(task2);

        manager.getEpic(1);
        manager.getTask(3);
        manager.getTask(4);

        manager.deleteById(4);

        String datFile = Files.readString(file.toPath());
        String datDownloadFile = Files.readString(fileDownload.toPath());

        assertEquals(datDownloadFile, datFile, "Данные при загрузке восстановились неверно.");
    }


}