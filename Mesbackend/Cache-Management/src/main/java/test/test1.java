package test;

import pojo.User;

public class test1 {


    public static void main(String[] args) {
         UserService userService = new UserService();
         User user=userService.getUser("name");
        System.out.println(user);

    }
}
