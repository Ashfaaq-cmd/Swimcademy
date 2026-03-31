public class User {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(int id, String fullName, String email, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId(){
    	return id; 
    	}
    public String getFullName() { 
    	return fullName; 
    	}
    public String getEmail(){
    	return email; 
    	}
    public String getPassword() {
    	return password; 
    	}
    public String getRole() { 
    	return role; 
    	}

    public void setId(int id) { 
    	this.id = id;
    	}
    public void setFullName(String n){
    	this.fullName = n; 
    	}
    public void setEmail(String e) {
    	this.email = e; 
    	}
    public void setPassword(String p) {
    	this.password = p;
    	}
    public void setRole(String r) { 
    	this.role = r; 
    	}
}