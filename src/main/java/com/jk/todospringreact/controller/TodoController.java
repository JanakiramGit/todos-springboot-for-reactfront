package com.jk.todospringreact.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jk.todospringreact.model.Todo;
import com.jk.todospringreact.model.TodoRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TodoController {
	
	@Autowired
	TodoRepository todoRepo;
	
	@GetMapping("/todos")
	public ResponseEntity<List<Todo>> getAllTodos(@RequestParam(required = false) String title) {
		try {
			List<Todo> todos = new ArrayList<Todo>();

			if (title == null)
				todoRepo.findAll().forEach(todos::add);
//			else
//				todoRepo.findByTitleContaining(title).forEach(tutorials::add);

			if (todos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(todos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
		try {
			Todo _todo = todoRepo.save(new Todo(null, todo.getTitle(), false));
			return new ResponseEntity<>(_todo, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteTodo(@PathVariable("id") long id) {
		try {
			todoRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable("id") long id, @RequestBody Todo todo) {
		Optional<Todo> todoData = todoRepo.findById(id);

		if (todoData.isPresent()) {
			Todo _todo = todoData.get();
			_todo.setTitle(todo.getTitle());
			_todo.setCompleted(todo.isCompleted());
			return new ResponseEntity<>(todoRepo.save(_todo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
