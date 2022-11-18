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
		
        //get("/counters", (req, res) -> counters.toJson());
		
		/*get("/hello", (req, res) -> "Hello World!");
		
        get("/counters", (req, res) -> counters.toJson());
 
        get("/counters/red", (req, res) -> counters.getRed());

        get("/counters/green", (req, res) -> counters.getGreen());

        // TODO: put for green/red and in JSON
        // variant that returns link/references to red and green counter
        put("/counters", (req,res) -> {
        
        	Gson gson = new Gson();
        	
        	counters = gson.fromJson(req.body(), Counters.class);
        
            return counters.toJson();
        	
        });*/
        
        
    }
}






/*
package no.hvl.dat250.rest.todos;

import com.google.gson.Gson;

import okhttp3.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import static spark.Spark.*;*/

/**
 * Rest-Endpoint.
 */
/*
public class TodoAPI {

    static List<Todo> todos = new ArrayList<Todo>();
    static Gson gson = new Gson();

    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        after((req, res) -> res.type("application/json"));

        get("/todos", (req, res) -> gson.toJson(todos));

        get("/todos/:id", (req, res) -> {
            String idStr = req.params("id");
            try {
                long id = Long.parseLong(idStr);
                Todo todo = null;
                for (Todo t : todos) {
                    if (t.getId() == id) {
                        todo = t;
                        break;
                    }
                }

                if (todo == null) {
                    res.status(404);
                    String _body = "Todo with the id  \""+ id +"\" not found!";
                    res.body(_body);

                } else {
                    res.status(200);
                    res.body(gson.toJson(todo));
                }
                
                System.out.print(res.body());
                System.out.print("\n");
                return res;
            } catch (NumberFormatException e) {
                res.status(400);
                res.body("The id \"" + idStr + "\" is not a number!");
                return res;
            }
        });

        post("/todos", (req, res) -> {
            try {
                Todo todoData = gson.fromJson(req.body(), Todo.class);
                Todo todo = new Todo((long)(Math.random() * 1_000_000_000), todoData.getSummary(), todoData.getDescription());
                todos.add(todo);
                res.status(201);
                res.body(gson.toJson(todo));
                return res;
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                res.status(400);
                return res;
            }
        });

        put("/todos/:id", (req, res) -> {
            String idStr = req.params("id");
            try {
                long id = Long.parseLong(idStr);
                Todo todoData = gson.fromJson(req.body(), Todo.class);
                Todo todo = new Todo(id, todoData.getSummary(), todoData.getDescription());
                boolean removed = todos.removeIf(t -> t.getId() == id);
                if (removed) {
                    todos.add(todo);
                    res.status(200);
                    res.body(gson.toJson(todo));
                } else {
                    res.status(404);
                }
                return res;
            } catch (NumberFormatException e) {
                res.status(400);
                res.body("The id \"" + idStr + "\" is not a number!");
                return res;
            } catch (Exception e) {
                res.status(400);
                return res;
            }
        });

        delete("/todos/:id", (req, res) -> {
            String idStr = req.params("id");
            try {
                long id = Long.parseLong(idStr);
                Todo todo = null;
                for (Todo t: todos) {
                    if (t.getId() == id) {
                        todo = t;
                        break;
                    }
                }

                if (todo == null) {
                    res.status(404);
                } else {
                    todos.remove(todo);
                    res.status(200);
                    res.body(gson.toJson(todo));
                }
                return res;
            } catch (NumberFormatException e) {
            res.status(400);
            res.body("The id \"" + idStr + "\" is not a number!");
            return res;
        } catch (Exception e) {
            res.status(400);
            return res;
        }
        });
    }
}*/


