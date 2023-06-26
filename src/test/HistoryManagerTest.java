package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void initializeTheManagerBeforeEachTest() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void checkWhenAddingTasksToTheHistory() {
        ArrayList<Integer> idSubtaskForEpic = new ArrayList<>();
        idSubtaskForEpic.add(2);
        Epic epic = new Epic("Epic1", "Description", TasksStatus.NEW, 1, idSubtaskForEpic, TaskType.EPIC);
        Subtask subtask = new Subtask("Subtask1", "Description", TasksStatus.NEW, 2, 1, TaskType.SUBTASK);
        Task task = new Task("Task1", "Description", TasksStatus.NEW, 3, TaskType.TASK);

        assertNull(historyManager.getHistory(), "Список истории не пуст.");

        List<Task> historyListTask = new ArrayList<>();
        historyListTask.add(epic);

        historyManager.add(epic);
        historyManager.add(epic);

        boolean listComparisonWithRepetition = historyManager.getHistory().equals(historyListTask);

        assertTrue(listComparisonWithRepetition, "Историю при повторном добавлении не удалилась.");

        historyListTask.add(subtask);
        historyListTask.add(task);

        historyManager.add(subtask);
        historyManager.add(task);

        boolean comparisonOfTheCompletedHistory = historyManager.getHistory().equals(historyListTask);

        assertTrue(comparisonOfTheCompletedHistory, "Неправильная запись истории.");

        historyListTask.remove(2);
        historyManager.remove(3);

        boolean historyComparisonForTailRemoval = historyManager.getHistory().equals(historyListTask);
        assertTrue(historyComparisonForTailRemoval, "Ошибка при удалении с конца истории.");

        historyListTask.clear();
        historyManager.remove(1);
        historyManager.remove(2);

        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);
        historyListTask.add(epic);
        historyListTask.add(subtask);
        historyListTask.add(task);

        historyListTask.remove(1);
        historyManager.remove(2);

        boolean historyComparisonWhenRemovingTheMiddle = historyManager.getHistory().equals(historyListTask);
        assertTrue(historyComparisonWhenRemovingTheMiddle, "Ошибка при удалении из середины истории.");

        historyListTask.clear();
        historyManager.remove(1);
        historyManager.remove(2);

        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);
        historyListTask.add(epic);
        historyListTask.add(subtask);
        historyListTask.add(task);

        historyListTask.remove(0);
        historyManager.remove(1);

        boolean comparisonOfHistoryWhenRemovingTheHead = historyManager.getHistory().equals(historyListTask);
        assertTrue(comparisonOfHistoryWhenRemovingTheHead, "Ошибка при удалении из середины истории.");
    }
}