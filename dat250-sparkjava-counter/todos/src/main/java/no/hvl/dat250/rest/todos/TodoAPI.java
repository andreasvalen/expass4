package no.hvl.dat250.rest.todos;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;





public class TodoAPI {

	static List<Todo> todos = new ArrayList<Todo>();
	static Long horribleWayOfCreatingIds = 1L;
	
    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }
        
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
        post("/todos", (req, res) -> {
            
        	Gson gson = new Gson();
        	horribleWayOfCreatingIds++;
        	Todo _todo = gson.fromJson(req.body(), Todo.class);
        	Todo todo = new Todo(horribleWayOfCreatingIds, _todo.getSummary(), _todo.getDescription());
 
        	todos.add(todo);
        	res.body(gson.toJson(todo));
        	res.status(201);
        
            return res;
        	
        });
        
        get("/todos/:id", (req, res) -> {
            
        	String _id = req.params("id");
        	Long id = -1L;
        	try {
            	 id= Long.parseLong(_id);
        	}
        	catch(Exception  e){
        		return "The id \"" +_id + "\" is not a number!";    		
        	}
        	
        	boolean foundItem = false;
        	Gson gson = new Gson();
        	for (Todo t : todos) {
        		if(t.getId()==id) {
        			res.status(200);
        			res.body(gson.toJson(t));
        			foundItem=true;
        		}
        	}
             	
        	if(!foundItem) {
        		return "Todo with the id \"" +_id + "\" not found!";
        	}
        	  
            return res;
        	
        });
        
        put("/todos/:id", (req, res) -> {
            
        	String _id = req.params("id");
        	Long id = -1L;
        	try {
            	 id= Long.parseLong(_id);
        	}
        	catch(Exception  e){
        		return "The id \"" +_id + "\" is not a number!";    		
        	}
        	Gson gson = new Gson();
        	Todo _todo = gson.fromJson(req.body(), Todo.class);
        	Todo todo = null;
        	
        	int indexOfObj = 0;        	
        	for (Todo t : todos) {
        		if(t.getId()==id) {
        			indexOfObj=todos.indexOf(t);
        			todo= new Todo(t.getId(),  _todo.getSummary(), _todo.getDescription());
        		}
        	}
        	todos.remove(indexOfObj);
        	todos.add(todo);

			res.status(200);
			res.body(gson.toJson(todo));

            return res;
        	
        });
        
        delete("/todos/:id", (req, res) -> {          
        	String _id = req.params("id");
        	Long id = -1L;
        	try {
            	 id= Long.parseLong(_id);
        	}
        	catch(Exception  e){
        		return "The id \"" +_id + "\" is not a number!";    		
        	}
     
        	int indexOfObj = -1;        	
        	for (Todo t : todos) {
        		if(t.getId()==id) {
        			indexOfObj=todos.indexOf(t);
        			}
        	}
        	
        	if(indexOfObj==-1) {
        		return "Todo with the id \"" +_id + "\" not found!";
        	     	        		
        	}else {
        		todos.remove(indexOfObj);
        		res.status(200);        		
        	}
        	

            return res;
        	
        });
        
        get("/todos", (req, res) -> {
        	Gson gson = new Gson();
        	res.status(200);
			res.body(gson.toJson(todos));
        	  
            return res;
        	
        });
        
    }
}

