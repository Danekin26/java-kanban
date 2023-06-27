package test;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;

    @Test
    void checkingToGetAListOfTasks() throws IOException {
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        assertEquals(0, manager.getListTask().size(), "Список задач не пуст.");

        Task task1 = new Task("Task1", "Description1", TasksStatus.NEW);
        Task task2 = new Task("Task2", "Description2", TasksStatus.NEW);
        manager.createTask(task1);
        manager.createTask(task2);
        assertEquals(2, manager.getListTask().size(), "Размер списка не соответствует количеству задач.");
    }

    @Test
    void checkingToGetAListOfEpics() throws IOException {
        Task task = new Task("Task", "Description", TasksStatus.NEW);
        Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);
        manager.createTask(task);
        manager.createSubtask(subtask);
        assertEquals(0, manager.getListEpic().size(), "Список эпиков не пуст.");

        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        assertEquals(2, manager.getListEpic().size(), "Размер списка не соответствует количеству задач.");
    }

    @Test
    void checkingToGetAListOfSubtasks() throws IOException {
        Task task = new Task("Task", "Description", TasksStatus.NEW);
        Epic epic = new Epic("Epic", "Description");
        manager.createTask(task);
        manager.createEpic(epic);
        assertEquals(0, manager.getListSubtask().size(), "Список подзадач не пуст.");

        Subtask subtask1 = new Subtask("Subtask1", "Description1", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TasksStatus.NEW);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        assertEquals(2, manager.getListSubtask().size(), "Размер списка не соответствует количеству задач.");
    }

    @Test
    void checkingTheDeletionOfTheListOfAllTasks() throws IOException {
        Task task1 = new Task("Task1", "Description1", TasksStatus.NEW);
        Task task2 = new Task("Task2", "Description2", TasksStatus.NEW);
        Task task3 = new Task("Task3", "Description3", TasksStatus.NEW);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        assertEquals(3, manager.getListTask().size(), "Список задач не заполнился.");


        manager.deleteTask();
        assertEquals(0, manager.getListTask().size(), "Список задач не очистился.");
    }

    @Test
    void checkingTheDeletionOfTheListOfAllSubtasks() throws IOException {
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TasksStatus.NEW);
        Subtask subtask3 = new Subtask("Subtask3", "Description3", TasksStatus.NEW);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        manager.deleteSubtask();
        assertEquals(0, manager.getListSubtask().size(), "Список подзадач не очистился.");
    }

    @Test
    void checkingTheDeletionOfTheListOfAllEpics() throws IOException {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");
        Subtask subtask11 = new Subtask("Subtask1 for Epic1", "Description1", TasksStatus.NEW);
        Subtask subtask12 = new Subtask("Subtask2 for Epic1", "Description2", TasksStatus.NEW);
        Subtask subtask21 = new Subtask("Subtask1 for Epic2", "Description1", TasksStatus.NEW);
        Subtask subtask22 = new Subtask("Subtask2 for Epic2", "Description2", TasksStatus.NEW);

        epic1.setIdToSubtask(subtask11.getId());
        subtask11.setIdToEpic(epic1.getId());
        epic1.setIdToSubtask(subtask12.getId());
        subtask12.setIdToEpic(epic1.getId());
        epic2.setIdToSubtask(subtask21.getId());
        subtask21.setIdToEpic(epic2.getId());
        epic2.setIdToSubtask(subtask22.getId());
        subtask22.setIdToEpic(epic2.getId());

        manager.deleteEpic();

        assertEquals(0, manager.getListSubtask().size(), "Подзадачи не удалены при очистке эпиков.");
        assertEquals(0, manager.getListEpic().size(), "Список эпиков не очистился.");
    }

    @Test
    void checkToGetATaskWithAnExistingIdAndANonExistingId() throws IOException {
        Task task1 = new Task("Task1", "Description1", TasksStatus.NEW);
        Task task2 = new Task("Task2", "Description2", TasksStatus.NEW);
        Task task3 = new Task("Task3", "Description3", TasksStatus.NEW);
        Task task4 = new Task("Task4", "Description4", TasksStatus.NEW);
        Task task5 = new Task("Task5", "Description5", TasksStatus.NEW);
        Task task6 = new Task("Task6", "Description6", TasksStatus.NEW);
        Task task7 = new Task("Task7", "Description7", TasksStatus.NEW);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        manager.createTask(task5);
        manager.createTask(task6);
        manager.createTask(task7);

        int idTask1 = task1.getId();
        int idTask4 = task4.getId();
        int idTask7 = task7.getId();

        assertEquals(task1, manager.getTask(idTask1), "Идентификатор для первой задачи задан не верно.");
        assertEquals(task4, manager.getTask(idTask4), "Идентификатор для четвертой задачи задан не верно.");
        assertEquals(task7, manager.getTask(idTask7), "Идентификатор для седьмой задачи задан не верно.");

        assertNull(manager.getTask(18), "Задача с таким id не должна существовать.");
    }

    @Test
    void checkToGetAEpicWithAnExistingIdAndANonExistingId() throws IOException {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");
        Epic epic3 = new Epic("Epic3", "Description3");
        Epic epic4 = new Epic("Epic4", "Description4");
        Epic epic5 = new Epic("Epic5", "Description5");

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createEpic(epic3);
        manager.createEpic(epic4);
        manager.createEpic(epic5);

        int idEpic1 = epic1.getId();
        int idEpic3 = epic3.getId();
        int idEpic5 = epic5.getId();

        assertEquals(epic1, manager.getEpic(idEpic1), "Идентификатор для первого эпика задан не верно.");
        assertEquals(epic3, manager.getEpic(idEpic3), "Идентификатор для третьего эпика задан не верно.");
        assertEquals(epic5, manager.getEpic(idEpic5), "Идентификатор для пятого эпика задан не верно.");

        assertNull(manager.getEpic(18), "Эпик с таким id не должен существовать.");
    }

    @Test
    void checkToGetASubtaskWithAnExistingIdAndANonExistingId() throws IOException {
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TasksStatus.NEW);
        Subtask subtask3 = new Subtask("Subtask3", "Description3", TasksStatus.NEW);
        Subtask subtask4 = new Subtask("Subtask4", "Description4", TasksStatus.NEW);
        Subtask subtask5 = new Subtask("Subtask5", "Description5", TasksStatus.NEW);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);
        manager.createSubtask(subtask5);

        int idSubtask1 = subtask1.getId();
        int idSubtask3 = subtask3.getId();
        int idSubtask5 = subtask5.getId();

        assertEquals(subtask1, manager.getSubtask(idSubtask1), "Идентификатор для первой подзадачи задан не верно.");
        assertEquals(subtask3, manager.getSubtask(idSubtask3), "Идентификатор для третьей подзадачи задан не верно.");
        assertEquals(subtask5, manager.getSubtask(idSubtask5), "Идентификатор для пятой подзадачи задан не верно.");

        assertNull(manager.getSubtask(18), "Подзадача с таким id не должна существовать.");
    }

    @Test
    void taskUpdateCheck() throws IOException { // проверка получения задачи по любому айди
        Task task = new Task("Task", "Description", TasksStatus.NEW);
        Task taskNew = new Task("Update task", "Description Update", TasksStatus.NEW, 1);
        Task taskNew2 = new Task("Update task2", "Description2 Update", TasksStatus.NEW, 18);


        manager.createTask(task);
        int idOldTask = task.getId();
        manager.updateTask(taskNew);

        assertEquals(taskNew, manager.getTask(idOldTask), "Ошибка при обновлении задачи.");

        manager.deleteTask();

        manager.createTask(task);
        manager.updateTask(taskNew2);

        assertEquals(task, manager.getTask(idOldTask), "Задача не обновлена.");
    }

    @Test
    void epicUpdateCheck() throws IOException {
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask1 = new Subtask("Subtask1", "Description", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description", TasksStatus.NEW);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        int idEpicOld = epic.getId();

        epic.setType(TaskType.EPIC);
        epic.setIdToSubtask(subtask1.getId());
        subtask1.setIdToEpic(epic.getId());
        epic.setIdToSubtask(subtask2.getId());
        subtask2.setIdToEpic(epic.getId());

        Epic epicNew = new Epic("Epic Update", "Description Update", epic.getStatus(), idEpicOld,
                epic.getIdToSubtask(), TaskType.EPIC);

        manager.updateEpic(epicNew);

        assertEquals(epicNew, manager.getEpic(idEpicOld), "Ошибка при обновлении эпика.");
    }

    @Test
    void subtaskUpdateCheck() throws IOException {
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);

        manager.createEpic(epic);
        manager.createSubtask(subtask);

        epic.setIdToSubtask(subtask.getId());
        subtask.setIdToEpic(epic.getId());
        Subtask subtaskNew = new Subtask("Subtask Update", "Description Update", TasksStatus.DONE, 2);

        manager.updateSubtask(subtaskNew);

        assertEquals(subtaskNew, manager.getSubtask(2), "Ошибка при обновлении подзадачи");
    }

    @Test
    void deletingByAnyIndexerOfAnyTask() throws IOException {
        Task task = new Task("Task", "Description", TasksStatus.NEW);
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);
        Task task2 = new Task("Task2", "Description2", TasksStatus.NEW);
        Task task3 = new Task("Task3", "Description3", TasksStatus.NEW);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(task2);
        manager.createTask(task3);

        task.setType(TaskType.TASK);
        epic.setType(TaskType.EPIC);
        subtask.setType(TaskType.SUBTASK);
        task2.setType(TaskType.TASK);
        task3.setType(TaskType.TASK);

        epic.setIdToSubtask(subtask.getId());
        subtask.setIdToEpic(epic.getId());
        manager.deleteById(1);
        assertNull(manager.getEpic(1), "Ошибка при удалении первой задачи");
        manager.deleteById(5);
        assertNull(manager.getTask(5), "Ошибка при удалении пятой задачи");
        manager.deleteById(18);
        assertNull(manager.getTask(18), "Ошибка при удалении задачи с несуществующем id");
    }

    @Test
    void checkToGetAListOfAllSubtasksFromTheEpic() throws IOException {
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask1 = new Subtask("Subtask1", "Description", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description", TasksStatus.NEW);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        epic.setType(TaskType.EPIC);
        subtask1.setType(TaskType.SUBTASK);
        subtask2.setType(TaskType.SUBTASK);
        epic.setIdToSubtask(subtask1.getId());
        subtask1.setIdToEpic(epic.getId());
        epic.setIdToSubtask(subtask2.getId());
        subtask2.setIdToEpic(epic.getId());

        ArrayList<Subtask> listSubtaskForEpic = new ArrayList<>();
        listSubtaskForEpic.add(subtask1);
        listSubtaskForEpic.add(subtask2);

        boolean equalsListSubtasks = manager.getListOfTaskInEpic(epic).equals(listSubtaskForEpic);
        assertTrue(equalsListSubtasks, "Массивы не равны");
        manager.deleteEpic();
        equalsListSubtasks = manager.getListOfTaskInEpic(epic).equals(listSubtaskForEpic);
        assertFalse(equalsListSubtasks, "Массивы равны");
    }

    @Test
    void checkingToGetAnyTaskById() throws IOException {
        Task task = new Task("Task", "Description", TasksStatus.NEW);
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);
        Task task2 = new Task("Task2", "Description2", TasksStatus.NEW);
        Task task3 = new Task("Task3", "Description3", TasksStatus.NEW);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(task2);
        manager.createTask(task3);

        epic.setIdToSubtask(subtask.getId());
        subtask.setIdToEpic(epic.getId());
        assertEquals(task, manager.getAnyTask(1), "Ошибка при проверке нижнего граничного условия");
        assertEquals(task3, manager.getAnyTask(5), "Ошибка при проверке верхнего граничного условия");
        assertNull(manager.getAnyTask(18), "Ошибка при проверке задачи с несуществующим id");
    }

    @Test
    void definingEpicStatusForAnEmptyListOfSubtasks() throws IOException {
        Epic epic = new Epic("Epic1", "Description1");
        manager.createEpic(epic);
        assertEquals(TasksStatus.NEW, epic.getStatus(), "Статус эпика без подзадач указан неверно.");

        Subtask subtask1 = new Subtask("Subtask1", "Description1", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TasksStatus.NEW);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        epic.setIdToSubtask(subtask1.getId());
        subtask1.setIdToEpic(epic.getId());
        epic.setIdToSubtask(subtask2.getId());
        subtask2.setIdToEpic(epic.getId());
        manager.updateEpic(epic);
        assertEquals(TasksStatus.NEW, epic.getStatus(), "Статус эпика с подзадачами со статусом NEW, указан неверно. Должен быть NEW.");

        subtask1.setStatus(TasksStatus.DONE);
        subtask2.setStatus(TasksStatus.DONE);
        manager.updateEpic(epic);
        assertEquals(TasksStatus.DONE, epic.getStatus(), "Статус эпика с подзадачами со статусом DONE, указан неверно. Должен быть DONE.");

        subtask1.setStatus(TasksStatus.NEW);
        manager.updateEpic(epic);
        assertEquals(TasksStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика с подзадачами со статусами NEW и DONE, указан неверно. Должен быть IN_PROGRESS.");

        subtask1.setStatus(TasksStatus.IN_PROGRESS);
        subtask2.setStatus(TasksStatus.IN_PROGRESS);
        manager.updateEpic(epic);
        assertEquals(TasksStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика с подзадачами со статусом IN_PROGRESS, указан неверно. Должен быть IN_PROGRESS.");
    }

    @Test
    void checkForEpicForTheSubtask() throws IOException {
        Epic epic = new Epic("Epic1", "Description1");
        Subtask subtask1 = new Subtask("Subtask1", "Description1", TasksStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask2", "Description2", TasksStatus.NEW);
        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        epic.setIdToSubtask(subtask1.getId());
        subtask1.setIdToEpic(epic.getId());

        manager.updateEpic(epic);

        assertEquals(1, subtask1.getIdToEpic(), "id эпика не совпадает с указанным id эпика в подзадачи");
        assertNotEquals(18, subtask2.getIdToEpic(), "id эпика не совпадает с указанным id эпика в подзадачи");
    }

    @Test
    void checkingToGetAListOfTheTasksYouHaveViewed() throws IOException {
        Task task = new Task("Task", "Description", TasksStatus.NEW);
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);
        Task task2 = new Task("Task2", "Description2", TasksStatus.NEW);
        Task task3 = new Task("Task3", "Description3", TasksStatus.NEW);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(task2);
        manager.createTask(task3);

        assertNull(manager.getListHistory());

        manager.getTask(1);
        manager.getEpic(2);
        manager.getSubtask(3);
        manager.getTask(4);
        manager.getTask(5);

        List<Task> listOfHistoryTask = new ArrayList<>();
        listOfHistoryTask.add(task);
        listOfHistoryTask.add(epic);
        listOfHistoryTask.add(subtask);
        listOfHistoryTask.add(task2);
        listOfHistoryTask.add(task3);

        boolean equalsHistory = manager.getListHistory().equals(listOfHistoryTask);

        assertTrue(equalsHistory, "Список истории неверно записан.");
    }

    @Test
    void checkingTheCalculationOfTheEndOfTheTask() throws IOException {
        LocalDateTime startTime = LocalDateTime.of(2023,10,30,10,30);
        long duration = 600L;
        Task task = new Task("Task1", "Description", TasksStatus.NEW, 3, TaskType.TASK,
                startTime , duration);
        LocalDateTime endTime = startTime.plusSeconds(duration);
        manager.createTask(task);
        assertEquals(endTime, task.getEndTime(), "Ошибка при расчете окончания выполнения задачи.");
        ArrayList<Integer> idSubtaskForEpic = new ArrayList<>();
        idSubtaskForEpic.add(2);
        idSubtaskForEpic.add(4);
        Subtask subtask1 = new Subtask("Subtask1", "Description", TasksStatus.NEW, 2, 1,
                TaskType.SUBTASK, LocalDateTime.of(2023,6,25,10,30), 600);
        Subtask subtask2 = new Subtask("Subtask1", "Description", TasksStatus.NEW, 4, 1,
                TaskType.SUBTASK, LocalDateTime.of(2023,6,25,12,30), 1200);
        Epic epic = new Epic("Epic1", "Description", TasksStatus.NEW, 1, idSubtaskForEpic,
                TaskType.EPIC);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createEpic(epic);
        epic.setSubtaskDuration(subtask1);
        epic.setSubtaskDuration(subtask2);

        LocalDateTime startEpic = LocalDateTime.of(2023,6,25,10,30);
        LocalDateTime endEpic = LocalDateTime.of(2023,6,25,12,30).plusSeconds(1200);

        assertEquals(startEpic, epic.getStartTime(), "Неверный расчет начала эпика.");
        assertEquals(endEpic, epic.getEndTime(), "Неверный расчет окончания эпика.");
    }
}