package teccr.justdoitcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/user/tasks")
@SessionAttributes("user")
public class UserTasksController {

    // =========================================
    // Crear usuario en sesión (con tareas demo)
    // =========================================
    @ModelAttribute(name = "user")
    public User user() {

        User usr = new User(
                "christine",
                "Christine McVie",
                "christine@fm.com",
                User.Type.REGULAR
        );

        Task task1 = new Task(
                UUID.randomUUID(),
                "Comprar Leche",
                LocalDateTime.now(),
                null,
                Task.Status.DONE
        );
        usr.addTask(task1);

        Task task2 = new Task(
                UUID.randomUUID(),
                "Reparacion de sistema de frenos del carro",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3).toLocalDate(),
                Task.Status.INPROGRESS
        );
        usr.addTask(task2);

        return usr;
    }

    // =========================================
    // GET - Mostrar tablero
    // =========================================
    @GetMapping
    public String showUserTasks(Model model,
                                @ModelAttribute("user") User user) {

        populateTaskLists(model, user);
        return "usertasks";
    }

    // =========================================
    // POST - Agregar nueva tarea
    // =========================================
    @PostMapping
    public String addTask(@RequestParam("description") String description,
                          @RequestParam("status") Task.Status status,
                          @ModelAttribute("user") User user,
                          Model model) {

        if (description == null || description.trim().length() < 3) {
            model.addAttribute("errorMessage",
                    "Descripcion debe tener al menos 3 caracteres");
            populateTaskLists(model, user);
            return "usertasks";
        }

        Task newTask = new Task(
                UUID.randomUUID(),
                description.trim(),
                LocalDateTime.now(),
                null,
                status
        );

        log.info("Adding task: {}", newTask);
        user.addTask(newTask);

        return "redirect:/user/tasks";
    }

    // =========================================
    // POST - Avanzar estado (UNA SOLA RUTA)
    // =========================================
    @PostMapping("/advance")
    public String advanceTask(@RequestParam("taskId") UUID taskId,
                              @ModelAttribute("user") User user) {

        log.info("Advancing task with id: {}", taskId);

        user.advanceTask(taskId);

        return "redirect:/user/tasks";
    }

    // =========================================
    // Método auxiliar para separar tareas
    // =========================================
    private void populateTaskLists(Model model, User user) {

        List<Task> allTasks = user.getTasks();

        model.addAttribute("pendingTasks",
                allTasks.stream()
                        .filter(t -> t.getStatus() == Task.Status.PENDING)
                        .collect(Collectors.toList()));

        model.addAttribute("inprogressTasks",
                allTasks.stream()
                        .filter(t -> t.getStatus() == Task.Status.INPROGRESS)
                        .collect(Collectors.toList()));

        model.addAttribute("doneTasks",
                allTasks.stream()
                        .filter(t -> t.getStatus() == Task.Status.DONE)
                        .collect(Collectors.toList()));
    }
}
