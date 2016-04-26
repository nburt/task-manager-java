package com.nathanaelburt.taskmanager;

import com.nathanaelburt.taskmanager.entity.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@Controller
public class TaskController {

    @RequestMapping("/tasks")
    public String index(Model model) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        model.addAttribute("tasks", session.createCriteria(Task.class).list());
        session.close();
        return "tasks/index";
    }

    @RequestMapping(value = "/tasks/new", method = RequestMethod.GET)
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/new";
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET)
    public String showTask(@PathVariable(value="id") String id, Model model) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        model.addAttribute("task", session.get(Task.class, Long.parseLong(id)));
        session.close();
        return "tasks/show";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    public String createTask(@ModelAttribute Task task, Model model) {
        model.addAttribute("task", task);
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(task);

        session.getTransaction().commit();
        session.close();
        return "redirect:tasks/" + task.getId();
    }

}
