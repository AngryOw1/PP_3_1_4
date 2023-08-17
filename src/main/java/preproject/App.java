package preproject;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import preproject.configuration.MyConfig;
import preproject.models.User;

public class App 
{
    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);

        Communication communication = context.getBean("communication", Communication.class);

        communication.getAllUsers();
        User user = new User(3L, "James", "Brown", (byte) 2);
        communication.createUser(user);
        user.setName("Thomas");
        user.setLastName("Shelby");
        communication.editUser(user);
        communication.deleteUser(3L);

        System.out.println(communication.getResponsesBody());
    }
}
